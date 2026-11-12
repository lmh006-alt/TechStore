package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");

        if (username == null || username.isBlank() || password == null || password.isBlank() || email == null || email.isBlank()) {
            req.setAttribute("alert", "Vui long nhap du thong tin");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        username = username.trim();
        email = email.trim();

        if (password.length() < 8) {
            req.setAttribute("alert", "Mat khau phai co it nhat 8 ky tu");
        } else if (service.checkExistUsername(username)) {
            req.setAttribute("alert", "Tai khoan da ton tai");
        } else if (service.checkExistEmail(email)) {
            req.setAttribute("alert", "Email da ton tai");
        } else {
            boolean success = service.register(username, password, email, fullname, phone);

            if (success) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            req.setAttribute("alert", "Dang ky khong thanh cong");
        }

        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }
}
