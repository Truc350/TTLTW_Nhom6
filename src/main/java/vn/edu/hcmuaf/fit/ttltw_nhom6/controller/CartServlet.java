package vn.edu.hcmuaf.fit.ttltw_nhom6.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Cart;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.CartItem;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.User;
import vn.edu.hcmuaf.fit.ttltw_nhom6.service.CartService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CartServlet", value = "/CartServlet")
public class CartServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        Cart cart = getOrCreateSessionCart(session);
        Integer userId = resolveUserId(session);
        String sessionId = session.getId();
        if (action == null || action.equals("view")) {
            viewCart(request, response, cart, userId, sessionId);

        } else if (action.equals("add")) {
            addToCart(request, response, cart, session, userId, sessionId);

        } else if (action.equals("update")) {
            updateCart(request, response, cart, session, userId, sessionId);

        } else if (action.equals("remove")) {
            removeFromCart(request, response, cart, session, userId, sessionId);

        } else if (action.equals("clear")) {
            clearCart(request, response, cart, session, userId, sessionId);

        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response,
                           Cart cart, HttpSession session,
                           Integer userId, String sessionId)
            throws ServletException, IOException {
        try {
            int comicId = Integer.parseInt(request.getParameter("comicId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            boolean isAjax = "true".equals(request.getParameter("ajax"));
            String returnUrl = request.getParameter("returnUrl");
            boolean buyNow = "true".equals(request.getParameter("buyNow"));

            if (quantity <= 0) quantity = 1;

            // Gọi CartService.addToCart (DB + sync session)
            String result = cartService.addToCart(cart, userId, sessionId, comicId, quantity);
            session.setAttribute("cart", cart);

            switch (result) {
                case "success":
                    CartItem addedItem = cart.get(comicId);
                    String successMsg = buildSuccessMsg(addedItem);
                    session.setAttribute("successMsg", successMsg);

                    if (isAjax) {
                        writeJson(response, true, successMsg);
                        return;
                    }

                    if (buyNow && addedItem != null) {
                        handleBuyNow(request, response, session, addedItem);
                        return;
                    }
                    break;

                case "out_of_stock":
                    String stockMsg = "Sản phẩm không đủ hàng trong kho.";
                    session.setAttribute("errorMsg", stockMsg);
                    if (isAjax) {
                        writeJson(response, false, stockMsg);
                        return;
                    }
                    break;

                case "not_found":
                    String nfMsg = "Không tìm thấy sản phẩm.";
                    session.setAttribute("errorMsg", nfMsg);
                    if (isAjax) {
                        writeJson(response, false, nfMsg);
                        return;
                    }
                    break;

                default:
                    String errMsg = "Có lỗi xảy ra, vui lòng thử lại.";
                    session.setAttribute("errorMsg", errMsg);
                    if (isAjax) {
                        writeJson(response, false, errMsg);
                        return;
                    }
                    break;
            }

            // Redirect sau khi add
            if ("wishlist".equals(returnUrl)) {
                response.sendRedirect(request.getContextPath() + "/wishlist");
            } else {
                response.sendRedirect(request.getContextPath() + "/comic-detail?id=" + comicId);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    private void viewCart(HttpServletRequest request, HttpServletResponse response,
                          Cart cart, Integer userId, String sessionId)
            throws ServletException, IOException {

        // Tắt cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession();

        // getCart: load từ DB + sync session
        List<CartItem> cartItems = cartService.getCart(cart, userId, sessionId);

        double totalAmount = cart.total();
        int totalQty = cart.totalQuantity();

        session.setAttribute("cart", cart);
        session.setAttribute("cartItems", cartItems);

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("totalQuantity", totalQty);
        request.setAttribute("checkoutTotal", totalAmount);

        User currentUser = (User) session.getAttribute("currentUser");
        request.setAttribute("isLoggedIn", currentUser != null);

        request.getRequestDispatcher("/frontend/nguoiB/cart.jsp").forward(request, response);
    }

    private void updateCart(HttpServletRequest request, HttpServletResponse response,
                            Cart cart, HttpSession session,
                            Integer userId, String sessionId)
            throws ServletException, IOException {
        try {
            int comicId = Integer.parseInt(request.getParameter("comicId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (quantity <= 0) {
                session.setAttribute("errorMsg", "Số lượng phải lớn hơn 0");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            String result = cartService.updateCart(cart, userId, sessionId, comicId, quantity);
            session.setAttribute("cart", cart);

            switch (result) {
                case "success":
                    session.setAttribute("successMsg", "Đã cập nhật số lượng");
                    break;
                case "out_of_stock":
                    session.setAttribute("errorMsg", "Sản phẩm không đủ hàng trong kho");
                    break;
                case "not_in_cart":
                    session.setAttribute("errorMsg", "Không tìm thấy sản phẩm trong giỏ hàng");
                    break;
                default:
                    session.setAttribute("errorMsg", "Có lỗi xảy ra");
                    break;
            }

            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response,
                                Cart cart, HttpSession session,
                                Integer userId, String sessionId)
            throws ServletException, IOException {
        try {
            int comicId = Integer.parseInt(request.getParameter("comicId"));

            boolean removed = cartService.removeItem(cart, userId, sessionId, comicId);
            session.setAttribute("cart", cart);

            if (removed) {
                session.setAttribute("successMsg", "Đã xóa sản phẩm khỏi giỏ hàng");
            } else {
                session.setAttribute("errorMsg", "Không tìm thấy sản phẩm");
            }

            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void clearCart(HttpServletRequest request, HttpServletResponse response,
                           Cart cart, HttpSession session,
                           Integer userId, String sessionId)
            throws ServletException, IOException {

        // Xóa toàn bộ items trong session
        cart.removeAllItems();
        session.setAttribute("cart", cart);
        session.setAttribute("successMsg", "Đã xóa toàn bộ giỏ hàng");

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private Cart getOrCreateSessionCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private Integer resolveUserId(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        return (user != null) ? user.getId() : null;
    }

    private String buildSuccessMsg(CartItem item) {
        if (item == null) return "Đã thêm vào giỏ hàng!";
        String name = item.getComic().getNameComics();
        if (item.isInFlashSale()) {
            return "Đã thêm \"" + name + "\" vào giỏ hàng! (Giá Flash Sale: "
                    + String.format("%,.0f", item.getFinalPrice()) + "₫)";
        }
        return "Đã thêm \"" + name + "\" vào giỏ hàng!";
    }

    private void handleBuyNow(HttpServletRequest request, HttpServletResponse response,
                              HttpSession session, CartItem item)
            throws IOException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            session.setAttribute("errorMsg", "Vui lòng đăng nhập để mua hàng");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<CartItem> selectedItems = new ArrayList<>();
        selectedItems.add(item);

        double subtotal = item.getFinalPrice() * item.getQuantity();
        double shippingFee = 25000;
        double total = subtotal + shippingFee;

        session.setAttribute("selectedItems", selectedItems);
        session.setAttribute("checkoutSubtotal", subtotal);
        session.setAttribute("shippingFee", shippingFee);
        session.setAttribute("checkoutTotal", total);

        response.sendRedirect(request.getContextPath() + "/checkout");
    }

    private void writeJson(HttpServletResponse response, boolean success, String message)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String escaped = message.replace("\"", "\\\"");
        response.getWriter().write(
                "{\"success\": " + success + ", \"message\": \"" + escaped + "\"}"
        );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
