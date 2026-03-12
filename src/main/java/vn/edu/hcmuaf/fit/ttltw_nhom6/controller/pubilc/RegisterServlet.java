package vn.edu.hcmuaf.fit.ttltw_nhom6.controller.pubilc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.ltw_nhom5.dao.UserDao;
import vn.edu.hcmuaf.fit.ltw_nhom5.db.JdbiConnector;
import vn.edu.hcmuaf.fit.ltw_nhom5.model.User;
import vn.edu.hcmuaf.fit.ltw_nhom5.utils.PasswordUtils;
import vn.edu.hcmuaf.fit.ltw_nhom5.utils.ValidationUtils;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao(JdbiConnector.get());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (ValidationUtils.isBlank(username)
                || ValidationUtils.isBlank(password) || ValidationUtils.isBlank(confirmPassword)) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.getRequestDispatcher("/fontend/public/Register.jsp").forward(request, response);
            return;
        }
        if (!ValidationUtils.isAtLeastOne(email, phone)) {
            request.setAttribute("error", "Vui lòng nhập ít nhất email hoặc số điện thoại!");
            request.getRequestDispatcher("/fontend/public/Register.jsp").forward(request, response);
            return;
        }
        if (!ValidationUtils.isBlank(email) && !ValidationUtils.isValidEmail(email)) {
            request.setAttribute("error", "Email không đúng định dạng!");
            request.getRequestDispatcher("/fontend/public/Register.jsp").forward(request, response);
            return;
        }
        if (!ValidationUtils.isBlank(phone) && !ValidationUtils.isValidPhone(phone)) {
            request.setAttribute("error", "Số điện thoại không đúng định dạng (VD: 0912345678)");
            request.getRequestDispatcher("/fontend/public/Register.jsp").forward(request, response);
            return;
        }


        // Kiểm tra mật khẩu khớp
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu không khớp!");
            request.getRequestDispatcher("/fontend/public/Register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra format mật khẩu
        if (!PasswordUtils.isValidPasswordFormat(password)) {
            request.setAttribute("error", "Mật khẩu phải ít nhất 8 ký tự, chữ hoa, thường, số và ký tự đặc biệt!");
            request.getRequestDispatcher("/fontend/public/Register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra username/email đã tồn tại
        if (userDao.findByUsername(username).isPresent()) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại!");
            request.getRequestDispatcher("/fontend/public/Register.jsp").forward(request, response);
            return;
        }
        if (!ValidationUtils.isBlank(phone) && userDao.findByPhone(phone).isPresent()) {
            request.setAttribute("error", "Số điện thoại đã được sử dụng!");
            request.getRequestDispatcher("/fontend/public/Register.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtils.isBlank(email) && userDao.findByEmail(email).isPresent()) {
            request.setAttribute("error", "Email đã được sử dụng!");
            request.getRequestDispatcher("/fontend/public/Register.jsp").forward(request, response);
            return;
        }

        // Hash mật khẩu và lưu
        String passwordHash = PasswordUtils.hashPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setEmail(ValidationUtils.isBlank(email) ? null : email);
        user.setPhone(ValidationUtils.isBlank(phone) ? null : phone);
        user.setPasswordHash(passwordHash);
        user.setFullName(username);

        // Sau khi insert user thành công
        userDao.insert(user);
        request.setAttribute("success", true);
        request.getRequestDispatcher("/fontend/public/Register.jsp").forward(request, response);

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/fontend/public/Register.jsp").forward(request, response);
    }
}
