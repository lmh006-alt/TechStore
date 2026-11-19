<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <title>Hồ sơ cá nhân | TechStore</title>
</head>

<body>
    <main class="page-container page-narrow">
        <div class="page-heading">
            <div>
                <p class="breadcrumb">Tài khoản / Hồ sơ cá nhân</p>
                <h1>Hồ sơ cá nhân</h1>
                <p class="subtitle">Quản lý thông tin và địa chỉ email của bạn.</p>
            </div>
        </div>

        <div class="alert alert-danger" role="alert"
             ${empty profileError ? 'hidden' : ''}>
            <c:out value="${profileError}"/>
        </div>
        <div class="alert alert-success" role="status"
             ${empty profileSuccess ? 'hidden' : ''}>
            <c:out value="${profileSuccess}"/>
        </div>

        <section class="panel mb-4" aria-labelledby="personal-info-heading">
            <h2 id="personal-info-heading">Thông tin cá nhân</h2>
            <p class="muted">Tên đăng nhập: <strong><c:out value="${user.userName}"/></strong></p>
            <p class="muted">Email hiện tại: <strong><c:out value="${user.email}"/></strong></p>

            <div class="d-flex align-items-center gap-3 mb-4">
                <c:choose>
                    <c:when test="${not empty user.avatar}">
                        <img class="rounded-circle border object-fit-cover"
                             src="${fn:escapeXml(user.avatar)}"
                             alt="Ảnh đại diện hiện tại" width="96" height="96">
                    </c:when>
                    <c:otherwise>
                        <div class="rounded-circle border bg-light d-flex align-items-center
                                    justify-content-center text-secondary"
                             role="img" aria-label="Chưa có ảnh đại diện"
                             style="width: 96px; height: 96px; font-size: 2rem;">
                            <span aria-hidden="true">?</span>
                        </div>
                    </c:otherwise>
                </c:choose>
                <div>
                    <h3 class="h5 mb-1">Ảnh đại diện</h3>
                    <p class="form-text mb-0">JPG, JPEG, PNG hoặc WEBP; tối đa 5 MB.</p>
                </div>
            </div>
            <form data-validate action="${pageContext.request.contextPath}/profile"
                  method="post" enctype="multipart/form-data" class="mb-4">
                <input type="hidden" name="csrfToken"
                       value="${fn:escapeXml(csrfToken)}">
                <input type="hidden" name="action" value="update-avatar">
                <div class="mb-3">
                    <label class="form-label" for="avatar">Chọn ảnh đại diện</label>
                    <input class="form-control" id="avatar" name="avatar"
                           type="file" accept=".jpg,.jpeg,.png,.webp,image/jpeg,image/png,image/webp"
                           data-max-bytes="5242880"
                           data-file-message="Chỉ chấp nhận ảnh JPG, JPEG, PNG hoặc WEBP."
                           required>
                </div>
                <button class="btn btn-primary" type="submit">
                    Cập nhật ảnh đại diện
                </button>
            </form>

            <form data-validate action="${pageContext.request.contextPath}/profile"
                  method="post">
                <input type="hidden" name="csrfToken"
                       value="${fn:escapeXml(csrfToken)}">
                <input type="hidden" name="action" value="update-profile">

                <div class="mb-3">
                    <label class="form-label" for="fullName">Họ và tên</label>
                    <input class="form-control" id="fullName" name="fullName"
                           type="text" maxlength="255" data-not-blank
                           autocomplete="name" required
                           value="${fn:escapeXml(profileFullName)}">
                </div>
                <div class="mb-3">
                    <label class="form-label" for="phone">Số điện thoại</label>
                    <input class="form-control" id="phone" name="phone"
                           type="tel" maxlength="20" data-phone
                           autocomplete="tel"
                           value="${fn:escapeXml(profilePhone)}">
                    <div class="form-text">Có thể để trống; nếu nhập cần có 7–15 chữ số.</div>
                </div>
                <button class="btn btn-primary" type="submit">
                    Lưu thông tin
                </button>
            </form>
        </section>

        <section class="panel" aria-labelledby="email-heading">
            <h2 id="email-heading">Thay đổi email</h2>
            <p class="muted">
                Chúng tôi sẽ gửi mã OTP đến email mới. Email chỉ được cập nhật
                sau khi mã được xác thực.
            </p>

            <form data-validate action="${pageContext.request.contextPath}/profile"
                  method="post" class="mb-3">
                <input type="hidden" name="csrfToken"
                       value="${fn:escapeXml(csrfToken)}">
                <input type="hidden" name="action" value="send-email-otp">
                <div class="mb-3">
                    <label class="form-label" for="newEmail">Email mới</label>
                    <input class="form-control" id="newEmail" name="newEmail"
                           type="email" maxlength="255" autocomplete="email"
                           data-not-blank required
                           value="${fn:escapeXml(emailFormValue)}">
                </div>
                <button class="btn btn-primary" type="submit">Gửi OTP</button>
            </form>

            <c:if test="${otpPending}">
                <div class="alert alert-info" role="status">
                    OTP đã gửi đến <strong><c:out value="${pendingEmail}"/></strong>.
                    Mã hết hạn sau 5 phút.
                </div>
                <form data-validate action="${pageContext.request.contextPath}/profile"
                      method="post" class="mb-3">
                    <input type="hidden" name="csrfToken"
                           value="${fn:escapeXml(csrfToken)}">
                    <input type="hidden" name="action" value="verify-email-otp">
                    <div class="mb-3">
                        <label class="form-label" for="otp">Mã OTP</label>
                        <input class="form-control" id="otp" name="otp"
                               type="text" inputmode="numeric"
                               autocomplete="one-time-code" maxlength="6"
                               pattern="[0-9]{6}" data-not-blank required>
                    </div>
                    <button class="btn btn-primary" type="submit">
                        Xác nhận email mới
                    </button>
                </form>
            </c:if>

            <form action="${pageContext.request.contextPath}/profile"
                  method="post">
                <input type="hidden" name="csrfToken"
                       value="${fn:escapeXml(csrfToken)}">
                <input type="hidden" name="action" value="resend-email-otp">
                <button class="btn btn-outline-primary" type="submit"
                        ${not hasEmailChallenge or not canResend ? 'disabled' : ''}>
                    Gửi lại OTP
                </button>
                <c:if test="${hasEmailChallenge and not canResend}">
                    <span class="form-text ms-2">
                        Có thể gửi lại sau ${resendWaitSeconds} giây.
                    </span>
                </c:if>
            </form>
        </section>
    </main>
</body>

</html>
