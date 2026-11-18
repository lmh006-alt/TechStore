<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu | TechStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body class="auth-page">
    <main class="auth-box">
        <a class="logo" href="${pageContext.request.contextPath}/home">Tech<span>Store</span></a>
        <h1>Quên mật khẩu</h1>
        <p class="subtitle">Nhập tên đăng nhập để nhận mã OTP qua email</p>
        <div class="error" role="alert" ${empty error ? 'hidden' : ''}>${fn:escapeXml(error)}</div>
        <form action="${pageContext.request.contextPath}/forgot-password" method="post">
            <div class="field"><label for="username">Tên đăng nhập</label><input id="username" name="username"
                    type="text" autocomplete="username" required></div>
            <button class="btn btn-block" type="submit">Gửi OTP</button>
        </form>
        <p class="auth-footer"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
    </main>
</body>

</html>
