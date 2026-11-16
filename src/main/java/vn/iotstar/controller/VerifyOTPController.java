
package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;

@WebServlet("/verify-otp")
public class VerifyOTPController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher(
            "/WEB-INF/views/verify-otp.jsp"
        ).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String otp = req.getParameter("otp");

        if (username == null || username.isBlank()
                || otp == null || !otp.matches("\\d{6}")) {
            req.setAttribute("alert", "Thong tin OTP khong hop le");
        } else if (service.verifyOTP(username.trim(), otp)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        } else {
            req.setAttribute("alert", "OTP sai hoac da het han");
        }

        req.getRequestDispatcher(
            "/WEB-INF/views/verify-otp.jsp"
        ).forward(req, resp);
    }
}
