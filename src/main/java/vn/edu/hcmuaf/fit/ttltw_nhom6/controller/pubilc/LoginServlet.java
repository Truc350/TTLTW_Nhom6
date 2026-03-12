package vn.edu.hcmuaf.fit.ttltw_nhom6.controller.pubilc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.ltw_nhom5.dao.UserDao;
import vn.edu.hcmuaf.fit.ltw_nhom5.db.JdbiConnector;
import vn.edu.hcmuaf.fit.ltw_nhom5.model.Cart;
import vn.edu.hcmuaf.fit.ltw_nhom5.model.User;
import vn.edu.hcmuaf.fit.ltw_nhom5.service.OrderViolationService;
import vn.edu.hcmuaf.fit.ltw_nhom5.utils.PasswordUtils;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao(JdbiConnector.get());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usernameOrEmail = request.getParameter("username");
        String password = request.getParameter("password");

        // Kiểm tra format mật khẩu trước
        if (!PasswordUtils.isValidPasswordFormat(password)) {
            request.setAttribute("error", "Mật khẩu ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt!");
            request.getRequestDispatcher("/fontend/public/login.jsp").forward(request, response);
            return;
        }

        Optional<User> userOpt = userDao.findByUsernameOrEmail(usernameOrEmail);
        boolean isActive = userDao.isUserActive(userOpt);
        if (!isActive) {
            request.setAttribute("error", "Tài khoản của bạn đã bị khóa!");
            request.getRequestDispatcher("/fontend/public/login.jsp").forward(request, response);
            return;
        }
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
                OrderViolationService.getInstance().resetLoginFailureCount(user.getId());

                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) {
                    Cart oldCart = (Cart) oldSession.getAttribute("cart");
                    if (oldCart != null) {
                    }
                    oldSession.invalidate();
                }

                HttpSession newSession = request.getSession(true); // true = tạo mới nếu chưa có

//                check admin
                boolean isAdmin = "ADMIN".equalsIgnoreCase(user.getRole());

                if (isAdmin) {
                    newSession.setAttribute("currentUser", user);
                    newSession.setAttribute("userId", user.getId());
                    newSession.setAttribute("isAdmin", true);

                    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                    response.setHeader("Pragma", "no-cache");
                    response.setDateHeader("Expires", 0);

                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    return;
                }
                Cart newCart = new Cart();

//                Lưu vào session mới
                newSession.setAttribute("cart", newCart);
                newSession.setAttribute("currentUser", user);
                newSession.setAttribute("clearCartLocalStorage", true);


                // Tắt cache để buộc browser load lại trang mới
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);

                String redirectUrl = (String) request.getSession().getAttribute("redirectAfterLogin");

                if (redirectUrl != null) {
                    request.getSession().removeAttribute("redirectAfterLogin");
                    response.sendRedirect(redirectUrl);
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            } else {
                OrderViolationService.getInstance().incrementLoginFailureCount(usernameOrEmail);
                OrderViolationService.getInstance().checkLoginFailureViolation(usernameOrEmail);

                request.setAttribute("error", "Hãy nhập đúng tài khoản và mật khẩu!");
                request.getRequestDispatcher("/fontend/public/login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Hãy nhập đúng tài khoản và mật khẩu!");
            request.getRequestDispatcher("/fontend/public/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("currentUser") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.getRequestDispatcher("/fontend/public/login.jsp").forward(request, response);
    }


}
