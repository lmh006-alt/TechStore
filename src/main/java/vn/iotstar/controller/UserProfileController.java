package vn.iotstar.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Locale;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.CloudinaryUtil;
import vn.iotstar.util.EmailUtil;
import vn.iotstar.util.PasswordUtil;

@WebServlet("/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = CloudinaryUtil.MAX_AVATAR_SIZE,
        maxRequestSize = 6L * 1024 * 1024)
public class UserProfileController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CSRF_ATTRIBUTE = "profileCsrfToken";
    private static final String OTP_ATTRIBUTE = "profileEmailChange";
    private static final long OTP_LIFETIME_MILLIS = 5 * 60 * 1000L;
    private static final long RESEND_COOLDOWN_MILLIS = 60 * 1000L;
    private static final long SEND_WINDOW_MILLIS = 15 * 60 * 1000L;
    private static final int MAX_OTP_ATTEMPTS = 5;
    private static final int MAX_OTP_SENDS_PER_WINDOW = 5;
    private static final Pattern EMAIL = Pattern.compile(
            "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?"
            + "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*");
    private static final Pattern PHONE = Pattern.compile("\\+?[0-9][0-9 .()-]*");

    private final UserService userService = new UserServiceImpl();
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = userService.getById(userId);
        if (user == null) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        ensureCsrfToken(session);
        Object flash = session.getAttribute("profileFlash");
        if (flash instanceof String) {
            req.setAttribute("profileSuccess", flash);
            session.removeAttribute("profileFlash");
        }
        renderProfile(req, resp, session, user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        Integer userId = getAuthenticatedUserId(session);
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (isMultipartRequest(req)) {
            try {
                req.getParts();
            } catch (IllegalStateException e) {
                resp.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
                        "Ảnh đại diện không được vượt quá 5 MB.");
                return;
            } catch (ServletException | IOException e) {
                log("Không thể đọc biểu mẫu tải ảnh đại diện.", e);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Không thể đọc biểu mẫu tải ảnh. Vui lòng thử lại.");
                return;
            }
        }
        if (!isValidCsrfToken(session, req.getParameter("csrfToken"))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Yêu cầu không hợp lệ. Vui lòng tải lại trang.");
            return;
        }

        User user = userService.getById(userId);
        if (user == null) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if ("update-profile".equals(action)) {
            updateProfile(req, resp, session, user, userId);
        } else if ("update-avatar".equals(action)) {
            updateAvatar(req, resp, session, user, userId);
        } else if ("send-email-otp".equals(action)) {
            sendEmailOtp(req, resp, session, user, userId, false);
        } else if ("resend-email-otp".equals(action)) {
            sendEmailOtp(req, resp, session, user, userId, true);
        } else if ("verify-email-otp".equals(action)) {
            verifyEmailOtp(req, resp, session, user, userId);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Thao tác hồ sơ không hợp lệ.");
        }
    }

    private void updateProfile(HttpServletRequest req,
            HttpServletResponse resp, HttpSession session, User user, int userId)
            throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        req.setAttribute("profileFullName", fullName == null ? "" : fullName);
        req.setAttribute("profilePhone", phone == null ? "" : phone);

        String normalizedName = fullName == null ? "" : fullName.trim();
        String normalizedPhone = phone == null ? "" : phone.trim();
        if (normalizedName.isEmpty() || normalizedName.length() > 255) {
            req.setAttribute("profileError",
                    "Họ tên là bắt buộc và tối đa 255 ký tự.");
        } else if (!normalizedPhone.isEmpty()
                && (normalizedPhone.length() > 20
                    || !PHONE.matcher(normalizedPhone).matches()
                    || !normalizedPhone.replaceAll("[^0-9]", "")
                            .matches("[0-9]{7,15}"))) {
            req.setAttribute("profileError",
                    "Số điện thoại cần có 7–15 chữ số và tối đa 20 ký tự.");
        } else if (!userService.updateProfile(
                userId, normalizedName, normalizedPhone)) {
            req.setAttribute("profileError",
                    "Không thể cập nhật thông tin. Vui lòng thử lại.");
        } else {
            session.setAttribute("profileFlash",
                    "Thông tin cá nhân đã được cập nhật.");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        renderProfile(req, resp, session, userService.getById(userId));
    }

    private void updateAvatar(HttpServletRequest req,
            HttpServletResponse resp, HttpSession session, User user, int userId)
            throws ServletException, IOException {
        if (!isMultipartRequest(req)) {
            req.setAttribute("profileError",
                    "Biểu mẫu tải ảnh không hợp lệ. Vui lòng thử lại.");
            renderProfile(req, resp, session, user);
            return;
        }

        Part avatarPart = req.getPart("avatar");
        if (avatarPart == null || avatarPart.getSubmittedFileName() == null
                || avatarPart.getSubmittedFileName().isBlank()) {
            req.setAttribute("profileError", "Vui lòng chọn ảnh đại diện.");
            renderProfile(req, resp, session, user);
            return;
        }

        String avatarUrl;
        try (var input = avatarPart.getInputStream()) {
            avatarUrl = CloudinaryUtil.uploadAvatar(
                    input.readAllBytes(), avatarPart.getSubmittedFileName());
        } catch (IllegalArgumentException e) {
            req.setAttribute("profileError", e.getMessage());
            renderProfile(req, resp, session, user);
            return;
        } catch (IOException | RuntimeException e) {
            log("Không thể tải ảnh đại diện lên Cloudinary.", e);
            req.setAttribute("profileError",
                    "Không thể tải ảnh đại diện. Vui lòng kiểm tra cấu hình Cloudinary và thử lại.");
            renderProfile(req, resp, session, user);
            return;
        }

        try {
            if (!userService.updateAvatar(userId, avatarUrl)) {
                req.setAttribute("profileError",
                        "Không thể lưu ảnh đại diện. Vui lòng thử lại.");
                renderProfile(req, resp, session, userService.getById(userId));
                return;
            }
        } catch (RuntimeException e) {
            log("Không thể lưu URL ảnh đại diện vào hồ sơ người dùng.", e);
            req.setAttribute("profileError",
                    "Ảnh đã tải lên nhưng không thể lưu vào hồ sơ. Vui lòng thử lại.");
            renderProfile(req, resp, session, userService.getById(userId));
            return;
        }

        session.setAttribute("profileFlash",
                "Ảnh đại diện đã được cập nhật.");
        resp.sendRedirect(req.getContextPath() + "/profile");
    }

    private void sendEmailOtp(HttpServletRequest req,
            HttpServletResponse resp, HttpSession session, User user,
            int userId, boolean resend) throws ServletException, IOException {
        String email;
        if (resend) {
            synchronized (session) {
                EmailChangeChallenge challenge = getChallenge(session);
                if (challenge == null || challenge.userId != userId
                        || challenge.email == null) {
                    req.setAttribute("profileError",
                            "Không có yêu cầu đổi email để gửi lại OTP.");
                    renderProfile(req, resp, session, user);
                    return;
                }
                email = challenge.email;
            }
        } else {
            String submittedEmail = req.getParameter("newEmail");
            email = submittedEmail == null ? "" : submittedEmail.trim();
            req.setAttribute("emailFormValue", email);
            if (email.isEmpty() || email.length() > 255
                    || !EMAIL.matcher(email).matches()) {
                req.setAttribute("profileError",
                        "Vui lòng nhập email hợp lệ, tối đa 255 ký tự.");
                renderProfile(req, resp, session, user);
                return;
            }
        }

        if (email.equalsIgnoreCase(user.getEmail())) {
            req.setAttribute("profileError",
                    "Email mới phải khác email hiện tại.");
            renderProfile(req, resp, session, user);
            return;
        }
        if (userService.checkEmailUsedByOtherUser(email, userId)) {
            req.setAttribute("profileError",
                    "Email này đã được một tài khoản khác sử dụng.");
            renderProfile(req, resp, session, user);
            return;
        }

        long now = System.currentTimeMillis();
        synchronized (session) {
            EmailChangeChallenge challenge = getChallenge(session);
            if (challenge == null || challenge.userId != userId) {
                challenge = new EmailChangeChallenge(userId);
            }
            pruneSendTimes(challenge, now);

            if (now < challenge.resendAllowedAt) {
                req.setAttribute("profileError",
                        "Vui lòng chờ " + secondsUntil(challenge.resendAllowedAt, now)
                        + " giây trước khi gửi lại OTP.");
                renderProfile(req, resp, session, user);
                return;
            }
            if (challenge.sendTimes.size() >= MAX_OTP_SENDS_PER_WINDOW) {
                req.setAttribute("profileError",
                        "Đã đạt giới hạn 5 lần gửi OTP trong 15 phút.");
                renderProfile(req, resp, session, user);
                return;
            }

            challenge.email = email;
            challenge.otpHash = null;
            challenge.expiresAt = 0;
            challenge.attempts = 0;
            challenge.resendAllowedAt = now + RESEND_COOLDOWN_MILLIS;
            challenge.sendTimes.add(now);
            session.setAttribute(OTP_ATTRIBUTE, challenge);

            String otp = String.format(Locale.ROOT, "%06d",
                    secureRandom.nextInt(1_000_000));
            try {
                EmailUtil.sendEmailChangeOTP(email, otp);
                challenge.otpHash = PasswordUtil.hash(otp);
                challenge.expiresAt = now + OTP_LIFETIME_MILLIS;
                session.setAttribute(OTP_ATTRIBUTE, challenge);
                session.setAttribute("profileFlash",
                        "OTP đã được gửi đến email mới và có hiệu lực trong 5 phút.");
                resp.sendRedirect(req.getContextPath() + "/profile");
                return;
            } catch (Exception e) {
                log("Không thể gửi OTP xác nhận email mới.", e);
                req.setAttribute("profileError",
                        "Không thể gửi OTP. Vui lòng thử lại sau khi hết thời gian chờ.");
                renderProfile(req, resp, session, user);
            }
        }
    }

    private void verifyEmailOtp(HttpServletRequest req,
            HttpServletResponse resp, HttpSession session, User user,
            int userId) throws ServletException, IOException {
        String otp = req.getParameter("otp");
        boolean updated = false;
        String error = null;

        synchronized (session) {
            EmailChangeChallenge challenge = getChallenge(session);
            if (challenge == null || challenge.userId != userId
                    || challenge.otpHash == null) {
                error = "Không có OTP hợp lệ. Vui lòng yêu cầu gửi OTP mới.";
            } else if (System.currentTimeMillis() >= challenge.expiresAt) {
                challenge.otpHash = null;
                challenge.expiresAt = 0;
                error = "OTP đã hết hạn. Vui lòng yêu cầu gửi lại.";
            } else if (otp == null || !otp.matches("[0-9]{6}")
                    || !PasswordUtil.verify(otp, challenge.otpHash)) {
                challenge.attempts++;
                if (challenge.attempts >= MAX_OTP_ATTEMPTS) {
                    challenge.otpHash = null;
                    challenge.expiresAt = 0;
                    error = "Bạn đã nhập sai OTP 5 lần. Vui lòng yêu cầu mã mới.";
                } else {
                    error = "OTP không đúng. Còn "
                            + (MAX_OTP_ATTEMPTS - challenge.attempts)
                            + " lần thử.";
                }
            } else if (!userService.updateEmail(
                    userId, challenge.email)) {
                challenge.otpHash = null;
                challenge.expiresAt = 0;
                error = "Email này đã được sử dụng hoặc không thể cập nhật.";
            } else {
                session.removeAttribute(OTP_ATTRIBUTE);
                session.setAttribute("profileFlash",
                        "Email đã được thay đổi thành công.");
                updated = true;
            }
            if (!updated && challenge != null) {
                session.setAttribute(OTP_ATTRIBUTE, challenge);
            }
        }

        if (updated) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }
        req.setAttribute("profileError", error);
        renderProfile(req, resp, session, userService.getById(userId));
    }

    private void renderProfile(HttpServletRequest req,
            HttpServletResponse resp, HttpSession session, User user)
            throws ServletException, IOException {
        if (user == null) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        ensureCsrfToken(session);
        req.setAttribute("csrfToken", session.getAttribute(CSRF_ATTRIBUTE));
        if (req.getAttribute("profileFullName") == null) {
            req.setAttribute("profileFullName",
                    user.getFullName() == null ? "" : user.getFullName());
        }
        if (req.getAttribute("profilePhone") == null) {
            req.setAttribute("profilePhone",
                    user.getPhone() == null ? "" : user.getPhone());
        }
        if (req.getAttribute("emailFormValue") == null) {
            req.setAttribute("emailFormValue", "");
        }

        synchronized (session) {
            EmailChangeChallenge challenge = getChallenge(session);
            long now = System.currentTimeMillis();
            if (challenge != null && challenge.userId == user.getId()) {
                pruneSendTimes(challenge, now);
                if (challenge.otpHash != null && now >= challenge.expiresAt) {
                    challenge.otpHash = null;
                    challenge.expiresAt = 0;
                }
                session.setAttribute(OTP_ATTRIBUTE, challenge);
                boolean pending = challenge.otpHash != null;
                req.setAttribute("otpPending", pending);
                req.setAttribute("pendingEmail",
                        challenge.email == null ? "" : challenge.email);
                long resendAvailableAt = getResendAvailableAt(challenge);
                req.setAttribute("hasEmailChallenge",
                        challenge.email != null);
                req.setAttribute("canResend", now >= resendAvailableAt);
                req.setAttribute("resendWaitSeconds",
                        secondsUntil(resendAvailableAt, now));
            } else {
                req.setAttribute("otpPending", false);
                req.setAttribute("pendingEmail", "");
                req.setAttribute("hasEmailChallenge", false);
                req.setAttribute("canResend", false);
                req.setAttribute("resendWaitSeconds", 0);
            }
        }

        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/user-profile.jsp")
                .forward(req, resp);
    }

    private Integer getAuthenticatedUserId(HttpSession session) {
        if (session == null || session.getAttribute("account") == null) {
            return null;
        }
        Object userId = session.getAttribute("userId");
        return userId instanceof Integer && (Integer) userId > 0
                ? (Integer) userId : null;
    }

    private boolean isMultipartRequest(HttpServletRequest req) {
        String contentType = req.getContentType();
        return contentType != null
                && contentType.toLowerCase(Locale.ROOT)
                        .startsWith("multipart/form-data");
    }

    private void ensureCsrfToken(HttpSession session) {
        if (!(session.getAttribute(CSRF_ATTRIBUTE) instanceof String)) {
            byte[] token = new byte[32];
            secureRandom.nextBytes(token);
            session.setAttribute(CSRF_ATTRIBUTE,
                    Base64.getUrlEncoder().withoutPadding().encodeToString(token));
        }
    }

    private boolean isValidCsrfToken(HttpSession session, String submitted) {
        Object expected = session.getAttribute(CSRF_ATTRIBUTE);
        return expected instanceof String && submitted != null
                && MessageDigest.isEqual(
                        ((String) expected).getBytes(StandardCharsets.UTF_8),
                        submitted.getBytes(StandardCharsets.UTF_8));
    }

    private EmailChangeChallenge getChallenge(HttpSession session) {
        Object challenge = session.getAttribute(OTP_ATTRIBUTE);
        return challenge instanceof EmailChangeChallenge
                ? (EmailChangeChallenge) challenge : null;
    }

    private void pruneSendTimes(EmailChangeChallenge challenge, long now) {
        challenge.sendTimes.removeIf(
                sentAt -> now - sentAt >= SEND_WINDOW_MILLIS);
    }

    private long secondsUntil(long timestamp, long now) {
        return Math.max(0, (timestamp - now + 999) / 1000);
    }

    private long getResendAvailableAt(EmailChangeChallenge challenge) {
        long availableAt = challenge.resendAllowedAt;
        if (challenge.sendTimes.size() >= MAX_OTP_SENDS_PER_WINDOW) {
            availableAt = Math.max(availableAt,
                    challenge.sendTimes.get(0) + SEND_WINDOW_MILLIS);
        }
        return availableAt;
    }

    private static final class EmailChangeChallenge
            implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        private final int userId;
        private String email;
        private String otpHash;
        private long expiresAt;
        private long resendAllowedAt;
        private int attempts;
        private final ArrayList<Long> sendTimes = new ArrayList<>();

        private EmailChangeChallenge(int userId) {
            this.userId = userId;
        }
    }
}
