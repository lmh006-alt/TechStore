package vn.iotstar.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;

@WebServlet("/video")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 100,
    maxRequestSize = 1024 * 1024 * 110
)
public class VideoController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final VideoService service = new VideoServiceImpl();

    private static final int PAGE_SIZE = 6;

    private static final Path UPLOAD_DIR = Path.of(
        System.getProperty("user.home"),
        "WebAssignmentJPA", "videos"
    );

    private Integer getUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        return session == null ? null
                : (Integer) session.getAttribute("userId");
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        Integer userId = getUserId(req);

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/logout");
            return;
        }

        String action = req.getParameter("action");

        if ("detail".equals(action) || "edit".equals(action)) {

            Video video = service.findById(
                Integer.parseInt(req.getParameter("id"))
            );

            if (video == null) {
                resp.sendError(404);
                return;
            }

            if ("edit".equals(action) && video.getUserId() != userId) {
                resp.sendError(403);
                return;
            }

            req.setAttribute("video", video);

            String view = "detail".equals(action)
                    ? "video-detail.jsp"
                    : "video-form.jsp";

            req.getRequestDispatcher("/WEB-INF/views/" + view)
               .forward(req, resp);

        } else if ("add".equals(action)) {

            req.getRequestDispatcher("/WEB-INF/views/video-form.jsp")
               .forward(req, resp);

        } else {

            int page = 1;

            try {
                page = Integer.parseInt(req.getParameter("page"));
            } catch (NumberFormatException ignored) {
            }

            long total = service.countByUserId(userId);

            int totalPages = Math.max(1,
                (int) Math.ceil((double) total / PAGE_SIZE));

            page = Math.max(1, Math.min(page, totalPages));

            req.setAttribute("videos",
                service.findByUserId(userId, page, PAGE_SIZE));

            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);

            req.getRequestDispatcher("/WEB-INF/views/videos.jsp")
               .forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        Integer userId = getUserId(req);

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/logout");
            return;
        }

        String action = req.getParameter("action");

        if ("delete".equals(action)) {

            Video video = service.findById(
                Integer.parseInt(req.getParameter("id"))
            );

            if (video == null || video.getUserId() != userId) {
                resp.sendError(403);
                return;
            }

            service.delete(video.getId());

            Files.deleteIfExists(
                UPLOAD_DIR.resolve(video.getFilePath())
            );

        } else if ("add".equals(action) || "edit".equals(action)) {

            boolean editing = "edit".equals(action);

            Video video;

            if (editing) {

                video = service.findById(
                    Integer.parseInt(req.getParameter("id"))
                );

                if (video == null || video.getUserId() != userId) {
                    resp.sendError(403);
                    return;
                }

            } else {
                video = new Video();
                video.setUserId(userId);
            }

            String title = req.getParameter("title");

            if (title == null || title.isBlank() || title.length() > 255) {
                resp.sendError(400, "Tieu de khong hop le");
                return;
            }

            video.setTitle(title.trim());
            video.setDescription(req.getParameter("description"));

            Part filePart = req.getPart("videoFile");

            String oldFile = video.getFilePath();
            String newFile = null;

            if (filePart != null && filePart.getSize() > 0) {

                String originalName = filePart.getSubmittedFileName();

                if (originalName == null ||
                    !originalName.toLowerCase(java.util.Locale.ROOT)
                                 .endsWith(".mp4")) {

                    resp.sendError(400, "Chi chap nhan file MP4");
                    return;
                }

                Files.createDirectories(UPLOAD_DIR);

                newFile = UUID.randomUUID() + ".mp4";

                try (var input = filePart.getInputStream()) {
                    Files.copy(input, UPLOAD_DIR.resolve(newFile));
                }

                video.setFilePath(newFile);

            } else if (!editing) {

                resp.sendError(400, "Chua chon video");
                return;
            }

            try {
                if (editing) {
                    service.update(video);
                } else {
                    service.insert(video);
                }
            } catch (RuntimeException e) {

                if (newFile != null) {
                    Files.deleteIfExists(UPLOAD_DIR.resolve(newFile));
                }

                throw e;
            }

            if (newFile != null && oldFile != null) {
                Files.deleteIfExists(UPLOAD_DIR.resolve(oldFile));
            }

        } else {
            resp.sendError(400);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/video");
    }
}