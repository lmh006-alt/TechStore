<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kích hoạt tài khoản | TechStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body class="auth-page">
    <main class="auth-box">
        <a class="logo" href="${pageContext.request.contextPath}/home">Tech<span>Store</span></a>
        <h1>Kích hoạt tài khoản</h1>
        <p class="subtitle">Nhập mã OTP gồm 6 chữ số đã gửi đến email</p>
        <div class="error" role="alert" ${empty alert ? 'hidden' : ''}>${fn:escapeXml(alert)}</div>
        <form action="${pageContext.request.contextPath}/verify-otp" method="post">
            <div class="field"><label for="username">Tên đăng nhập</label><input id="username" name="username"
                    type="text" value="${fn:escapeXml(param.username)}" required></div>
            <div class="field"><label for="otp">Mã OTP</label><input id="otp" name="otp" type="text"
                    maxlength="6" pattern="[0-9]{6}" inputmode="numeric" autocomplete="one-time-code" required>
            </div>
            <button class="btn btn-block" type="submit">Xác nhận OTP</button>
        </form>
        <p class="auth-footer"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
    </main>
</body>

</html>
