<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
		<!DOCTYPE html>
		<html lang="vi">

		<head>
			<meta charset="UTF-8">
			<meta name="viewport" content="width=device-width, initial-scale=1.0">
			<title>Đăng nhập</title>
			<link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
		</head>

		<body>
			<main class="auth-box">
				<h2>Đăng nhập</h2>
				<p class="subtitle">Chào mừng bạn quay trở lại</p>
				<div class="error" style="${empty alert ? 'display:none' : ''}">${fn:escapeXml(alert)}</div>
				<form action="${pageContext.request.contextPath}/login" method="post">
					<div class="field">
						<label for="username">Tài khoản</label>
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
					<button type="submit">Đăng nhập</button>
				</form>
				<p class="footer">Chưa có tài khoản?
					<a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
				</p>
			</main>
		</body>

		</html>