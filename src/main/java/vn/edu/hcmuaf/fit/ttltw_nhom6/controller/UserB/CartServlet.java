package vn.edu.hcmuaf.fit.ttltw_nhom6.controller.UserB;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.ttltw_nhom6.dao.FlashSaleComicsDAO;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Cart;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.CartItem;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Comic;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.User;
import vn.edu.hcmuaf.fit.ttltw_nhom6.service.CartService;
import vn.edu.hcmuaf.fit.ttltw_nhom6.service.ComicService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private CartService cartService;  // THÊM MỚI

    @Override
    public void init() {
        cartService = new CartService();  // THÊM MỚI
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        if (action == null || action.equals("view")) {
            viewCart(request, response, cart);
        } else if (action.equals("add")) {
            addToCart(request, response, cart, session);
        } else if (action.equals("update")) {
            updateCart(request, response, cart, session);
        } else if (action.equals("remove")) {
            removeFromCart(request, response, cart, session);
        } else if (action.equals("clear")) {
            clearCart(request, response, cart, session);
        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
    private void clearCart(HttpServletRequest request, HttpServletResponse response,
                           Cart cart, HttpSession session)
            throws ServletException, IOException {

        User    currentUser = (User) session.getAttribute("currentUser");
        Integer userId      = (currentUser != null) ? currentUser.getId() : null;

        if (userId != null) {
            // Đã đăng nhập → xóa DB
            cartService.clearCart(cart, userId, null);
        } else {
            // Guest → xóa session
            cart.removeAllItems();
        }

        session.setAttribute("cart", cart);
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response,
                                Cart cart, HttpSession session)
            throws ServletException, IOException {
        try {
            int comicId = Integer.parseInt(request.getParameter("comicId"));

            User    currentUser = (User) session.getAttribute("currentUser");
            Integer userId      = (currentUser != null) ? currentUser.getId() : null;

            if (userId != null) {
                // Đã đăng nhập → xóa trong DB
                cartService.removeItem(cart, userId, null, comicId);
            } else {
                // Guest → xóa trong session
                cart.removeItem(comicId);
            }

            session.setAttribute("cart", cart);
            session.setAttribute("successMsg", "Đã xóa sản phẩm khỏi giỏ hàng");
            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMsg", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void updateCart(HttpServletRequest request, HttpServletResponse response,
                            Cart cart, HttpSession session)
            throws ServletException, IOException {
        try {
            int comicId  = Integer.parseInt(request.getParameter("comicId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            if (quantity <= 0) quantity = 1;

            User    currentUser = (User) session.getAttribute("currentUser");
            Integer userId      = (currentUser != null) ? currentUser.getId() : null;

            if (userId != null) {
                // Đã đăng nhập → update DB
                String result = cartService.updateCart(cart, userId, null, comicId, quantity);
                if ("out_of_stock".equals(result)) {
                    session.setAttribute("errorMsg", "Sản phẩm không đủ hàng");
                }
            } else {
                // Guest → update session
                cart.updateItem(comicId, quantity);
            }

            session.setAttribute("cart", cart);
            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMsg", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void viewCart(HttpServletRequest request, HttpServletResponse response, Cart cart)
            throws ServletException, IOException {

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        FlashSaleComicsDAO flashSaleComicsDAO = new FlashSaleComicsDAO();
        HttpSession session = request.getSession();
        User    currentUser = (User) session.getAttribute("currentUser");
        Integer userId      = (currentUser != null) ? currentUser.getId() : null;
        CartService cartService = new CartService();
        cartService.getCart(cart, userId, session.getId());
        session.setAttribute("cart", cart);
        boolean hasUpdates = false;

        for (CartItem item : cart.getItems()) {
            int comicId = item.getComic().getId();
            Map<String, Object> flashSaleInfo = flashSaleComicsDAO.getFlashSaleInfoByComicId(comicId);

            if (flashSaleInfo != null) {
                Integer currentFlashSaleId = (Integer) flashSaleInfo.get("flashsale_id");
                Object  discountObj        = flashSaleInfo.get("discount_percent");
                Double  discountPercent    = (discountObj instanceof Number)
                        ? ((Number) discountObj).doubleValue() : null;

                if (discountPercent != null) {
                    double originalPrice    = item.getComic().getPrice();
                    double newFlashSalePrice = originalPrice * (1 - discountPercent / 100.0);

                    if (item.getFlashSalePrice() == null
                            || !currentFlashSaleId.equals(item.getFlashSaleId())
                            || Math.abs(item.getFlashSalePrice() - newFlashSalePrice) > 0.01) {
                        item.updateFlashSale(currentFlashSaleId, newFlashSalePrice);
                        hasUpdates = true;
                    }
                }
            } else {
                if (item.getFlashSaleId() != null) {
                    item.removeFlashSale();
                    hasUpdates = true;
                } else {
                    double currentDiscountPrice = item.getComic().getDiscountPrice();
                    if (Math.abs(item.getPriceAtPurchase() - currentDiscountPrice) > 0.01) {
                        item.setPriceAtPurchase(currentDiscountPrice);
                        hasUpdates = true;
                    }
                }
            }
        }

        if (hasUpdates) {
            session.setAttribute("cart", cart);
        }

        List<CartItem> cartItems    = cart.getItems();
        double         totalAmount  = cart.total();
        int            totalQty     = cart.totalQuantity();

        session.setAttribute("cart",      cart);
        session.setAttribute("cartItems", cartItems);

        request.setAttribute("totalAmount",   totalAmount);
        request.setAttribute("totalQuantity", totalQty);

//        User currentUser = (User) session.getAttribute("currentUser");
        request.setAttribute("isLoggedIn", currentUser != null);

        request.getRequestDispatcher("/frontend/nguoiB/cart.jsp").forward(request, response);
    }
    private void addToCart(HttpServletRequest request, HttpServletResponse response,
                           Cart cart, HttpSession session)
            throws ServletException, IOException {
        try {
            int comicId  = Integer.parseInt(request.getParameter("comicId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            if (quantity <= 0) quantity = 1;

            boolean isAjax    = "true".equals(request.getParameter("ajax"));
            String  returnUrl = request.getParameter("returnUrl");

            ComicService comicService = new ComicService();
            Comic        comic        = comicService.getComicById(comicId);

<<<<<<< HEAD:src/main/java/vn/edu/hcmuaf/fit/ttltw_nhom6/controller/UserB/CartSevlet.java
            if (comic == null) {
                session.setAttribute("errorMsg", "Không tìm thấy sản phẩm");
                if (isAjax) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"success\": false, \"message\": \"Không tìm thấy sản phẩm\"}");
                    return;
                }
                if ("wishlist".equals(returnUrl)) {
                    response.sendRedirect(request.getContextPath() + "/wishlist");
                } else {
                    response.sendRedirect(request.getContextPath() + "/comic-detail?id=" + comicId);
                }
                return;
            }

            User    currentUser = (User) session.getAttribute("currentUser");
            Integer userId      = (currentUser != null) ? currentUser.getId() : null;
            if (userId != null) {
                CartItem existingItem  = cart.get(comicId);
                int      totalQuantity = (existingItem != null)
                        ? existingItem.getQuantity() + quantity : quantity;

                if (comic.getStockQuantity() < totalQuantity) {
                    session.setAttribute("errorMsg",
                            "Sản phẩm không đủ hàng. Chỉ còn " + comic.getStockQuantity() + " sản phẩm.");
                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": false, \"message\": \"Sản phẩm không đủ hàng\"}");
                        return;
                    }
                    response.sendRedirect(request.getContextPath() + "/comic-detail?id=" + comicId);
                    return;
                }

                String result = cartService.addToCart(cart, userId, null, comicId, quantity);
                session.setAttribute("cart", cart);

                String successMsg = "Đã thêm \"" + comic.getNameComics() + "\" vào giỏ hàng!";
                if ("out_of_stock".equals(result)) {
                    session.setAttribute("errorMsg", "Sản phẩm không đủ hàng");
                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": false, \"message\": \"Sản phẩm không đủ hàng\"}");
                        return;
                    }
                    response.sendRedirect(request.getContextPath() + "/comic-detail?id=" + comicId);
                    return;
                }
                if ("not_found".equals(result)) {
                    session.setAttribute("errorMsg", "Không tìm thấy sản phẩm");
                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": false, \"message\": \"Không tìm thấy sản phẩm\"}");
                        return;
                    }
                    response.sendRedirect(request.getContextPath() + "/comic-detail?id=" + comicId);
                    return;
                }

                session.setAttribute("successMsg", successMsg);
            } else {
                CartItem existingItem  = cart.get(comicId);
                int      totalQuantity = (existingItem != null)
                        ? existingItem.getQuantity() + quantity : quantity;

                if (comic.getStockQuantity() < totalQuantity) {
                    session.setAttribute("errorMsg",
                            "Sản phẩm không đủ hàng. Chỉ còn " + comic.getStockQuantity() + " sản phẩm.");
                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": false, \"message\": \"Sản phẩm không đủ hàng\"}");
                        return;
                    }
                    response.sendRedirect(request.getContextPath() + "/comic-detail?id=" + comicId);
                    return;
                }

                // Lấy flash sale nếu có
                FlashSaleComicsDAO  flashSaleComicsDAO = new FlashSaleComicsDAO();
                Map<String, Object> flashSaleInfo      = flashSaleComicsDAO.getFlashSaleInfoByComicId(comicId);
                Integer flashSaleId    = null;
                Double  flashSalePrice = null;
                if (flashSaleInfo != null) {
                    flashSaleId = (Integer) flashSaleInfo.get("flashsale_id");
                    Object discountObj = flashSaleInfo.get("discount_percent");
                    Double discount    = (discountObj instanceof Number)
                            ? ((Number) discountObj).doubleValue() : null;
                    if (discount != null) {
                        flashSalePrice = comic.getPrice() * (1 - discount / 100.0);
                    }
                }

                // Lưu vào Cart object trong session
                cart.addItem(comic, quantity, flashSaleId, flashSalePrice);
                session.setAttribute("cart", cart);

                String successMsg = "Đã thêm \"" + comic.getNameComics() + "\" vào giỏ hàng!";
=======
            if (comic != null) {

                // Lấy thông tin Flash Sale (để hiển thị message)
                FlashSaleComicsDAO  flashSaleComicsDAO = new FlashSaleComicsDAO();
                Map<String, Object> flashSaleInfo      = flashSaleComicsDAO.getFlashSaleInfoByComicId(comicId);
                Double flashSalePrice = null;
                if (flashSaleInfo != null) {
                    Object discountObj     = flashSaleInfo.get("discount_percent");
                    Double discountPercent = (discountObj instanceof Number)
                            ? ((Number) discountObj).doubleValue() : null;
                    if (discountPercent != null) {
                        flashSalePrice = comic.getPrice() * (1 - discountPercent / 100.0);
                    }
                }

                // SỬA: dùng CartService để lưu xuống DB
                User    currentUser = (User) session.getAttribute("currentUser");
                Integer userId      = (currentUser != null) ? currentUser.getId() : null;

                String result = cartService.addToCart(cart, userId, session.getId(), comicId, quantity);
                session.setAttribute("cart", cart);

                if ("-1".equals(result) || "not_found".equals(result)) {
                    // Sản phẩm không tồn tại / bị ẩn / bị xóa
                    session.setAttribute("errorMsg", "Sản phẩm không tồn tại hoặc đã bị ẩn");
                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": false, \"message\": \"Sản phẩm không tồn tại\"}");
                        return;
                    }
                    response.sendRedirect(request.getContextPath() + "/comic-detail?id=" + comicId);
                    return;
                }

                if ("out_of_stock".equals(result)) {
                    // Hết hàng hoàn toàn (available <= 0)
                    session.setAttribute("errorMsg", "Sản phẩm đã hết hàng");
                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": false, \"message\": \"Sản phẩm đã hết hàng\"}");
                        return;
                    }
                    response.sendRedirect(request.getContextPath() + "/comic-detail?id=" + comicId);
                    return;
                }

                // Xây dựng message
                String successMsg = "Đã thêm \"" + comic.getNameComics() + "\" vào giỏ hàng!";
