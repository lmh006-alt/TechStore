<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <title>Kích hoạt tài khoản | TechStore</title>
</head>

<body>
    <main class="auth-box card">
        <a class="logo" href="${pageContext.request.contextPath}/home">Tech<span>Store</span></a>
        <h1>Kích hoạt tài khoản</h1>
        <p class="subtitle">Nhập mã OTP gồm 6 chữ số đã gửi đến email</p>
        <div class="error" role="alert" ${empty alert ? 'hidden' : ''}>${fn:escapeXml(alert)}</div>
        <form data-validate action="${pageContext.request.contextPath}/verify-otp" method="post">
            <div class="field"><label class="form-label" for="username">Tên đăng nhập</label><input class="form-control" id="username" maxlength="255" data-not-blank name="username"
                    type="text" value="${fn:escapeXml(param.username)}" required></div>
            <div class="field"><label class="form-label" for="otp">Mã OTP</label><input class="form-control" id="otp" name="otp" type="text"
                    maxlength="6" pattern="[0-9]{6}" inputmode="numeric" autocomplete="one-time-code" required>
            </div>
            <button class="btn btn-primary w-100" type="submit">Xác nhận OTP</button>
        </form>
        <p class="auth-footer"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
    </main>
</body>

</html>
