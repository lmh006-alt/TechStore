<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="forwardPath" value="${requestScope['jakarta.servlet.forward.servlet_path']}"/>
<c:set var="currentPath" value="${empty forwardPath ? pageContext.request.servletPath : forwardPath}"/>
<c:set var="authPage" value="${empty currentPath or currentPath eq '/' or currentPath eq '/login'
    or currentPath eq '/register' or currentPath eq '/forgot-password' or currentPath eq '/reset-password'
    or currentPath eq '/verify-otp'}"/>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><sitemesh:write property="title"/></title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <!-- TechStore CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <sitemesh:write property="head"/>
</head>

<body class="${authPage ? 'auth-page' : 'site-page'}">

    <c:if test="${not authPage and not empty sessionScope.account}">

        <!-- HEADER -->
        <header class="site-header">
            <div class="header-inner">

                <a class="logo"
                   href="${pageContext.request.contextPath}/home">
                    Tech<span>Store</span>
                </a>

                <div class="header-account">

                    <span>
                        Xin chào,
                        <strong>
                            <c:out value="${sessionScope.account}"/>
                        </strong>
                    </span>

                    <form action="${pageContext.request.contextPath}/logout"
                          method="post">

                        <button class="btn btn-outline" type="submit">
                            Đăng xuất
                        </button>

                    </form>

                </div>
            </div>
        </header>

        <!-- NAVBAR -->
        <nav class="site-nav" aria-label="Điều hướng chính">
            <div class="nav nav-inner flex-nowrap">

                <a id="nav-home" class="nav-link${currentPath eq '/home' ? ' active' : ''}"
                   aria-current="${currentPath eq '/home' ? 'page' : 'false'}"
                   href="${pageContext.request.contextPath}/home">
                    Trang chủ
                </a>

                <a class="nav-link${currentPath eq '/categories' ? ' active' : ''}"
                   aria-current="${currentPath eq '/categories' ? 'page' : 'false'}"
                   href="${pageContext.request.contextPath}/categories">
                    Quản lý danh mục
                </a>

                <a id="nav-reviews" class="nav-link" href="${pageContext.request.contextPath}/home#reviews">
                    Video đánh giá
                </a>

                <a class="nav-link${currentPath eq '/video' ? ' active' : ''}"
                   aria-current="${currentPath eq '/video' ? 'page' : 'false'}"
                   href="${pageContext.request.contextPath}/video">
                    Video của tôi
                </a>

            </div>
        </nav>

    </c:if>

    <!-- PAGE CONTENT -->
    <sitemesh:write property="body"/>

    <!-- FOOTER -->
    <c:if test="${not authPage and not empty sessionScope.account}">
        <footer class="site-footer">
            <strong>TechStore &copy; 2026</strong>
            <p>Khám phá công nghệ cùng cộng đồng.</p>
        </footer>
    </c:if>

    <c:if test="${currentPath eq '/home' and not empty sessionScope.account}">
        <script>
            const homeLink = document.getElementById('nav-home');
            const reviewsLink = document.getElementById('nav-reviews');

            function updateHomeNavigation() {
                const showingReviews = window.location.hash === '#reviews';
                homeLink.classList.toggle('active', !showingReviews);
                reviewsLink.classList.toggle('active', showingReviews);
                homeLink.setAttribute('aria-current', showingReviews ? 'false' : 'page');
                reviewsLink.setAttribute('aria-current', showingReviews ? 'location' : 'false');
            }

            updateHomeNavigation();
            window.addEventListener('hashchange', updateHomeNavigation);
        </script>
    </c:if>

    <script src="${pageContext.request.contextPath}/js/form-validation.js" defer></script>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
