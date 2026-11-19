package vn.iotstar.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import vn.iotstar.model.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;

@WebServlet("/categories")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024, maxRequestSize = 3 * 1024 * 1024)
public class CategoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final CategoryService service = new CategoryServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("account") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        req.setAttribute("searchKeyword", keyword);
        List<Category> categories;

        if (keyword != null && keyword.length() > 255) {
            req.setAttribute("searchError", "Từ khóa tối đa 255 ký tự.");
            categories = service.getAll();
        } else if (keyword != null && !keyword.isBlank()) {
            categories = service.search(keyword);
        } else {
            categories = service.getAll();
        }

        showCategories(req, resp, categories);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String contentType = req.getContentType();
        boolean multipart = contentType != null
                && contentType.toLowerCase(Locale.ROOT).startsWith("multipart/form-data");

        if (multipart) {
            try {
                req.getParts();
            } catch (IllegalStateException e) {
                showError(req, resp, "uploadError", "Ảnh tối đa 2 MB. Vui lòng nhập lại và chọn ảnh nhỏ hơn.");
                return;
            } catch (ServletException | IOException e) {
                showError(req, resp, "uploadError", "Không thể đọc biểu mẫu tải ảnh. Vui lòng thử lại.");
                return;
            }
        }

        String action = req.getParameter("action");

        if (!"add".equals(action) && !"edit".equals(action) && !"delete".equals(action)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = 0;
        Category existing = null;

        if (!"add".equals(action)) {
            try {
                id = Integer.parseInt(req.getParameter("id"));

                if (id <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã danh mục không hợp lệ.");
                return;
            }

            existing = service.get(id);

            if (existing == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy danh mục.");
                return;
            }
        }

        if ("delete".equals(action)) {
            service.delete(id);
            resp.sendRedirect(req.getContextPath() + "/categories");
            return;
        }

        boolean editing = "edit".equals(action);
        String errorAttribute = editing ? "editError" : "addError";
        String name = req.getParameter("name");
        req.setAttribute(editing ? "editName" : "addName", name);
        if (editing) {
            req.setAttribute("editId", id);
        }

        if (name == null || name.isBlank() || name.length() > 255) {
            showError(req, resp, errorAttribute, "Tên danh mục là bắt buộc và tối đa 255 ký tự.");
            return;
        }
        if (!multipart) {
            showError(req, resp, errorAttribute, "Biểu mẫu tải ảnh không hợp lệ.");
            return;
        }

        name = name.trim();
        Category duplicate = service.get(name);
        if (duplicate != null && duplicate.getId() != id) {
            showError(req, resp, errorAttribute, "Tên danh mục đã được sử dụng.");
            return;
        }

        String icon = existing == null ? null : existing.getIcon();
        Part image = req.getPart("image");
        Path destination = null;

        if (image != null && image.getSubmittedFileName() != null && !image.getSubmittedFileName().isEmpty()) {
            String format = switch (image.getContentType() == null ? "" : image.getContentType()) {
                case "image/png" -> "png";
                case "image/jpeg" -> "jpg";
                case "image/gif" -> "gif";
                default -> "";
            };
            if (format.isEmpty()) {
                showError(req, resp, errorAttribute, "Chỉ chấp nhận ảnh PNG, JPG hoặc GIF.");
                return;
            }

            BufferedImage decoded;
            try {
                decoded = readImage(image, format);
            } catch (IOException e) {
                decoded = null;
            }
            if (decoded == null) {
                showError(req, resp, errorAttribute, "Ảnh trống, bị lỗi hoặc không đúng định dạng đã chọn.");
                return;
            }

            Path folder = Path.of(System.getProperty("user.home"), "WebAssignmentUploads");
            icon = UUID.randomUUID() + "." + format;
            destination = folder.resolve(icon);
            try {
                Files.createDirectories(folder);
                if (!ImageIO.write(decoded, format, destination.toFile())) {
                    throw new IOException("Không tìm thấy bộ ghi ảnh.");
                }
            } catch (IOException e) {
                removeFailedUpload(destination);
                log("Không thể lưu ảnh danh mục.", e);
                showError(req, resp, errorAttribute, "Không thể lưu ảnh danh mục. Vui lòng thử lại.");
                return;
            }
        }

        Category category = new Category(id, name, icon);
        try {
            if (editing) {
                service.edit(category);
            } else {
                service.insert(category);
            }
        } catch (RuntimeException e) {
            removeFailedUpload(destination);
            throw e;
        }

        resp.sendRedirect(req.getContextPath() + "/categories");
    }

    private BufferedImage readImage(Part image, String format) throws IOException {
        try (InputStream bytes = image.getInputStream();
                ImageInputStream input = ImageIO.createImageInputStream(bytes)) {
            if (input == null) {
                return null;
            }
            var readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) {
                return null;
            }
            ImageReader reader = readers.next();
            try {
                String expected = "jpg".equals(format) ? "jpeg" : format;
                if (!expected.equalsIgnoreCase(reader.getFormatName())) {
                    return null;
                }
                reader.setInput(input);
                return reader.read(0);
            } finally {
                reader.dispose();
            }
        }
    }

    private void removeFailedUpload(Path destination) {
        if (destination != null) {
            try {
                Files.deleteIfExists(destination);
            } catch (IOException e) {
                log("Không thể dọn tệp ảnh chưa được lưu.", e);
            }
        }
    }

    private void showError(HttpServletRequest req, HttpServletResponse resp, String attribute, String message)
            throws ServletException, IOException {
        req.setAttribute(attribute, message);
        showCategories(req, resp, service.getAll());
    }

    private void showCategories(HttpServletRequest req, HttpServletResponse resp, List<Category> categories)
            throws ServletException, IOException {
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/WEB-INF/views/categories.jsp").forward(req, resp);
    }
}
