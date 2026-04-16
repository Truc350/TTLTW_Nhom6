package vn.edu.hcmuaf.fit.ttltw_nhom6.controller.pubilc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.ttltw_nhom6.dao.UserDao;
import vn.edu.hcmuaf.fit.ttltw_nhom6.db.JdbiConnector;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Cart;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.CartItem;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.User;
import vn.edu.hcmuaf.fit.ttltw_nhom6.service.CartService;
import vn.edu.hcmuaf.fit.ttltw_nhom6.service.OrderViolationService;
import vn.edu.hcmuaf.fit.ttltw_nhom6.utils.PasswordUtils;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDao    userDao;
    private CartService cartService;
    @Override
    public void init() {
        userDao     = new UserDao(JdbiConnector.get());
        cartService = new CartService();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usernameOrEmail = request.getParameter("username");
        String password        = request.getParameter("password");
        // Validate format mật khẩu
        if (!PasswordUtils.isValidPasswordFormat(password)) {
            request.setAttribute("error", "Mật khẩu ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt!");
            request.getRequestDispatcher("/frontend/public/login.jsp").forward(request, response);
            return;
        }
        Optional<User> userOpt = userDao.findByUsernameOrEmail(usernameOrEmail);
        boolean isActive = userDao.isUserActive(userOpt);
        if (!isActive) {
            request.setAttribute("error", "Tài khoản của bạn đã bị khóa!");
            request.getRequestDispatcher("/frontend/public/login.jsp").forward(request, response);
            return;
        }
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
                OrderViolationService.getInstance().resetLoginFailureCount(user.getId());
                HttpSession oldSession = request.getSession(false);
                String guestSessionId = null;
//                Cart guestCart = null;
                if (oldSession != null) {
                    guestSessionId = oldSession.getId();

                    Cart guestCart = (Cart) oldSession.getAttribute("cart");
                    if (guestCart != null && !guestCart.getItems().isEmpty()) {
                        // Lưu từng item vào DB với sessionId này
                        for (CartItem item : guestCart.getItems()) {
                            cartService.addToCart(
                                    guestCart,
                                    null,
                                    guestSessionId,
                                    item.getComic().getId(),
                                    item.getQuantity()
                            );
                        }
                    }

                    guestSessionId = oldSession.getId();
//                    guestCart = (Cart) oldSession.getAttribute("cart");
                    oldSession.invalidate(); // Hủy session cũ (bảo mật)
                }
                HttpSession newSession = request.getSession(true);
                // Admin → không cần cart
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    newSession.setAttribute("currentUser", user);
                    newSession.setAttribute("userId", user.getId());
                    newSession.setAttribute("isAdmin", true);
                    setNoCacheHeaders(response);
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    return;
                }

                Cart userCart = new Cart();
                if (guestSessionId != null) {
                    cartService.mergeCart(userCart, user.getId(), guestSessionId);
                } else {
                    cartService.getCart(userCart, user.getId(), newSession.getId());
                }
                newSession.setAttribute("cart",userCart);
                newSession.setAttribute("currentUser", user);
                newSession.setAttribute("clearCartLocalStorage", true);

                setNoCacheHeaders(response);
                // Redirect sau login
                String redirectUrl = (String) newSession.getAttribute("redirectAfterLogin");
                if (redirectUrl != null) {
                    newSession.removeAttribute("redirectAfterLogin");
                    response.sendRedirect(redirectUrl);
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }

            } else {
                // Sai mật khẩu
                OrderViolationService.getInstance().incrementLoginFailureCount(usernameOrEmail);
                OrderViolationService.getInstance().checkLoginFailureViolation(usernameOrEmail);

                request.setAttribute("error", "Hãy nhập đúng tài khoản và mật khẩu!");
                request.getRequestDispatcher("/frontend/public/login.jsp").forward(request, response);
            }

        } else {
            request.setAttribute("error", "Hãy nhập đúng tài khoản và mật khẩu!");
            request.getRequestDispatcher("/frontend/public/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("currentUser") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        request.getRequestDispatcher("/frontend/public/login.jsp").forward(request, response);
    }

    private void setNoCacheHeaders(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}