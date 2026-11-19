package vn.iotstar.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
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
    private static final int DESCRIPTION_MAX_BYTES = 65_535;

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

            int id = readVideoId(req);
            if (id <= 0) {
                resp.sendError(400, "Mã video không hợp lệ.");
                return;
            }
            Video video = service.findById(id);

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

        String contentType = req.getContentType();
        boolean multipart = contentType != null
                && contentType.toLowerCase(Locale.ROOT).startsWith("multipart/form-data");

        if (multipart) {
            try {
                req.getParts();
            } catch (IllegalStateException e) {
                req.setAttribute("uploadRejected", true);
                showFormError(req, resp, "Video tối đa 100 MB. Hãy mở lại biểu mẫu và chọn tệp nhỏ hơn.");
                return;
            } catch (ServletException | IOException e) {
                req.setAttribute("uploadRejected", true);
                showFormError(req, resp, "Không thể đọc biểu mẫu tải video. Hãy mở lại biểu mẫu và thử lại.");
                return;
            }
        }

        String action = req.getParameter("action");

        if ("delete".equals(action)) {

            int id = readVideoId(req);
            if (id <= 0) {
                resp.sendError(400, "Mã video không hợp lệ.");
                return;
            }
            Video video = service.findById(id);

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

                int id = readVideoId(req);
                if (id <= 0) {
                    resp.sendError(400, "Mã video không hợp lệ.");
                    return;
                }
                video = service.findById(id);

                if (video == null || video.getUserId() != userId) {
                    resp.sendError(403);
                    return;
                }

            } else {
                video = new Video();
                video.setUserId(userId);
            }

            String title = req.getParameter("title");
            String description = req.getParameter("description");
            req.setAttribute("submittedVideo", true);
            req.setAttribute("submittedTitle", title);
            req.setAttribute("submittedDescription", description);
            if (editing) {
                req.setAttribute("video", video);
            }

            if (title == null || title.isBlank() || title.length() > 255) {
                showFormError(req, resp, "Tiêu đề là bắt buộc và tối đa 255 ký tự.");
                return;
            }
            if (description != null && description.getBytes(StandardCharsets.UTF_8).length > DESCRIPTION_MAX_BYTES) {
                showFormError(req, resp, "Mô tả tối đa 65.535 byte UTF-8.");
                return;
            }
            if (!multipart) {
                showFormError(req, resp, "Biểu mẫu tải video không hợp lệ.");
                return;
            }

            Part filePart = req.getPart("videoFile");
            String oldFile = video.getFilePath();
            String newFile = null;
            String originalName = filePart == null ? null : filePart.getSubmittedFileName();

            if (originalName != null && !originalName.isEmpty()) {
                if (!originalName.toLowerCase(Locale.ROOT).endsWith(".mp4") || !hasMp4Structure(filePart)) {
                    showFormError(req, resp, "Tệp video phải có đuôi .mp4 và nội dung MP4 hợp lệ.");
                    return;
                }

                newFile = UUID.randomUUID() + ".mp4";
                Path destination = UPLOAD_DIR.resolve(newFile);
                try (var input = filePart.getInputStream()) {
                    Files.createDirectories(UPLOAD_DIR);
                    Files.copy(input, destination);
                } catch (IOException e) {
                    removeFailedUpload(destination);
                    log("Không thể lưu tệp video.", e);
                    showFormError(req, resp, "Không thể lưu tệp video. Vui lòng thử lại.");
                    return;
                }
                video.setFilePath(newFile);
            } else if (!editing) {
                showFormError(req, resp, "Vui lòng chọn tệp video MP4.");
                return;
            }

            video.setTitle(title.trim());
            video.setDescription(description);

            try {
                if (editing) {
                    service.update(video);
                } else {
                    service.insert(video);
                }
            } catch (RuntimeException e) {

                if (newFile != null) {
                    removeFailedUpload(UPLOAD_DIR.resolve(newFile));
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

    private int readVideoId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean hasMp4Structure(Part file) {
        // Check the MP4 container boxes without decoding or buffering the media payload.
        try (var input = new DataInputStream(file.getInputStream())) {
            long remaining = file.getSize();
            boolean fileType = false;
            boolean metadata = false;
            boolean mediaData = false;

            while (remaining >= 8) {
                long size = Integer.toUnsignedLong(input.readInt());
                int type = input.readInt();
                int headerSize = 8;
                if (size == 1) {
                    size = input.readLong();
                    headerSize = 16;
                } else if (size == 0) {
                    size = remaining;
                }
                if (size < headerSize || size > remaining) {
                    return false;
                }
                long payload = size - headerSize;
                if (type == 0x66747970) { // ftyp
                    fileType = payload >= 8 && payload % 4 == 0;
                } else if (type == 0x6d6f6f76) { // moov
                    metadata = payload >= 8;
                } else if (type == 0x6d646174) { // mdat
                    mediaData = payload > 0;
                }
                if (fileType && metadata && mediaData) {
                    return true;
                }
                input.skipNBytes(payload);
                remaining -= size;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    private void removeFailedUpload(Path destination) {
        try {
            Files.deleteIfExists(destination);
        } catch (IOException e) {
            log("Không thể dọn tệp video chưa được lưu.", e);
        }
    }

    private void showFormError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("videoError", message);
        req.getRequestDispatcher("/WEB-INF/views/video-form.jsp").forward(req, resp);
    }
}
