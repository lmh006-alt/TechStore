<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
    <h2>Đăng nhập</h2>
    <p style="color:red">${alert}</p>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <p>Tài khoản:</p>
        <input type="text" name="username" value="${fn:escapeXml(rememberedUsername)}" required>
        <p>Mật khẩu:</p>
        <input type="password" name="password" required>
        <p>
            <label>
                <input type="checkbox" name="remember">
                Ghi nhớ tài khoản bằng Cookie
            </label>
        </p>
        <button type="submit">Đăng nhập</button>
    </form>
    <p>
        Chưa có tài khoản?
        <a href="${pageContext.request.contextPath}/register">
            Đăng ký
        </a>
    </p>
</body>
</html>
