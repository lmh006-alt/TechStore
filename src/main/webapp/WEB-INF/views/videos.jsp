<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Video của tôi | TechStore</title>
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
            <a class="active" aria-current="page" href="${pageContext.request.contextPath}/video">Video của tôi</a>
        </div>
    </nav>
    <main class="page-container">

        <div class="page-heading">
            <h1>Video của tôi</h1>
            <a class="btn" href="${pageContext.request.contextPath}/video?action=add">+ Đăng video</a>
        </div>

        <c:if test="${empty videos}">
            <div class="empty-state">
                <h2>Bạn chưa đăng video nào</h2>
                <p>Đăng video đầu tiên để chia sẻ trải nghiệm công nghệ của bạn.</p>
            </div>
        </c:if>

        <div class="video-grid">
            <c:forEach var="video" items="${videos}">

                <article class="video-card">

                    <video preload="metadata" controls>
                        <source 
                            src="${pageContext.request.contextPath}/video-stream?id=${video.id}"
                            type="video/mp4">
                    </video>

                    <div class="video-info">
                        <h2>
                            <a href="${pageContext.request.contextPath}/video?action=detail&id=${video.id}">
                                <c:out value="${video.title}"/>
                            </a>
                        </h2>

                        <p><c:out value="${video.description}"/></p>

                        <div class="video-actions">

                            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/video?action=edit&id=${video.id}">
                                Sửa
                            </a>

                            <form action="${pageContext.request.contextPath}/video"
                                  method="post"
                                  onsubmit="return confirm('Bạn có chắc muốn xóa video này?');">

                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${video.id}">

                                <button class="btn btn-danger" type="submit">
                                    Xóa
                                </button>

                            </form>

                        </div>
                    </div>
                </article>

            </c:forEach>
        </div>

        <c:if test="${totalPages > 1}">
            <nav class="pagination" aria-label="Phân trang video">

            <c:forEach var="i" begin="1" end="${totalPages}">

                <a class="${i == currentPage ? 'active' : ''}"
                   aria-current="${i == currentPage ? 'page' : 'false'}"
                   href="${pageContext.request.contextPath}/video?page=${i}">
                    ${i}
                </a>

            </c:forEach>

            </nav>
        </c:if>
    </main>
    <footer class="site-footer">
        <strong>TechStore &copy; 2026</strong>
        <p>Khám phá công nghệ cùng cộng đồng.</p>
    </footer>
</body>
</html>
