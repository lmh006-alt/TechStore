<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <title>Đăng ký tài khoản | TechStore</title>
</head>

<body>
    <main class="auth-box card">
        <a class="logo" href="${pageContext.request.contextPath}/home">Tech<span>Store</span></a>
        <h1>Đăng ký tài khoản</h1>
        <p class="subtitle">Tạo tài khoản mới để bắt đầu</p>
        <div class="error" role="alert" ${empty alert ? 'hidden' : ''}>${fn:escapeXml(alert)}</div>
        <form data-validate action="${pageContext.request.contextPath}/register" method="post">
            <div class="field"><label class="form-label" for="username">Tên đăng nhập</label><input class="form-control" id="username" maxlength="255" data-not-blank name="username" value="${fn:escapeXml(param.username)}"
                    type="text" autocomplete="username" required></div>
            <div class="field"><label class="form-label" for="password">Mật khẩu</label><input class="form-control" id="password" name="password" data-not-blank
                    type="password" minlength="8" autocomplete="new-password" required></div>
            <div class="field"><label class="form-label" for="email">Email</label><input class="form-control" id="email" name="email" type="email" maxlength="255" value="${fn:escapeXml(param.email)}"
                    autocomplete="email" required></div>
            <div class="field"><label class="form-label" for="fullname">Họ tên</label><input class="form-control" id="fullname" name="fullname" maxlength="255" value="${fn:escapeXml(param.fullname)}"
                    type="text" autocomplete="name"></div>
            <div class="field"><label class="form-label" for="phone">Số điện thoại</label><input class="form-control" id="phone" name="phone" maxlength="20" data-phone value="${fn:escapeXml(param.phone)}"
                    type="tel" autocomplete="tel"></div>
            <button class="btn btn-primary w-100" type="submit">Đăng ký</button>
        </form>
        <p class="auth-footer">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
    </main>
</body>

</html>
