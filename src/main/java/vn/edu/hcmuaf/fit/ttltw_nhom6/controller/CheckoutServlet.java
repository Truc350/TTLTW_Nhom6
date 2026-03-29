package vn.edu.hcmuaf.fit.ttltw_nhom6.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.ttltw_nhom6.dao.FlashSaleComicsDAO;
import vn.edu.hcmuaf.fit.ttltw_nhom6.dao.UserShippingAddressDAO;
import vn.edu.hcmuaf.fit.ttltw_nhom6.db.JdbiConnector;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.CartItem;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Comic;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.User;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.UserShippingAddress;
import vn.edu.hcmuaf.fit.ttltw_nhom6.service.ComicService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private UserShippingAddressDAO shippingAddressDAO;

    @Override
    public void init() throws ServletException {
        Jdbi jdbi = JdbiConnector.get();
        shippingAddressDAO = new UserShippingAddressDAO(jdbi);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String comicIdParam = request.getParameter("comicId");
        if (comicIdParam != null) {
            try {
                int comicId = Integer.parseInt(comicIdParam);
                int quantity = request.getParameter("quantity") != null
                        ? Integer.parseInt(request.getParameter("quantity")) : 1;

                ComicService comicService = new ComicService();
                Comic comic = comicService.getComicById(comicId);

                if (comic == null || comic.getStockQuantity() < quantity) {
                    session.setAttribute("errorMsg", "Sản phẩm không đủ hàng");
                    response.sendRedirect(request.getContextPath() + "/comic-detail?id=" + comicId);
                    return;
                }

                FlashSaleComicsDAO flashSaleDAO = new FlashSaleComicsDAO();
                Map<String, Object> flashInfo = flashSaleDAO.getFlashSaleInfoByComicId(comicId);
                Integer flashSaleId = null;
                Double flashSalePrice = null;
                if (flashInfo != null) {
                    flashSaleId = (Integer) flashInfo.get("flashsale_id");
                    Object discountObj = flashInfo.get("discount_percent");
                    if (discountObj instanceof Number) {
                        double pct = ((Number) discountObj).doubleValue();
                        flashSalePrice = comic.getPrice() * (1 - pct / 100.0);
                    }
                }

                CartItem tempItem = new CartItem(comic, quantity, flashSaleId, flashSalePrice);
                double subtotal = tempItem.getFinalPrice() * quantity;


                double shippingFee = 25000;
                String shippingFeeParam = request.getParameter("shippingFee");
                if (shippingFeeParam != null && !shippingFeeParam.isEmpty()) {
                    try {
                        shippingFee = Double.parseDouble(shippingFeeParam);
                    } catch (NumberFormatException ignored) {}
                }

                List<CartItem> selectedItems = new ArrayList<>();
                selectedItems.add(tempItem);

                session.setAttribute("selectedItems", selectedItems);
                session.setAttribute("checkoutSubtotal", subtotal);
                session.setAttribute("shippingFee", shippingFee);
                session.setAttribute("checkoutTotal", subtotal + shippingFee);

            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
        }

        request.setAttribute("user", user);

        @SuppressWarnings("unchecked")
        List<CartItem> selectedItems = (List<CartItem>) session.getAttribute("selectedItems");

        if (selectedItems == null || selectedItems.isEmpty()) {
            session.setAttribute("cartError", "Vui lòng chọn sản phẩm để thanh toán");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        Optional<UserShippingAddress> defaultAddress =
                shippingAddressDAO.getDefaultAddress(user.getId());

        if (defaultAddress.isPresent()) {
            UserShippingAddress address = defaultAddress.get();

            if (address.getDistrict() == null || address.getDistrict().trim().isEmpty()) {
                List<UserShippingAddress> allAddresses = shippingAddressDAO.getAddressesByUserId(user.getId());
                UserShippingAddress addressWithDistrict = allAddresses.stream()
                        .filter(a -> a.getDistrict() != null && !a.getDistrict().trim().isEmpty())
                        .findFirst()
                        .orElse(address);
                address = addressWithDistrict;
            }
            request.setAttribute("defaultAddress", address);
            request.setAttribute("defaultRecipientName", address.getRecipientName());
            request.setAttribute("defaultPhone", address.getPhone());
            request.setAttribute("defaultProvince", address.getProvince());
            request.setAttribute("defaultDistrict", address.getDistrict());
            request.setAttribute("defaultWard", address.getWard());
            request.setAttribute("defaultStreetAddress", address.getStreetAddress());
        }

        String orderSuccess = (String) session.getAttribute("orderSuccess");
        if (orderSuccess != null) {
            request.setAttribute("orderSuccess", orderSuccess);
            session.removeAttribute("orderSuccess");
        }
        String orderError = (String) session.getAttribute("orderError");
        if (orderError != null) {
            request.setAttribute("orderError", orderError);
            session.removeAttribute("orderError");
        }

        request.getRequestDispatcher("/frontend/nguoiB/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String[] selectedComicIds = request.getParameterValues("selectedComics");

        if (selectedComicIds == null || selectedComicIds.length == 0) {
            session.setAttribute("checkoutError", "Vui lòng chọn ít nhất một sản phẩm để thanh toán");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        @SuppressWarnings("unchecked")
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");

        if (cartItems == null || cartItems.isEmpty()) {
            System.out.println("Cart is empty!");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        List<CartItem> selectedItems = new ArrayList<>();
        double subtotal = 0;

        for (String comicId : selectedComicIds) {
            int id = Integer.parseInt(comicId);
            for (CartItem item : cartItems) {
                if (item.getComic().getId() == id) {
                    selectedItems.add(item);
                    subtotal += item.getFinalPrice() * item.getQuantity();
                    break;
                }
            }
        }

        double shippingFee = 25000;

        double totalAmount = subtotal + shippingFee;

        session.setAttribute("selectedItems", selectedItems);
        session.setAttribute("checkoutSubtotal", subtotal);
        session.setAttribute("shippingFee", shippingFee);
        session.setAttribute("checkoutTotal", totalAmount);

        response.sendRedirect(request.getContextPath() + "/checkout");
    }
}