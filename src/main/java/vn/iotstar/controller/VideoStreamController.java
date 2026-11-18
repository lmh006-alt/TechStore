package vn.iotstar.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;

@WebServlet("/video-stream")
public class VideoStreamController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final VideoService service = new VideoServiceImpl();

    private static final Path UPLOAD_DIR = Path.of(
        System.getProperty("user.home"),
        "WebAssignmentJPA", "videos"
    );

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.sendError(401);
            return;
        }

        int id;

        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(400);
            return;
        }

        Video video = service.findById(id);

        if (video == null) {
            resp.sendError(404);
            return;
        }

        Path file = UPLOAD_DIR.resolve(video.getFilePath()).normalize();

        if (!file.startsWith(UPLOAD_DIR) || !Files.isRegularFile(file)) {
            resp.sendError(404);
            return;
        }

        resp.setContentType("video/mp4");
        resp.setContentLengthLong(Files.size(file));

        Files.copy(file, resp.getOutputStream());
    }
}