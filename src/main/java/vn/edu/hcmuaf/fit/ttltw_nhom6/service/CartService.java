package vn.edu.hcmuaf.fit.ttltw_nhom6.service;

import vn.edu.hcmuaf.fit.ttltw_nhom6.dao.CartDAO;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Cart;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.CartItem;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Comic;

import java.util.List;
import java.util.Optional;

public class CartService {
//    private final CartDAO cartDAO;
//    private final ComicService comicService;
//    public CartService() {
//        this.cartDAO      = new CartDAO();
//        this.comicService = new ComicService();
//    }
//    public String addToCart(Cart cart, Integer userId, String sessionId,
//                            int comicId, int quantity) {
//        System.out.println("[DEBUG] addToCart: userId=" + userId + ", sessionId=" + sessionId + ", comicId=" + comicId);
//
//        try {
//            Comic comic = comicService.getComicById(comicId);
//            if (comic == null
//                    || !"active".equalsIgnoreCase(comic.getStatus())
//                    || comic.getIsHidden() == 1
//                    || comic.isDeleted()) {
//                return "not_found";
//            }
//            int damagedQty = comic.getDamagedQuantity() != null ? comic.getDamagedQuantity() : 0;
//            int available  = comic.getStockQuantity() - damagedQty;
//            if (available <= 0) return "out_of_stock";
//            int cartId   = resolveCartId(userId, sessionId);
//            int affected = cartDAO.addItem(cartId, comicId, quantity);
//            if (affected == 0) return "error";
//            syncSessionCart(cart, userId, sessionId);
//            return "success";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "error";
//        }
//    }
//    public List<CartItem> getCart(Cart cart, Integer userId, String sessionId) {
//        try {
//            int cartId = resolveCartId(userId, sessionId);
//            List<CartItem> items = cartDAO.getCartItems(cartId);
//            cart.setItems(items);
//            return items;
//        } catch (Exception e) {
//            e.printStackTrace();
//            cart.setItems(List.of());
//            return List.of();
//        }
//    }
//    public String updateCart(Cart cart, Integer userId, String sessionId,
//                             int comicId, int newQuantity) {
//        try {
//            int cartId = resolveCartId(userId, sessionId);
//            Optional<Integer> existingCartId = (userId != null)
//                    ? cartDAO.findCartIdByUserId(userId)
//                    : cartDAO.findCartIdBySessionId(sessionId);
//
//            if (existingCartId.isEmpty()) return "not_in_cart";
//            Comic comic = comicService.getComicById(comicId);
//            if (comic != null) {
//                int damagedQty = comic.getDamagedQuantity() != null ? comic.getDamagedQuantity() : 0;
//                int available  = comic.getStockQuantity() - damagedQty;
//                if (newQuantity > available) return "out_of_stock";
//            }
//
//            int affected = cartDAO.updateQuantity(cartId, comicId, newQuantity);
//            if (affected == 0) return "not_in_cart";
//            syncSessionCart(cart, userId, sessionId);
//            return "success";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "error";
//        }
//    }
//    public boolean removeItem(Cart cart, Integer userId, String sessionId, int comicId) {
//        try {
//            int cartId   = resolveCartId(userId, sessionId);
//            int affected = cartDAO.removeItem(cartId, comicId);
//            if (affected > 0) {
//                cart.removeItem(comicId);
//                return true;
//            }
//            return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    public boolean clearCart(Cart cart, Integer userId, String sessionId) {
//        try {
//            int cartId   = resolveCartId(userId, sessionId);
//            cartDAO.clearCartItems(cartId);
//            cart.removeAllItems();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    public void mergeCart(Cart cart, int userId, String guestSessionId) {
//        try {
//            System.out.println("[DEBUG] mergeCart: userId=" + userId + ", guestSessionId=" + guestSessionId);
//            cartDAO.mergeCart(guestSessionId, userId);
//            int cartId = cartDAO.getOrCreateCartByUserId(userId);
//            List<CartItem> items = cartDAO.getCartItems(cartId);
//            System.out.println("[DEBUG] mergeCart: items loaded = " + items.size());
//            cart.setItems(items);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    private int resolveCartId(Integer userId, String sessionId) {
//        if (userId != null) return cartDAO.getOrCreateCartByUserId(userId);
//        return cartDAO.getOrCreateCartBySessionId(sessionId);
//    }
//    private void syncSessionCart(Cart cart, Integer userId, String sessionId) {
//        try {
//            int cartId = resolveCartId(userId, sessionId);
//            List<CartItem> items = cartDAO.getCartItems(cartId);
//            cart.setItems(items);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
private final CartDAO cartDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
    }

    // ------------------------------------------------------------------ //
    //  Lay gio hang tu DB                                                 //
    // ------------------------------------------------------------------ //

    public List<CartItem> getCart(Cart cart, Integer userId, String sessionId) {
        Optional<Integer> cartIdOpt = resolveCartId(userId, sessionId, false);
        if (cartIdOpt.isEmpty()) return List.of();

        int cartId = cartIdOpt.get();
        cart.setId(cartId);
        List<CartItem> items = cartDAO.getCartItems(cartId);
        cart.setItems(items);
        return items;
    }

    // ------------------------------------------------------------------ //
    //  Them san pham                                                      //
    //  @return "success" | "out_of_stock" | "not_found"                  //
    // ------------------------------------------------------------------ //

    public String addToCart(Cart cart, Integer userId, String sessionId,
                            int comicId, int quantity) {
        int cartId = resolveOrCreateCartId(userId, sessionId);
        cart.setId(cartId);
        System.out.println("[CartService] addToCart: cartId=" + cartId
                + " userId=" + userId + " sessionId=" + sessionId
                + " comicId=" + comicId + " qty=" + quantity);
        int affected = cartDAO.addItem(cartId, comicId, quantity);
        System.out.println("[CartService] addItem returned: " + affected);
        if (affected < 0) return "not_found";
        if (affected == 0) return "out_of_stock";

        refreshCart(cart, cartId);
        return "success";
    }

    // ------------------------------------------------------------------ //
    //  Cap nhat so luong                                                  //
    //  @return "success" | "out_of_stock" | "not_found"                  //
    // ------------------------------------------------------------------ //

    public String updateCart(Cart cart, Integer userId, String sessionId,
                             int comicId, int quantity) {
        Optional<Integer> cartIdOpt = resolveCartId(userId, sessionId, false);
        if (cartIdOpt.isEmpty()) return "not_found";

        int cartId = cartIdOpt.get();

        CartItem existing = cart.get(comicId);
        if (existing == null) return "not_found";

        int affected = cartDAO.updateQuantity(cartId, comicId, quantity);
        if (affected == 0) return "not_found";

        refreshCart(cart, cartId);

        CartItem updated = cart.get(comicId);
        if (updated != null && updated.getQuantity() < quantity) {
            return "out_of_stock";
        }
        return "success";
    }

    public boolean updateQuantity(Cart cart, Integer userId, String sessionId,
                                  int comicId, int newQuantity) {
        return "success".equals(updateCart(cart, userId, sessionId, comicId, newQuantity));
    }

    // ------------------------------------------------------------------ //
    //  Xoa san pham                                                       //
    // ------------------------------------------------------------------ //

    public boolean removeItem(Cart cart, Integer userId, String sessionId, int comicId) {
        Optional<Integer> cartIdOpt = resolveCartId(userId, sessionId, false);
        if (cartIdOpt.isEmpty()) return false;

        int cartId   = cartIdOpt.get();
        int affected = cartDAO.removeItem(cartId, comicId);
        if (affected > 0) {
            refreshCart(cart, cartId);
            return true;
        }
        return false;
    }

    public void clearCart(Cart cart, Integer userId, String sessionId) {
        Optional<Integer> cartIdOpt = resolveCartId(userId, sessionId, false);
        if (cartIdOpt.isEmpty()) return;

        cartDAO.clearCartItems(cartIdOpt.get());
        cart.removeAllItems();
    }

    // ------------------------------------------------------------------ //
    //  Merge guest cart -> user cart khi dang nhap                       //
    // ------------------------------------------------------------------ //

    public void mergeCart(Cart cart, int userId, String guestSessionId) {
        cartDAO.mergeCart(guestSessionId, userId);
        getCart(cart, userId, null);
    }

    // ------------------------------------------------------------------ //
    //  Helper                                                             //
    // ------------------------------------------------------------------ //

    private Optional<Integer> resolveCartId(Integer userId, String sessionId,
                                            boolean createIfAbsent) {
        if (userId != null) {
            Optional<Integer> opt = cartDAO.findCartIdByUserId(userId);
            if (opt.isPresent() || !createIfAbsent) return opt;
            return Optional.of(cartDAO.getOrCreateCartByUserId(userId));
        } else {
            Optional<Integer> opt = cartDAO.findCartIdBySessionId(sessionId);
            if (opt.isPresent() || !createIfAbsent) return opt;
            return Optional.of(cartDAO.getOrCreateCartBySessionId(sessionId));
        }
    }

    private int resolveOrCreateCartId(Integer userId, String sessionId) {
        return userId != null
                ? cartDAO.getOrCreateCartByUserId(userId)
                : cartDAO.getOrCreateCartBySessionId(sessionId);
    }

    private void refreshCart(Cart cart, int cartId) {
        cart.setId(cartId);
        List<CartItem> items = cartDAO.getCartItems(cartId);
        System.out.println("[CartService] refreshCart: cartId=" + cartId
                + " items loaded=" + items.size());
        cart.setItems(items);
    }
}