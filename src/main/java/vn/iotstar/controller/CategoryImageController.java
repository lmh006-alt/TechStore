package vn.iotstar.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/category-image")
public class CategoryImageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String file = req.getParameter("file");

        if (file == null || !file.matches("[0-9a-fA-F-]{36}\\.(png|jpg|gif)")) {
            resp.sendError(404);
            return;
        }

        Path folder = Path.of(System.getProperty("user.home"), "WebAssignmentUploads");

        Path image = folder.resolve(file);

        if (!Files.isRegularFile(image)) {
            resp.sendError(404);
            return;
        }

        String type = Files.probeContentType(image);
        resp.setContentType(type != null ? type : "application/octet-stream");
        resp.setHeader("X-Content-Type-Options", "nosniff");
        Files.copy(image, resp.getOutputStream());
    }
}
