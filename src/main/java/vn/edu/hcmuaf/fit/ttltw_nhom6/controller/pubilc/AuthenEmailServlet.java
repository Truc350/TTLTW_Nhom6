package vn.edu.hcmuaf.fit.ttltw_nhom6.controller.pubilc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.ltw_nhom5.dao.UserDao;
import vn.edu.hcmuaf.fit.ltw_nhom5.db.JdbiConnector;
import vn.edu.hcmuaf.fit.ltw_nhom5.model.User;
import vn.edu.hcmuaf.fit.ltw_nhom5.utils.EmailUtils;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "AuthenEmailServlet", value = "/AuthenEmailServlet")
public class AuthenEmailServlet extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao(JdbiConnector.get());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        Optional<User> userOpt = userDao.findByEmail(email);
        if (userOpt.isEmpty()) {
            request.setAttribute("error", "Email không tồn tại trong hệ thống!");
            request.getRequestDispatcher("/fontend/public/ForgotPass.jsp").forward(request, response);
            return;
        }

        // Sinh mã OTP
        String code = String.valueOf((int)(Math.random() * 900000) + 100000);

        // Lưu OTP vào session
        request.getSession().setAttribute("otp", code);
        request.getSession().setAttribute("otpEmail", email);
        request.getSession().setAttribute("otpSent", true); // DÙNG SESSION thay vì request

        // Gửi email
        EmailUtils.sendEmail(email, "Mã xác thực quên mật khẩu", "Mã OTP của bạn là: " + code);

        // Quay lại ForgotPass.jsp và bật popup
        request.getRequestDispatcher("/fontend/public/ForgotPass.jsp").forward(request, response);
    }
}