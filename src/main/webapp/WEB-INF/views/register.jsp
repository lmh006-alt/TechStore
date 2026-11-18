<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký tài khoản | TechStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body class="auth-page">
    <main class="auth-box">
        <a class="logo" href="${pageContext.request.contextPath}/home">Tech<span>Store</span></a>
        <h1>Đăng ký tài khoản</h1>
        <p class="subtitle">Tạo tài khoản mới để bắt đầu</p>
        <div class="error" role="alert" ${empty alert ? 'hidden' : ''}>${fn:escapeXml(alert)}</div>
        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="field"><label for="username">Tên đăng nhập</label><input id="username" name="username"
                    type="text" autocomplete="username" required></div>
            <div class="field"><label for="password">Mật khẩu</label><input id="password" name="password"
                    type="password" minlength="8" autocomplete="new-password" required></div>
            <div class="field"><label for="email">Email</label><input id="email" name="email" type="email"
                    autocomplete="email" required></div>
            <div class="field"><label for="fullname">Họ tên</label><input id="fullname" name="fullname"
                    type="text" autocomplete="name"></div>
            <div class="field"><label for="phone">Số điện thoại</label><input id="phone" name="phone"
                    type="text" autocomplete="tel"></div>
            <button class="btn btn-block" type="submit">Đăng ký</button>
        </form>
        <p class="auth-footer">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
    </main>
</body>

</html>