>>>>>>> ca824ecbf44dc1d10871cb2602f4b3b20979309c:src/main/java/vn/edu/hcmuaf/fit/ttltw_nhom6/controller/UserB/CartServlet.java
                if (flashSalePrice != null) {
                    successMsg += " (Giá Flash Sale: " + String.format("%,.0f", flashSalePrice) + "₫)";
                } else if (comic.getDiscountPrice() < comic.getPrice()) {
                    successMsg += " (Giá ưu đãi: " + String.format("%,.0f", comic.getDiscountPrice()) + "₫)";
                }
                session.setAttribute("successMsg", successMsg);

                if (isAjax) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"success\": true, \"message\": \"" + successMsg + "\"}");
                    return;
                }
            }
            boolean buyNow = "true".equals(request.getParameter("buyNow"));
            if (buyNow) {
                if (currentUser == null) {
                    session.setAttribute("errorMsg", "Vui lòng đăng nhập để mua hàng");
                    response.sendRedirect(request.getContextPath() + "/login");
                    return;
                }
                CartItem addedItem = cart.get(comicId);
                if (addedItem != null) {
                    List<CartItem> selectedItems = new ArrayList<>();
                    selectedItems.add(addedItem);

                    double subtotal    = addedItem.getFinalPrice() * addedItem.getQuantity();
                    double shippingFee = 25000;
                    double totalAmount = subtotal + shippingFee;

                    session.setAttribute("selectedItems",    selectedItems);
                    session.setAttribute("checkoutSubtotal", subtotal);
                    session.setAttribute("shippingFee",      shippingFee);
                    session.setAttribute("checkoutTotal",    totalAmount);

                    response.sendRedirect(request.getContextPath() + "/checkout");
                }
            } else {
                if (isAjax) {
                    // Đã được handle ở trên cho cả 2 case, không cần xử lý thêm
                    return;
                }
                if ("wishlist".equals(returnUrl)) {
                    response.sendRedirect(request.getContextPath() + "/wishlist");
                } else {
                    response.sendRedirect(request.getContextPath() + "/comic-detail?id=" + comicId);
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}