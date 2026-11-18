<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý danh mục | TechStore</title>
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
            <a class="active" aria-current="page" href="${pageContext.request.contextPath}/categories">Quản lý danh mục</a>
            <a href="${pageContext.request.contextPath}/home#reviews">Video đánh giá</a>
            <a href="${pageContext.request.contextPath}/video">Video của tôi</a>
        </div>
    </nav>
    <main class="page-container">
        <div class="page-heading"><h1>Quản lý danh mục</h1></div>

        <div class="category-forms">
            <section class="panel" aria-labelledby="add-heading">
                <h2 id="add-heading">Thêm danh mục</h2>
                <form action="${pageContext.request.contextPath}/categories" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="add">
                    <div class="field">
                        <label for="categoryName">Tên danh mục</label>
                        <input type="text" name="name" id="categoryName" required>
                    </div>
                    <div class="field">
                        <label for="categoryImage">Ảnh danh mục (không bắt buộc)</label>
                        <input type="file" name="image" id="categoryImage" accept="image/png,image/jpeg,image/gif">
                    </div>
                    <button class="btn" type="submit">Thêm danh mục</button>
                </form>
            </section>
            <section class="panel" aria-labelledby="edit-heading">
                <h2 id="edit-heading">Sửa danh mục</h2>
                <form action="${pageContext.request.contextPath}/categories" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="edit">
                    <div class="field">
                        <label for="editId">Mã danh mục</label>
                        <input type="number" name="id" id="editId" placeholder="Chọn nút Sửa trong bảng" readonly required>
                    </div>
                    <div class="field">
                        <label for="editName">Tên mới</label>
                        <input type="text" name="name" id="editName" required>
                    </div>
                    <div class="field">
                        <label for="editImage">Ảnh thay thế (không bắt buộc)</label>
                        <input type="file" name="image" id="editImage" accept="image/png,image/jpeg,image/gif">
                    </div>
                    <button class="btn btn-secondary" type="submit">Lưu thay đổi</button>
                </form>
            </section>
        </div>

        <section class="panel" aria-labelledby="category-list">
            <h2 id="category-list">Danh sách danh mục</h2>
            <form class="category-filter" action="${pageContext.request.contextPath}/categories" method="get">
                <div class="field">
                    <label for="categoryKeyword">Tìm kiếm danh mục</label>
                    <input type="text" name="keyword" id="categoryKeyword" placeholder="Nhập từ khóa">
                </div>
                <button class="btn btn-secondary" type="submit">Tìm kiếm</button>
                <a class="btn btn-outline" href="${pageContext.request.contextPath}/categories">Xem tất cả</a>
            </form>
            <c:choose>
                <c:when test="${empty categories}">
                    <div class="empty-state"><p>Không có danh mục để hiển thị.</p></div>
                </c:when>
                <c:otherwise>
                    <div class="table-wrapper" tabindex="0" role="region" aria-label="Bảng danh mục">
                        <table class="category-table">
                            <thead>
                                <tr>
                                    <th scope="col">STT</th>
                                    <th scope="col">Tên danh mục</th>
                                    <th scope="col">Ảnh</th>
                                    <th scope="col">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="cate" items="${categories}" varStatus="status">
                                    <tr>
                                        <td>${status.count}</td>
                                        <td><c:out value="${cate.name}"/></td>
                                        <td>
                                            <c:if test="${not empty cate.icon}">
                                                <c:url var="imageUrl" value="/category-image">
                                                    <c:param name="file" value="${cate.icon}"/>
                                                </c:url>
                                                <img class="category-image" src="${imageUrl}" width="48" height="48"
                                                     alt="Ảnh danh mục" onerror="this.hidden = true">
                                                <span class="muted"><c:out value="${cate.icon}"/></span>
                                            </c:if>
                                        </td>
                                        <td>
                                            <div class="table-actions">
                                                <button class="btn btn-secondary" type="button" onclick="editCategory(this)"
                                                        data-id="${cate.id}" data-name="<c:out value='${cate.name}'/>">Sửa</button>
                                                <form action="${pageContext.request.contextPath}/categories" method="post"
                                                      onsubmit="return confirm('Xóa danh mục này?')">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="id" value="${cate.id}">
                                                    <button class="btn btn-danger" type="submit">Xóa</button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </main>
    <footer class="site-footer">
        <strong>TechStore &copy; 2026</strong>
        <p>Khám phá công nghệ cùng cộng đồng.</p>
    </footer>
    <script>
        function editCategory(button) {
            document.getElementById("editId").value = button.dataset.id;
            const nameInput = document.getElementById("editName");
            nameInput.value = button.dataset.name;
            document.getElementById("editImage").value = "";
            nameInput.focus();
        }
    </script>
</body>
</html>
