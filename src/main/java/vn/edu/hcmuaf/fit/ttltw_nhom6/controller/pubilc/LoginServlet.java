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
    private UserDao     userDao;
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

        if (!PasswordUtils.isValidPasswordFormat(password)) {
            request.setAttribute("error",
                    "Mat khau it nhat 8 ky tu, gom chu hoa, chu thuong, so va ky tu dac biet!");
            request.getRequestDispatcher("/frontend/public/login.jsp").forward(request, response);
            return;
        }

        Optional<User> userOpt = userDao.findByUsernameOrEmail(usernameOrEmail);
        if (!userDao.isUserActive(userOpt)) {
            request.setAttribute("error", "Tai khoan cua ban da bi khoa!");
            request.getRequestDispatcher("/frontend/public/login.jsp").forward(request, response);
            return;
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
                OrderViolationService.getInstance().resetLoginFailureCount(user.getId());

                // BUOC 1: Lay guestSessionId TRUOC KHI invalidate session cu.
                // Cart guest da duoc luu xuong DB boi CartSevlet,
                // nen chi can sessionId de tra cuu trong DB, khong can cart object.
                HttpSession oldSession     = request.getSession(false);
                String      guestSessionId = null;
                if (oldSession != null) {
                    guestSessionId = oldSession.getId();
                    oldSession.invalidate();
                }

                // BUOC 2: Tao session moi
                HttpSession newSession = request.getSession(true);

                // Admin khong can cart
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    newSession.setAttribute("currentUser", user);
                    newSession.setAttribute("userId",      user.getId());
                    newSession.setAttribute("isAdmin",     true);
                    setNoCacheHeaders(response);
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    return;
                }

                // BUOC 3: Merge guest cart (DB) vao user cart (DB)
                // CartDAO.mergeCart doc guest cart theo guestSessionId,
                // gop vao user cart, roi xoa guest cart.
                Cart userCart = new Cart();
                if (guestSessionId != null) {
                    cartService.mergeCart(userCart, user.getId(), guestSessionId);
                } else {
                    cartService.getCart(userCart, user.getId(), newSession.getId());
                }

                newSession.setAttribute("cart",                  userCart);
                newSession.setAttribute("currentUser",           user);
                newSession.setAttribute("clearCartLocalStorage", true);

                setNoCacheHeaders(response);
                String redirectUrl = (String) newSession.getAttribute("redirectAfterLogin");
                if (redirectUrl != null) {
                    newSession.removeAttribute("redirectAfterLogin");
                    response.sendRedirect(redirectUrl);
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }

            } else {
                OrderViolationService.getInstance().incrementLoginFailureCount(usernameOrEmail);
                OrderViolationService.getInstance().checkLoginFailureViolation(usernameOrEmail);
                request.setAttribute("error", "Hay nhap dung tai khoan va mat khau!");
                request.getRequestDispatcher("/frontend/public/login.jsp").forward(request, response);
            }

        } else {
            request.setAttribute("error", "Hay nhap dung tai khoan va mat khau!");
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
        response.setHeader("Pragma",        "no-cache");
        response.setDateHeader("Expires",   0);
    }
}