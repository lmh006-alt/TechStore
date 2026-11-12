<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>CRUD Category</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 35px;
        }
        table {
            border-collapse: collapse;
            width: 90%;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }
        th {
            background: #eee;
        }
        input {
            padding: 7px;
            margin: 4px;
        }
        button {
            padding: 7px 12px;
            cursor: pointer;
        }
        img {
            object-fit: contain;
        }
    </style>
</head>
<body>
    <h2>Quản lý danh mục - CRUD Category</h2>
    <h3>Thêm danh mục</h3>
    <form action="${pageContext.request.contextPath}/categories" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="add">
        <input type="text" name="name" placeholder="Tên danh mục" required>
        <input type="file" name="image" accept="image/png,image/jpeg,image/gif">
        <button type="submit">Thêm</button>
    </form>
    <hr>
    <h3>Tìm kiếm danh mục</h3>
    <form action="${pageContext.request.contextPath}/categories" method="get">
        <input type="text" name="keyword" placeholder="Nhập từ khóa">
        <button type="submit">Tìm kiếm</button>
        <a href="${pageContext.request.contextPath}/categories">
            Xem tất cả
        </a>
    </form>
    <h3>Danh sách danh mục</h3>
    <table>
        <tr>
            <th>ID</th>
            <th>Tên danh mục</th>
            <th>Icon</th>
            <th>Thao tác</th>
        </tr>
        <c:forEach var="cate" items="${categories}">
            <tr>
                <td>${cate.id}</td>
                <td>
                    <c:out value="${cate.name}"/>
                </td>
                <td>
                    <c:if test="${not empty cate.icon}">
                        <c:url var="imageUrl" value="/category-image">
                            <c:param name="file" value="${cate.icon}"/>
                        </c:url>
                        <img src="${imageUrl}" width="60" height="60" alt="Icon" onerror="this.style.display='none'">
                        <div>
                            <c:out value="${cate.icon}"/>
                        </div>
                    </c:if>
                </td>
                <td>
                    <button type="button" onclick="editCategory(this)" data-id="${cate.id}" data-name="<c:out value='${cate.name}'/>">
                        Sửa
                    </button>
                    <form action="${pageContext.request.contextPath}/categories" method="post" style="display:inline" onsubmit="return confirm('Xóa danh mục này?')">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${cate.id}">
                        <button type="submit">Xóa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <hr>
    <h3>Sửa danh mục</h3>
    <form action="${pageContext.request.contextPath}/categories" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="edit">
        <input type="number" name="id" id="editId" placeholder="ID" readonly required>
        <input type="text" name="name" id="editName" placeholder="Tên mới" required>
        <label for="editImage">Thay ảnh (không bắt buộc):</label>
        <input type="file" name="image" id="editImage" accept="image/png,image/jpeg,image/gif">
        <button type="submit">Lưu thay đổi</button>
    </form>
    <p>
        <a href="${pageContext.request.contextPath}/home">
            Về trang chủ
        </a>
    </p>
    <script>
        function editCategory(button) {
            document.getElementById("editId").value = button.dataset.id;
            document.getElementById("editName").value = button.dataset.name;
            document.getElementById("editImage").value = "";
        }
    </script>
</body>
</html>
