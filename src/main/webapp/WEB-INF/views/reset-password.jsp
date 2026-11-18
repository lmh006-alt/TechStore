<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu | TechStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body class="auth-page">
    <main class="auth-box">
        <a class="logo" href="${pageContext.request.contextPath}/home">Tech<span>Store</span></a>
        <h1>Đặt lại mật khẩu</h1>
        <p class="subtitle">Nhập mã OTP từ email và mật khẩu mới</p>
        <div class="error" role="alert" ${empty error ? 'hidden' : ''}>${fn:escapeXml(error)}</div>
        <form action="${pageContext.request.contextPath}/reset-password" method="post">
            <input type="hidden" name="username"
                value="${fn:escapeXml(not empty param.username ? param.username : username)}">
            <div class="field"><label for="otp">Mã OTP (6 số)</label><input id="otp" name="otp" type="text"
                    pattern="[0-9]{6}" maxlength="6" inputmode="numeric" autocomplete="one-time-code" required>
            </div>
            <div class="field"><label for="newPassword">Mật khẩu mới</label><input id="newPassword"
                    name="newPassword" type="password" minlength="8" autocomplete="new-password" required></div>
            <div class="field"><label for="confirmPassword">Xác nhận mật khẩu</label><input id="confirmPassword"
                    name="confirmPassword" type="password" minlength="8" autocomplete="new-password" required>
            </div>
            <button class="btn btn-block" type="submit">Đổi mật khẩu</button>
        </form>
        <p class="auth-footer"><a href="${pageContext.request.contextPath}/forgot-password">Gửi lại yêu cầu OTP</a>
        </p>
    </main>
</body>

</html>
