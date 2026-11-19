<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title><c:out value="${video.title}"/> | TechStore</title>
</head>
<body>
    <main class="page-container detail-container">
        <div class="toolbar">
            <a class="btn btn-outline" href="${pageContext.request.contextPath}/home">&larr; Về trang chủ</a>
            <a class="btn btn-outline" href="${pageContext.request.contextPath}/video">Video của tôi</a>
        </div>
        <article class="video-detail panel card">
            <h1><c:out value="${video.title}"/></h1>
            <p class="muted">Ngày đăng <c:out value="${video.createdDate}"/></p>
            <div class="ratio ratio-16x9">
                <video controls preload="metadata">
                    <source src="${pageContext.request.contextPath}/video-stream?id=${video.id}" type="video/mp4">
                    Trình duyệt không hỗ trợ phát video.
                </video>
            </div>
            <section class="video-description">
                <h2>Mô tả</h2>
                <p><c:out value="${video.description}"/></p>
            </section>
        </article>
    </main>
</body>
</html>
