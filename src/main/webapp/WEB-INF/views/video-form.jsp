<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>${empty video ? 'Thêm video' : 'Sửa video'} | TechStore</title>
</head>
<body>
    <main class="page-container page-narrow">

        <div class="page-heading"><h1>${empty video ? 'Thêm video' : 'Sửa video'}</h1></div>

        <c:if test="${not empty videoError}">
            <div class="error" role="alert"><c:out value="${videoError}"/></div>
        </c:if>
        <c:if test="${uploadRejected}">
            <a class="btn btn-outline" href="${pageContext.request.contextPath}/video">Về Video của tôi</a>
        </c:if>

        <c:if test="${not uploadRejected}">
        <form class="video-form panel" data-validate
              action="${pageContext.request.contextPath}/video"
              method="post"
              enctype="multipart/form-data">

            <input type="hidden" name="action"
                   value="${empty video ? 'add' : 'edit'}">

            <c:if test="${not empty video}">
                <input type="hidden" name="id" value="${video.id}">
            </c:if>

            <label class="form-label" for="videoTitle">Tiêu đề video</label>

            <input class="form-control" type="text" id="videoTitle" name="title"
                   value="${fn:escapeXml(submittedVideo ? submittedTitle : video.title)}"
                   maxlength="255" data-not-blank required>

            <label class="form-label" for="videoDescription">Mô tả video</label>

            <textarea class="form-control" id="videoDescription" name="description" rows="5"
                      maxlength="65535" data-text-bytes="65535"><c:out value="${submittedVideo ? submittedDescription : video.description}"/></textarea>

            <c:if test="${not empty video}">

                <p id="current-video-label" class="field-label">Video hiện tại</p>

                <video aria-labelledby="current-video-label" controls preload="metadata">
                    <source
                        src="${pageContext.request.contextPath}/video-stream?id=${video.id}"
                        type="video/mp4">
                </video>

            </c:if>

            <label class="form-label" for="videoFile">Chọn tệp video</label>

            <c:choose>
                <c:when test="${empty video}">
                    <input class="form-control" type="file" id="videoFile" name="videoFile" aria-describedby="video-file-help"
                           accept=".mp4" data-max-bytes="104857600" data-file-message="Chỉ chấp nhận tệp MP4." required>
                </c:when>

                <c:otherwise>
                    <input class="form-control" type="file" id="videoFile" name="videoFile" aria-describedby="video-file-help"
                           accept=".mp4" data-max-bytes="104857600" data-file-message="Chỉ chấp nhận tệp MP4.">

                    <p class="muted">Để trống nếu muốn giữ video hiện tại.</p>
                </c:otherwise>
            </c:choose>

            <p id="video-file-help" class="muted">Định dạng MP4, tối đa 100 MB.</p>

            <div class="video-actions">

                <button class="btn btn-primary" type="submit">Lưu video</button>

                <a class="btn btn-outline"
                   href="${pageContext.request.contextPath}/video">
                    Hủy
                </a>

            </div>

        </form>
        </c:if>
    </main>
</body>
</html>
