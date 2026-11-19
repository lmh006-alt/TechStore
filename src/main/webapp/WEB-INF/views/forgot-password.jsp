<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <title>Quên mật khẩu | TechStore</title>
</head>

<body>
    <main class="auth-box card">
        <a class="logo" href="${pageContext.request.contextPath}/home">Tech<span>Store</span></a>
        <h1>Quên mật khẩu</h1>
        <p class="subtitle">Nhập tên đăng nhập để nhận mã OTP qua email</p>
        <div class="error" role="alert" ${empty error ? 'hidden' : ''}>${fn:escapeXml(error)}</div>
        <form data-validate action="${pageContext.request.contextPath}/forgot-password" method="post">
            <div class="field"><label class="form-label" for="username">Tên đăng nhập</label><input class="form-control" id="username" maxlength="255" data-not-blank name="username" value="${fn:escapeXml(param.username)}"
                    type="text" autocomplete="username" required></div>
            <button class="btn btn-primary w-100" type="submit">Gửi OTP</button>
        </form>
        <p class="auth-footer"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
    </main>
</body>

</html>
