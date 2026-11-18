<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập | TechStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body class="auth-page">
    <main class="auth-box">
        <a class="logo" href="${pageContext.request.contextPath}/home">Tech<span>Store</span></a>
        <h1>Đăng nhập</h1>
        <p class="subtitle">Chào mừng bạn quay trở lại</p>
        <div class="error" role="alert" ${empty alert ? 'hidden' : ''}>
            ${fn:escapeXml(alert)}
        </div>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="field">
                <label for="username">Tên đăng nhập</label>
                <input id="username" type="text" name="username" value="${fn:escapeXml(rememberedUsername)}"
                    placeholder="Nhập tên đăng nhập" autocomplete="username" required>
            </div>
            <div class="field">
                <label for="password">Mật khẩu</label>
                <input id="password" type="password" name="password" placeholder="Nhập mật khẩu"
                    autocomplete="current-password" required>
            </div>
            <div class="options">
                <label class="remember">
                    <input type="checkbox" name="remember"> Ghi nhớ tài khoản
                </label>
                <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
            </div>
            <button class="btn btn-block" type="submit">Đăng nhập</button>
        </form>
        <p class="auth-footer">Chưa có tài khoản?
            <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
        </p>
    </main>
</body>

</html>
