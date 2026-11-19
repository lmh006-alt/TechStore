<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Video của tôi | TechStore</title>
</head>
<body>
    <main class="page-container">

        <div class="page-heading">
            <h1>Video của tôi</h1>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/video?action=add">+ Đăng video</a>
        </div>

        <c:if test="${empty videos}">
            <div class="empty-state">
                <h2>Bạn chưa đăng video nào</h2>
                <p>Đăng video đầu tiên để chia sẻ trải nghiệm công nghệ của bạn.</p>
            </div>
        </c:if>

        <div class="video-grid d-grid gap-4">
            <c:forEach var="video" items="${videos}">

                <article class="video-card card">

                    <video preload="metadata" controls>
                        <source 
                            src="${pageContext.request.contextPath}/video-stream?id=${video.id}"
                            type="video/mp4">
                    </video>

                    <div class="video-info card-body">
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
</body>
</html>
