<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
</head>
<body>
    <h2>Đăng ký tài khoản</h2>
    <p style="color:red">${alert}</p>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <p>Tài khoản:</p>
        <input type="text" name="username" required>
        <p>Mật khẩu:</p>
        <input type="password" name="password" minlength="8" required>
        <p>Email:</p>
        <input type="email" name="email" required>
        <p>Họ tên:</p>
        <input type="text" name="fullname">
        <p>Số điện thoại:</p>
        <input type="text" name="phone">
        <p>
            <button type="submit">Đăng ký</button>
        </p>
    </form>
    <a href="${pageContext.request.contextPath}/login">
        Quay lại đăng nhập
    </a>
</body>
</html>
