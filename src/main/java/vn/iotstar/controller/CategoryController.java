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
        List<Category> categories;

        if (keyword != null && !keyword.isBlank()) {
            categories = service.search(keyword);
        } else {
            categories = service.getAll();
        }

        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/WEB-INF/views/categories.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

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
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID");
                return;
            }

            existing = service.get(id);

            if (existing == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        if ("delete".equals(action)) {
            service.delete(id);
            resp.sendRedirect(req.getContextPath() + "/categories");
            return;
        }

        String name = req.getParameter("name");

        if (name == null || name.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category name");
            return;
        }

        String contentType = req.getContentType();

        if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("multipart/form-data")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid form type");
            return;
        }

        String icon = existing == null ? null : existing.getIcon();
        Part image = req.getPart("image");

        if (image != null && image.getSize() > 0) {
            String type = image.getContentType();
            String extension;
            String format;

            if ("image/png".equals(type)) {
                extension = ".png";
                format = "png";
            } else if ("image/jpeg".equals(type)) {
                extension = ".jpg";
                format = "jpg";
            } else if ("image/gif".equals(type)) {
                extension = ".gif";
                format = "gif";
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only PNG, JPG and GIF are allowed");
                return;
            }

            BufferedImage decoded;

            try (InputStream input = image.getInputStream()) {
                decoded = ImageIO.read(input);
            }

            if (decoded == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid image");
                return;
            }

            Path folder = Path.of(System.getProperty("user.home"), "WebAssignmentUploads");

            Files.createDirectories(folder);

            String filename = UUID.randomUUID() + extension;
            Path destination = folder.resolve(filename);

            if (!ImageIO.write(decoded, format, destination.toFile())) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cannot save image");
                return;
            }

            icon = filename;
        }

        Category category = new Category(id, name.trim(), icon);

        if ("add".equals(action)) {
            service.insert(category);
        } else {
            service.edit(category);
        }

        resp.sendRedirect(req.getContextPath() + "/categories");
    }
}
