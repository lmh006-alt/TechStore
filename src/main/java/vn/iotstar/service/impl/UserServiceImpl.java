
package vn.iotstar.service.impl;

import java.security.SecureRandom;
import java.util.Date;
import java.util.regex.Pattern;

import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.util.PasswordUtil;
import vn.iotstar.util.EmailUtil;

public class UserServiceImpl implements UserService {

    private static final Pattern EMAIL = Pattern.compile(
            "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?"
            + "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*");
    private static final Pattern PHONE = Pattern.compile("\\+?[0-9][0-9 .()-]*");

    private final UserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = userDao.get(username);

        if (user != null
                && user.isActive()
                && PasswordUtil.verify(password, user.getPassWord())) {
            return user;
        }

        return null;
    }

    @Override
    public User get(String username) {
        return userDao.get(username);
    }

    @Override
    public User getById(int userId) {
        return userId > 0 ? userDao.getById(userId) : null;
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.existsByEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.get(username) != null;
    }

    @Override
    public boolean updateProfile(int userId, String fullName, String phone) {
        if (userId <= 0 || fullName == null || fullName.isBlank()
                || fullName.length() > 255) {
            return false;
        }

        String normalizedPhone = phone == null || phone.isBlank()
                ? null : phone.trim();
        if (normalizedPhone != null
                && (normalizedPhone.length() > 20
                    || !PHONE.matcher(normalizedPhone).matches()
                    || !normalizedPhone.replaceAll("[^0-9]", "")
                            .matches("[0-9]{7,15}"))) {
            return false;
        }

        return userDao.updateProfile(userId, fullName.trim(), normalizedPhone);
    }

    @Override
    public boolean updateAvatar(int userId, String avatar) {
        if (userId <= 0 || avatar == null || avatar.isBlank()
                || avatar.length() > 255
                || !avatar.startsWith("https://")) {
            return false;
        }
        return userDao.updateAvatar(userId, avatar);
    }

    @Override
    public boolean checkEmailUsedByOtherUser(String email, int userId) {
        return email != null && userId > 0
                && userDao.existsByEmailForOtherUser(email, userId);
    }

    @Override
    public boolean updateEmail(int userId, String email) {
        if (userId <= 0 || email == null || email.length() > 255
                || !EMAIL.matcher(email).matches()
                || checkEmailUsedByOtherUser(email, userId)) {
            return false;
        }

        return userDao.updateEmail(userId, email);
    }

    @Override
    public boolean register(String username, String password,
            String email, String fullname, String phone) {

        if (checkExistUsername(username) || checkExistEmail(email)) {
            return false;
        }

        String otp = String.format("%06d",
                new SecureRandom().nextInt(1000000));

        User user = new User();
        user.setUserName(username);
        user.setPassWord(PasswordUtil.hash(password));
        user.setEmail(email);
        user.setFullName(fullname);
        user.setPhone(phone);
        user.setRoleid(2);
        user.setActive(false);

        user.setOtpCode(PasswordUtil.hash(otp));
        user.setOtpExpiry(new Date(
                System.currentTimeMillis() + 5 * 60 * 1000));

        boolean success = userDao.insert(user);
        if (!success) {
            return false;
        }

        try {
            EmailUtil.sendOTP(email, otp);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean verifyOTP(String username, String otp) {
        User user = userDao.get(username);

        if (user == null || user.isActive()) {
            return false;
        }

        if (otp == null || user.getOtpCode() == null
                || user.getOtpExpiry() == null) {
            return false;
        }

        if (new Date().after(user.getOtpExpiry())) {
            return false;
        }

        if (!PasswordUtil.verify(otp, user.getOtpCode())) {
            return false;
        }

        return userDao.activateAccount(user.getId());
    }

    // BAI 3: Gui OTP quen mat khau
    @Override
    public boolean sendResetOTP(String username) {
        User user = userDao.get(username);

        if (user == null || !user.isActive()) {
            return false;
        }

        String otp = String.format("%06d",
                new SecureRandom().nextInt(1000000));

        String otpHash = PasswordUtil.hash(otp);
        Date expiry = new Date(
                System.currentTimeMillis() + 5 * 60 * 1000);

        if (!userDao.saveResetOTP(user.getId(), otpHash, expiry)) {
            return false;
        }

        try {
            EmailUtil.sendOTP(user.getEmail(), otp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // BAI 3: Xac minh OTP va doi mat khau
    @Override
    public boolean resetPassword(String username,
            String otp, String newPassword) {

        User user = userDao.get(username);

        if (user == null || !user.isActive()) {
            return false;
        }

        if (otp == null || !otp.matches("\\d{6}")
                || newPassword == null || newPassword.length() < 8) {
            return false;
        }

        if (user.getResetOtpCode() == null
                || user.getResetOtpExpiry() == null) {
            return false;
        }

        if (!new Date().before(user.getResetOtpExpiry())) {
            return false;
        }

        if (!PasswordUtil.verify(otp, user.getResetOtpCode())) {
            return false;
        }

        String passwordHash = PasswordUtil.hash(newPassword);

        return userDao.resetPassword(
                user.getId(), passwordHash, user.getResetOtpCode());
    }
}
