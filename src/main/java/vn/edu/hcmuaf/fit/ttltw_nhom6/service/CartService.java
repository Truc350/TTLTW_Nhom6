package vn.edu.hcmuaf.fit.ttltw_nhom6.service;

import vn.edu.hcmuaf.fit.ttltw_nhom6.dao.CartDAO;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Cart;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.CartItem;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Comic;
import vn.edu.hcmuaf.fit.ttltw_nhom6.service.cache.CacheService;

import java.util.List;
import java.util.Optional;

public class CartService {

private final CartDAO cartDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
    }

    public List<CartItem> getCart(Cart cart, Integer userId, String sessionId) {
        Optional<Integer> cartIdOpt = resolveCartId(userId, sessionId, false);
        if (cartIdOpt.isEmpty()) return List.of();

        int cartId = cartIdOpt.get();
        cart.setId(cartId);
        if (CacheService.cartCache.containsKey(cartId)) {
            System.out.println("LOAD CART FROM CACHE");
            List<CartItem> items = CacheService.cartCache.get(cartId);
            cart.setItems(items);
            return items;
        }

        List<CartItem> items = cartDAO.getCartItems(cartId);

        if (items != null) {
            CacheService.cartCache.put(cartId, items);
        }

        cart.setItems(items);
        return items;
    }

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
        CacheService.cartCache.remove(cartId);
        refreshCart(cart, cartId);
        return "success";
    }

    public String updateCart(Cart cart, Integer userId, String sessionId,
                             int comicId, int quantity) {
        Optional<Integer> cartIdOpt = resolveCartId(userId, sessionId, false);
        if (cartIdOpt.isEmpty()) return "not_found";

        int cartId = cartIdOpt.get();

        CacheService.cartCache.remove(cartId);

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

    public boolean removeItem(Cart cart, Integer userId, String sessionId, int comicId) {
        Optional<Integer> cartIdOpt = resolveCartId(userId, sessionId, false);
        if (cartIdOpt.isEmpty()) return false;

        int cartId   = cartIdOpt.get();
        CacheService.cartCache.remove(cartId);
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
        CacheService.cartCache.remove(cartIdOpt.get());
        cartDAO.clearCartItems(cartIdOpt.get());
        cart.removeAllItems();
    }

    public void mergeCart(Cart cart, int userId, String guestSessionId) {
        cartDAO.mergeCart(guestSessionId, userId);
        getCart(cart, userId, null);
    }

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
        CacheService.cartCache.put(cartId, items);
        cart.setItems(items);
    }

    public void mergeSessionCartToDb(Cart userCart, int userId, Cart guestCart) {
        for (CartItem guestItem  : guestCart.getItems()) {
            int comicId = guestItem.getComic().getId();
            int quantity = guestItem.getQuantity();
            addToCart(userCart,userId, null, comicId, quantity);
        }
        getCart(userCart, userId, null);
    }
}