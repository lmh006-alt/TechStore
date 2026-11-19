<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <title>Đăng nhập | TechStore</title>
</head>

<body>
    <main class="auth-box card">
        <a class="logo" href="${pageContext.request.contextPath}/home">Tech<span>Store</span></a>
        <h1>Đăng nhập</h1>
        <p class="subtitle">Chào mừng bạn quay trở lại</p>
        <div class="error" role="alert" ${empty alert ? 'hidden' : ''}>
            ${fn:escapeXml(alert)}
        </div>
        <form data-validate action="${pageContext.request.contextPath}/login" method="post">
            <div class="field">
                <label class="form-label" for="username">Tên đăng nhập</label>
                <input class="form-control" id="username" maxlength="255" data-not-blank type="text" name="username" value="${fn:escapeXml(rememberedUsername)}"
                    placeholder="Nhập tên đăng nhập" autocomplete="username" required>
            </div>
            <div class="field">
                <label class="form-label" for="password">Mật khẩu</label>
                <input class="form-control" id="password" type="password" name="password" data-not-blank placeholder="Nhập mật khẩu"
                    autocomplete="current-password" required>
            </div>
            <div class="options">
                <label class="remember">
                    <input type="checkbox" name="remember" ${rememberSelected ? 'checked' : ''}> Ghi nhớ tài khoản
                </label>
                <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
            </div>
            <button class="btn btn-primary w-100" type="submit">Đăng nhập</button>
        </form>
        <p class="auth-footer">Chưa có tài khoản?
            <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
        </p>
    </main>
</body>

</html>
