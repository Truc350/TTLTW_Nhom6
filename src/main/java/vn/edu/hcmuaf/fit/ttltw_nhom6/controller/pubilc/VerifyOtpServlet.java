package vn.edu.hcmuaf.fit.ttltw_nhom6.controller.pubilc;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "VerifyOtpServlet", value = "/VerifyOtpServlet")
public class VerifyOtpServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");
        String sessionCode = (String) request.getSession().getAttribute("otp");

        if (sessionCode != null && sessionCode.equals(code)) {
            // OTP đúng -> xóa tất cả attributes liên quan
            request.getSession().removeAttribute("otpSent");
            request.getSession().removeAttribute("otpError");

            // Chuyển sang trang tạo mật khẩu
            response.sendRedirect(request.getContextPath() + "/frontend/public/reset-password.jsp");
        } else {
            // OTP sai -> giữ otpSent và báo lỗi
            request.getSession().setAttribute("otpSent", true);
            request.getSession().setAttribute("otpError", "Mã xác thực không đúng!");

            // dùng forward để giữ otpSent
            request.getRequestDispatcher("/frontend/public/ForgotPass.jsp").forward(request, response);
        }
    }
}