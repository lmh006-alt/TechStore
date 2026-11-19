<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <title>TechStore - Trang chủ</title>
</head>

<body>

    <main class="page-container">

        <!-- BANNER -->
        <section class="hero">

            <div class="hero-content">

                <h1>
                    Khám phá công nghệ.<br>
                    Chọn đúng sản phẩm.
                </h1>

                <p>
                    Cập nhật xu hướng công nghệ và khám phá những
                    đánh giá sản phẩm thực tế từ cộng đồng TechStore.
                </p>

                <a class="btn btn-primary" href="#reviews">
                    Khám phá ngay &rarr;
                </a>

            </div>

            <svg class="hero-art" viewBox="0 0 24 24"
                 fill="none" stroke="currentColor"
                 stroke-width="1.5" stroke-linecap="round"
                 stroke-linejoin="round" aria-hidden="true">
                <path d="M4 17V5h16v12M2 17h20l-1 2H3z"/>
            </svg>

        </section>

        <!-- CATEGORIES -->
        <section>

            <div class="section-heading">

                <h2>Nhóm sản phẩm công nghệ</h2>

                <a href="${pageContext.request.contextPath}/categories">
                    Quản lý danh mục &rarr;
                </a>

            </div>

            <p class="subtitle">Các nhóm sản phẩm minh họa.</p>

            <div class="category-grid">

                <div class="category-card">
                    <svg class="category-icon" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor"
                         stroke-width="1.5" stroke-linecap="round"
                         stroke-linejoin="round" aria-hidden="true">
                        <path d="M4 17V5h16v12M2 17h20l-1 2H3z"/>
                    </svg>
                    Máy tính xách tay
                </div>

                <div class="category-card">
                    <svg class="category-icon" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor"
                         stroke-width="1.5" stroke-linecap="round"
                         stroke-linejoin="round" aria-hidden="true">
                        <path d="M8 2h8a2 2 0 0 1 2 2v16a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2zm3 16h2"/>
                    </svg>
                    Điện thoại
                </div>

                <div class="category-card">
                    <svg class="category-icon" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor"
                         stroke-width="1.5" stroke-linecap="round"
                         stroke-linejoin="round" aria-hidden="true">
                        <path d="M3 4h18v13H3zM12 17v4m-4 0h8"/>
                    </svg>
                    Màn hình
                </div>

                <div class="category-card">
                    <svg class="category-icon" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor"
                         stroke-width="1.5" stroke-linecap="round"
                         stroke-linejoin="round" aria-hidden="true">
                        <path d="M2 6h20v12H2zM5 10h1m3 0h1m3 0h1m3 0h1M5 14h1m3 0h6m3 0h1"/>
                    </svg>
                    Bàn phím
                </div>

                <div class="category-card">
                    <svg class="category-icon" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor"
                         stroke-width="1.5" stroke-linecap="round"
                         stroke-linejoin="round" aria-hidden="true">
                        <path d="M12 3a6 6 0 0 1 6 6v6a6 6 0 0 1-12 0V9a6 6 0 0 1 6-6zm0 4v4"/>
                    </svg>
                    Chuột
                </div>

                <div class="category-card">
                    <svg class="category-icon" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor"
                         stroke-width="1.5" stroke-linecap="round"
                         stroke-linejoin="round" aria-hidden="true">
                        <path d="M3 14v-2a9 9 0 0 1 18 0v2M3 14h4v6H5a2 2 0 0 1-2-2zm18 0h-4v6h2a2 2 0 0 0 2-2z"/>
                    </svg>
                    Tai nghe
                </div>

            </div>

        </section>

        <!-- LATEST VIDEOS -->
        <section id="reviews">

            <div class="section-heading">

                <h2>Video đánh giá mới nhất</h2>

                <a class="btn btn-primary"
                   href="${pageContext.request.contextPath}/video?action=add">
                    + Đăng video
                </a>

            </div>

            <c:if test="${empty videos}">

                <div class="empty-state">

                    <h3>Chưa có video đánh giá nào</h3>

                    <p>Hãy là người đầu tiên chia sẻ trải nghiệm công nghệ.</p>

                </div>

            </c:if>

            <div class="video-grid d-grid gap-4">

                <c:forEach var="video" items="${videos}">

                    <article class="video-card card">

                        <video controls preload="metadata">

                            <source
                                src="${pageContext.request.contextPath}/video-stream?id=${video.id}"
                                type="video/mp4">

                        </video>

                        <div class="video-info card-body">

                            <h3>
                                <a href="${pageContext.request.contextPath}/video?action=detail&id=${video.id}">
                                    <c:out value="${video.title}"/>
                                </a>
                            </h3>

                            <p>
                                <c:out value="${video.description}"/>
                            </p>

                        </div>

                    </article>

                </c:forEach>

            </div>

        </section>

    </main>

</body>

</html>
