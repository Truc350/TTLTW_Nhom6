package vn.edu.hcmuaf.fit.ttltw_nhom6.controller.pubilc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.ttltw_nhom6.dao.UserDao;
import vn.edu.hcmuaf.fit.ttltw_nhom6.db.JdbiConnector;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.User;
import vn.edu.hcmuaf.fit.ttltw_nhom6.utils.PasswordUtils;
import vn.edu.hcmuaf.fit.ttltw_nhom6.utils.ValidationUtils;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao(JdbiConnector.get());
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher("/frontend/public/Register.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username        = request.getParameter("username");
        String email           = request.getParameter("email");
        String phone           = request.getParameter("phone");
        String password        = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        if (!ValidationUtils.isValidUsername(username)) {
            forwardWithError(request, response,
                    "Tên đăng nhập chỉ gồm chữ, số, dấu gạch dưới (3–30 ký tự)!");
            return;
        }
        if (!ValidationUtils.isAtLeastOne(email, phone)) {
            forwardWithError(request, response,
                    "Vui lòng nhập ít nhất email hoặc số điện thoại!");
            return;
        }
        if (!ValidationUtils.isBlank(email) && !ValidationUtils.isValidEmail(email)) {
            forwardWithError(request, response, "Email không đúng định dạng!");
            return;
        }
        if (!ValidationUtils.isBlank(phone) && !ValidationUtils.isValidPhone(phone)) {
            forwardWithError(request, response,
                    "Số điện thoại không đúng định dạng (VD: 0912345678)!");
            return;
        }
        if (!ValidationUtils.isValidPassword(password)) {
            forwardWithError(request, response,
                    "Mật khẩu phải ít nhất 8 ký tự, gồm chữ hoa, chữ thường và ký tự đặc biệt!");
            return;
        }
        if (!password.equals(confirmPassword)) {
            forwardWithError(request, response, "Mật khẩu xác nhận không khớp!");
            return;
        }
        if (userDao.findByUsername(username).isPresent()) {
            forwardWithError(request, response, "Tên đăng nhập đã tồn tại!");
            return;
        }
        if (!ValidationUtils.isBlank(phone) && userDao.findByPhone(phone).isPresent()) {
            forwardWithError(request, response, "Số điện thoại đã được sử dụng!");
            return;
        }
        if (!ValidationUtils.isBlank(email) && userDao.findByEmail(email).isPresent()) {
            forwardWithError(request, response, "Email đã được sử dụng!");
            return;
        }
        String passwordHash = PasswordUtils.hashPassword(password);
        User user = new User();
        user.setUsername(username);
        user.setEmail(ValidationUtils.isBlank(email) ? null : email);
        user.setPhone(ValidationUtils.isBlank(phone) ? null : phone);
        user.setPasswordHash(passwordHash);
        user.setFullName(username);
        userDao.insert(user);
        request.setAttribute("success", true);
        request.getRequestDispatcher("/frontend/public/Register.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/frontend/public/Register.jsp").forward(request, response);
    }
}