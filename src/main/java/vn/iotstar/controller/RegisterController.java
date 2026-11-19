package vn.iotstar.controller;

import java.io.IOException;
import java.util.regex.Pattern;

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
    private static final Pattern EMAIL = Pattern.compile(
            "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?"
            + "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*");
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
            req.setAttribute("alert", "Vui lòng nhập tên đăng nhập, mật khẩu và email.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        username = username.trim();
        email = email.trim();

        if (username.length() > 255 || email.length() > 255
                || (fullname != null && fullname.length() > 255)) {
            req.setAttribute("alert", "Tên đăng nhập, email và họ tên tối đa 255 ký tự mỗi trường.");
        } else if (!EMAIL.matcher(email).matches()) {
            req.setAttribute("alert", "Địa chỉ email không đúng định dạng.");
        } else if (phone != null && (phone.length() > 20 || (!phone.isBlank()
                && (!phone.matches("\\+?[0-9][0-9 .()-]*")
                    || !phone.replaceAll("[^0-9]", "").matches("[0-9]{7,15}"))))) {
            req.setAttribute("alert", "Số điện thoại cần có 7–15 chữ số và tối đa 20 ký tự.");
        } else if (password.length() < 8) {
            req.setAttribute("alert", "Mật khẩu phải có ít nhất 8 ký tự.");
        } else if (service.checkExistUsername(username)) {
            req.setAttribute("alert", "Tên đăng nhập đã tồn tại.");
        } else if (service.checkExistEmail(email)) {
            req.setAttribute("alert", "Email đã được sử dụng.");
        } else {
            boolean success = service.register(username, password, email, fullname, phone);

            if (success) {
                resp.sendRedirect(req.getContextPath()
                        + "/verify-otp?username="
                        + java.net.URLEncoder.encode(
                                username,
                                java.nio.charset.StandardCharsets.UTF_8));
                return;
            }

            req.setAttribute("alert", "Không thể hoàn tất đăng ký. Vui lòng thử lại.");
        }

        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }
}
