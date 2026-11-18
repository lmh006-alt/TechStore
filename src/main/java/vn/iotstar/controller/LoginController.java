package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("account") != null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberedUsername".equals(cookie.getName())) {
                    req.setAttribute("rememberedUsername", cookie.getValue());
                }
            }
        }

        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("alert", "Tai khoan va mat khau khong duoc rong");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        User user = service.login(username.trim(), password);

        if (user == null) {
            req.setAttribute("alert", "Tai khoan hoac mat khau khong dung");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        HttpSession oldSession = req.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("account", user.getUserName());
        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(30 * 60);

        Cookie cookie = new Cookie("rememberedUsername", "on".equals(remember) ? user.getUserName() : "");
        cookie.setHttpOnly(true);
        cookie.setSecure(req.isSecure());
        cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        cookie.setMaxAge("on".equals(remember) ? 7 * 24 * 60 * 60 : 0);
        resp.addCookie(cookie);

        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
