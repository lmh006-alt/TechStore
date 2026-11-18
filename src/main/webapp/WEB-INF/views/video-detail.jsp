<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${video.title}"/> | TechStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="site-page">
    <header class="site-header">
        <div class="header-inner">
            <a class="logo" href="${pageContext.request.contextPath}/home">Tech<span>Store</span></a>
            <div class="header-account">
                <span>Xin chào, <strong><c:out value="${sessionScope.account}"/></strong></span>
                <form action="${pageContext.request.contextPath}/logout" method="post">
                    <button class="btn btn-outline" type="submit">Đăng xuất</button>
                </form>
            </div>
        </div>
    </header>
    <nav class="navbar" aria-label="Điều hướng chính">
        <div class="nav-inner">
            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
            <a href="${pageContext.request.contextPath}/categories">Quản lý danh mục</a>
            <a href="${pageContext.request.contextPath}/home#reviews">Video đánh giá</a>
            <a href="${pageContext.request.contextPath}/video">Video của tôi</a>
        </div>
    </nav>
    <main class="page-container detail-container">
        <div class="toolbar">
            <a class="btn btn-outline" href="${pageContext.request.contextPath}/home">&larr; Về trang chủ</a>
            <a class="btn btn-outline" href="${pageContext.request.contextPath}/video">Video của tôi</a>
        </div>
        <article class="video-detail panel">
            <h1><c:out value="${video.title}"/></h1>
            <p class="muted">Ngày đăng <c:out value="${video.createdDate}"/></p>
            <video controls preload="metadata">
                <source src="${pageContext.request.contextPath}/video-stream?id=${video.id}" type="video/mp4">
                Trình duyệt không hỗ trợ phát video.
            </video>
            <section class="video-description">
                <h2>Mô tả</h2>
                <p><c:out value="${video.description}"/></p>
            </section>
        </article>
    </main>
    <footer class="site-footer">
        <strong>TechStore &copy; 2026</strong>
        <p>Khám phá công nghệ cùng cộng đồng.</p>
    </footer>
</body>
</html>
