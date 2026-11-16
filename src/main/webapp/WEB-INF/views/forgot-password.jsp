<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
		<!DOCTYPE html>
		<html lang="vi">

		<head>
			<meta charset="UTF-8">
			<meta name="viewport" content="width=device-width, initial-scale=1.0">
			<title>Quên mật khẩu</title>
			<link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
		</head>

		<body>
			<main class="auth-box">
				<h2>Quên mật khẩu</h2>
				<p class="subtitle">Nhập tên đăng nhập để nhận mã OTP qua email</p>
				<div class="error" style="${empty error ? 'display:none' : ''}">${fn:escapeXml(error)}</div>
				<form action="${pageContext.request.contextPath}/forgot-password" method="post">
					<div class="field"><label for="username">Tên đăng nhập</label><input id="username" name="username"
							type="text" autocomplete="username" required></div>
					<button type="submit">Gửi OTP</button>
				</form>
				<p class="footer"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
			</main>
		</body>

		</html>