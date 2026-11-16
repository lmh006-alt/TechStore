
package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;

@WebServlet("/forgot-password")
public class ForgotPasswordController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/forgot-password.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");

        if (username == null || username.isBlank()) {
            request.setAttribute("error", "Vui lòng nhập tên đăng nhập");
        } else if (userService.sendResetOTP(username.trim())) {
            response.sendRedirect(request.getContextPath()
                    + "/reset-password?username="
                    + java.net.URLEncoder.encode(
                            username.trim(), "UTF-8"));
            return;
        } else {
            request.setAttribute("error",
                    "Không thể gửi OTP. Vui lòng kiểm tra thông tin.");
        }

        doGet(request, response);
    }
}
