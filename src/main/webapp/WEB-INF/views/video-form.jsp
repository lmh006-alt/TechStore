<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty video ? 'Thêm video' : 'Sửa video'} | TechStore</title>
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
    <main class="page-container page-narrow">

        <div class="page-heading"><h1>${empty video ? 'Thêm video' : 'Sửa video'}</h1></div>

        <form class="video-form panel"
              action="${pageContext.request.contextPath}/video"
              method="post"
              enctype="multipart/form-data">

            <input type="hidden" name="action"
                   value="${empty video ? 'add' : 'edit'}">

            <c:if test="${not empty video}">
                <input type="hidden" name="id" value="${video.id}">
            </c:if>

            <label for="videoTitle">Tiêu đề video</label>

            <input type="text" id="videoTitle" name="title"
                   value="${fn:escapeXml(video.title)}"
                   maxlength="255" required>

            <label for="videoDescription">Mô tả video</label>

            <textarea id="videoDescription" name="description" rows="5"><c:out value="${video.description}"/></textarea>

            <c:if test="${not empty video}">

                <p id="current-video-label" class="field-label">Video hiện tại</p>

                <video aria-labelledby="current-video-label" controls preload="metadata">
                    <source
                        src="${pageContext.request.contextPath}/video-stream?id=${video.id}"
                        type="video/mp4">
                </video>

            </c:if>

            <label for="videoFile">Chọn tệp video</label>

            <c:choose>
                <c:when test="${empty video}">
                    <input type="file" id="videoFile" name="videoFile" aria-describedby="video-file-help"
                           accept=".mp4" required>
                </c:when>

                <c:otherwise>
                    <input type="file" id="videoFile" name="videoFile" aria-describedby="video-file-help"
                           accept=".mp4">

                    <p class="muted">Để trống nếu muốn giữ video hiện tại.</p>
                </c:otherwise>
            </c:choose>

            <p id="video-file-help" class="muted">Định dạng MP4, tối đa 100 MB.</p>

            <div class="video-actions">

                <button class="btn" type="submit">Lưu video</button>

                <a class="btn btn-outline"
                   href="${pageContext.request.contextPath}/video">
                    Hủy
                </a>

            </div>

        </form>
    </main>
    <footer class="site-footer">
        <strong>TechStore &copy; 2026</strong>
        <p>Khám phá công nghệ cùng cộng đồng.</p>
    </footer>
</body>
</html>
