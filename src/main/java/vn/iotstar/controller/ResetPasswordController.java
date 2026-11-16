
package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;

@WebServlet("/reset-password")
public class ResetPasswordController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/reset-password.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String otp = request.getParameter("otp");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null || username.isBlank()
                || otp == null || newPassword == null
                || confirmPassword == null) {

            request.setAttribute("error",
                    "Vui lòng nhập đầy đủ thông tin");

        } else if (!newPassword.equals(confirmPassword)) {

            request.setAttribute("error",
                    "Mật khẩu xác nhận không khớp");

        } else if (newPassword.length() < 8) {

            request.setAttribute("error",
                    "Mật khẩu phải có ít nhất 8 ký tự");

        } else if (userService.resetPassword(
                username.trim(), otp.trim(), newPassword)) {

            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;

        } else {

            request.setAttribute("error",
                    "OTP không đúng, đã hết hạn hoặc không hợp lệ");
        }

        request.setAttribute("username", username);
        doGet(request, response);
    }
}
