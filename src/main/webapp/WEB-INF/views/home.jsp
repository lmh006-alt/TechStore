<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<!DOCTYPE html>
	<html lang="vi">

	<head>
		<meta charset="UTF-8">
		<title>Home</title>
	</head>

	<body>
		<h2>Đăng nhập thành công!</h2>
		<p>Chào mừng: ${username}</p>
		<a href="${pageContext.request.contextPath}/categories">
			Quản lý danh mục
		</a>
		<form action="${pageContext.request.contextPath}/logout" method="post">
			<button type="submit">Đăng xuất</button>
		</form>
	</body>

	</html>