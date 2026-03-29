package vn.edu.hcmuaf.fit.ttltw_nhom6.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.fit.ttltw_nhom6.dao.FlashSaleComicsDAO;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Cart;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.CartItem;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Comic;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.User;
import vn.edu.hcmuaf.fit.ttltw_nhom6.service.ComicService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "Cart", value = "/cart")
public class CartSevlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    private void clearCart(HttpServletRequest request, HttpServletResponse response, Cart cart, HttpSession session) throws ServletException, IOException {
        cart.removeAllItems();
        session.setAttribute("cart", cart);
        session.setAttribute("successMsg", "Đã xóa toàn bộ giỏ hàng");

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response, Cart cart, HttpSession session) throws ServletException, IOException {
        try {
            int comicId = Integer.parseInt(request.getParameter("comicId"));

            cart.removeItem(comicId);
            session.setAttribute("cart", cart);
            session.setAttribute("successMsg", "Đã xóa sản phẩm khỏi giỏ hàng");

            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void updateCart(HttpServletRequest request, HttpServletResponse response, Cart cart, HttpSession session) throws ServletException, IOException {
        try {
            int comicId = Integer.parseInt(request.getParameter("comicId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (quantity <= 0) {
                session.setAttribute("errorMsg", "Số lượng phải lớn hơn 0");
                response.sendRedirect(request.getContextPath() + "/cart");
                quantity = 1;
            }

            // ktra ton kho
            ComicService comicService = new ComicService();
            Comic comic = comicService.getComicById(comicId);

            if(comic != null && comic.getStockQuantity() < quantity){
                session.setAttribute("errorMsg", "Sản phẩm không đủ hàng. Chỉ còn " + comic.getStockQuantity() + " sản phẩm.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            boolean success = cart.updateItem(comicId, quantity);

            if (success) {
                session.setAttribute("cart", cart);
                session.setAttribute("successMsg", "Đã cập nhật số lượng");
            } else {
                session.setAttribute("errorMsg", "Không tìm thấy sản phẩm trong giỏ hàng");
            }

            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/cart");
        }

    }

    private void viewCart(HttpServletRequest request, HttpServletResponse response, Cart cart)
            throws ServletException, IOException {
        // Tắt cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        FlashSaleComicsDAO flashSaleComicsDAO = new FlashSaleComicsDAO();
        HttpSession session = request.getSession();
        boolean hasUpdates = false;

        for (CartItem item : cart.getItems()) {
            int comicId = item.getComic().getId();

            Map<String, Object> flashSaleInfo = flashSaleComicsDAO.getFlashSaleInfoByComicId(comicId);

            if (flashSaleInfo != null) {
                Integer currentFlashSaleId = (Integer) flashSaleInfo.get("flashsale_id");
                Object discountObj = flashSaleInfo.get("discount_percent");
                Double discountPercent = (discountObj instanceof Number)
                        ? ((Number) discountObj).doubleValue()
                        : null;

                if (discountPercent != null) {
                    double originalPrice = item.getComic().getPrice();
                    double newFlashSalePrice = originalPrice * (1 - discountPercent / 100.0);

                    if (item.getFlashSalePrice() == null ||
                            !currentFlashSaleId.equals(item.getFlashSaleId()) ||
                            Math.abs(item.getFlashSalePrice() - newFlashSalePrice) > 0.01) {

                        item.updateFlashSale(currentFlashSaleId, newFlashSalePrice);
                        hasUpdates = true;

                    }
                }
            } else {
                if (item.getFlashSaleId() != null) {
                    double discountPrice = item.getComic().getDiscountPrice();

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

        List<CartItem> cartItems = cart.getItems();
        double totalAmount = cart.total();
        int totalQuantity = cart.totalQuantity();

        session.setAttribute("cart", cart);
        session.setAttribute("cartItems", cartItems);

        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("totalQuantity", totalQuantity);

        // Kiểm tra trạng thái đăng nhập
        User currentUser = (User) session.getAttribute("currentUser");
        request.setAttribute("isLoggedIn", currentUser != null);

        request.getRequestDispatcher("/frontend/nguoiB/cart.jsp").forward(request, response);
    }
    private void addToCart(HttpServletRequest request, HttpServletResponse response, Cart cart, HttpSession session) throws ServletException, IOException {
        try {
            int comicId = Integer.parseInt(request.getParameter("comicId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            boolean isAjax = "true".equals(request.getParameter("ajax"));
            String returnUrl = request.getParameter("returnUrl");

            if (quantity <= 0) {
                quantity = 1;
            }

            ComicService comicService = new ComicService();
            Comic comic = comicService.getComicById(comicId);

            if (comic != null) {
                CartItem existingItem = cart.get(comicId);
                int totalQuantity = (existingItem != null) ? existingItem.getQuantity() + quantity : quantity;

                if (comic.getStockQuantity() < totalQuantity) {
                    session.setAttribute("errorMsg", "Sản phẩm không đủ hàng. Chỉ còn " +
                            comic.getStockQuantity() + " sản phẩm.");

                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"success\": false, \"message\": \"Sản phẩm không đủ hàng\"}");
                        return;
                    }

                    response.sendRedirect(request.getContextPath() + "/comic-detail?id=" + comicId);
                    return;
                }

                FlashSaleComicsDAO flashSaleComicsDAO = new FlashSaleComicsDAO();
                Map<String, Object> flashSaleInfo = flashSaleComicsDAO.getFlashSaleInfoByComicId(comicId);

                Integer flashSaleId = null;
                Double flashSalePrice = null;

                if (flashSaleInfo != null) {
                    flashSaleId = (Integer) flashSaleInfo.get("flashsale_id");
                    Object discountObj = flashSaleInfo.get("discount_percent");
                    Double discountPercent = (discountObj instanceof Number)
                            ? ((Number) discountObj).doubleValue()
                            : null;

                    // Tính giá Flash Sale từ GIÁ GỐC
                    if (discountPercent != null) {
                        flashSalePrice = comic.getPrice() * (1 - discountPercent / 100.0);
                    }
                } else {
                    System.out.println("Comic không trong Flash Sale");
                }

                cart.addItem(comic, quantity, flashSaleId, flashSalePrice);
                session.setAttribute("cart", cart);

                String successMsg = "Đã thêm \"" + comic.getNameComics() + "\" vào giỏ hàng!";
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

                boolean buyNow = "true".equals(request.getParameter("buyNow"));

                if (buyNow) {
                    User currentUser = (User) session.getAttribute("currentUser");

                    if (currentUser == null) {
                        session.setAttribute("errorMsg", "Vui lòng đăng nhập để mua hàng");
                        response.sendRedirect(request.getContextPath() + "/login");
                        return;
                    }

                    CartItem addedItem = cart.get(comicId);
                    if (addedItem != null) {
                        List<CartItem> selectedItems = new ArrayList<>();
                        selectedItems.add(addedItem);

                        // Tính toán cho checkout
                        double subtotal = addedItem.getFinalPrice() * addedItem.getQuantity();
                        double shippingFee = 25000;
                        double totalAmount = subtotal + shippingFee;

                        // Set vào session
                        session.setAttribute("selectedItems", selectedItems);
                        session.setAttribute("checkoutSubtotal", subtotal);
                        session.setAttribute("shippingFee", shippingFee);
                        session.setAttribute("checkoutTotal", totalAmount);

                        // Redirect đến checkout
                        response.sendRedirect(request.getContextPath() + "/checkout");
                    }
                }



                else {
                    if ("wishlist".equals(returnUrl)) {
                        response.sendRedirect(request.getContextPath() + "/wishlist");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/comic-detail?id=" + comicId);
                    }
                }
            } else {
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
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("errorMsg", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}