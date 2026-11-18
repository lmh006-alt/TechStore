package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;

@WebServlet("/home")
public class HomeController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final VideoService videoService = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("account") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setAttribute("username", session.getAttribute("account"));

        req.setAttribute("videos", videoService.findLatest(10));

        req.getRequestDispatcher("/WEB-INF/views/home.jsp")
           .forward(req, resp);
    }
}