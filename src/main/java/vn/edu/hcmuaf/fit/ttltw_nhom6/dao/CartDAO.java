package vn.edu.hcmuaf.fit.ttltw_nhom6.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.ttltw_nhom6.db.JdbiConnector;

import java.util.Map;
import java.util.Optional;

public class CartDAO {
    private final Jdbi jdbi;

    public CartDAO() {
        this.jdbi = JdbiConnector.get();
    }
    public int getOrCreateCartByUserId(int userId) {
        return jdbi.withHandle(handle -> {
           Optional<Integer> cartId = handle.createQuery(
                   "SELECT  id FROM cart"+
                           "WHERE user_id = :uid AND status = 'active' "+
                           "LIMIT 1").bind("uid", userId).mapTo(Integer.class).findOne();
           if (cartId.isPresent()) return  cartId.get();
            return handle.createUpdate(
                            "INSERT INTO cart (user_id, session_id, status, created_at, updated_at) " +
                                    "VALUES (:uid, NULL, 'active', NOW(), NOW())")
                    .bind("uid", userId)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();
        });
    }
    public int getOrCreateCartBySessionId(String sessionId) {
        return jdbi.withHandle(handle -> {
            Optional<Integer> cartId = handle
                    .createQuery(
                            "SELECT id FROM cart " +
                                    "WHERE session_id = :sid AND user_id IS NULL AND status = 'active' " +
                                    "LIMIT 1")
                    .bind("sid", sessionId)
                    .mapTo(Integer.class)
                    .findOne();

            if (cartId.isPresent()) return cartId.get();

            return handle.createUpdate(
                            "INSERT INTO cart (user_id, session_id, status, created_at, updated_at) " +
                                    "VALUES (NULL, :sid, 'active', NOW(), NOW())")
                    .bind("sid", sessionId)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();
        });
    }
    public int addItem(int cartId, int comicId, int quantity) {
        return jdbi.withHandle(handle -> {
            // Lấy giá gốc, tồn kho và Flash Sale
            Map<String, Object> comicInfo = handle
                    .createQuery(
                            "SELECT c.price, " +
                                    "       c.stock_quantity, " +
                                    "       COALESCE(c.damaged_quantity, 0) AS damaged_quantity, " +
                                    "       fs.id              AS flash_sale_id, " +
                                    "       fs.discount_percent " +
                                    "FROM comics c " +
                                    "LEFT JOIN flashsale fs " +
                                    "       ON fs.status = 'active' " +
                                    "      AND NOW() BETWEEN fs.start_time AND fs.end_time " +
                                    "      AND fs.is_deleted = 0 " +
                                    "WHERE c.id        = :comicId " +
                                    "  AND c.status    = 'active' " +
                                    "  AND c.is_hidden = 0 " +
                                    "  AND c.is_deleted = 0 " +
                                    "LIMIT 1")
                    .bind("comicId", comicId)
                    .mapToMap()
                    .findOne()
                    .orElse(null);

            if (comicInfo == null) return 0; // sản phẩm không hợp lệ
            double priceAtPurchase = ((Number) comicInfo.get("price")).doubleValue();
            int    stockQty        = ((Number) comicInfo.get("stock_quantity")).intValue();
            int    damagedQty      = ((Number) comicInfo.get("damaged_quantity")).intValue();
            int    available       = stockQty - damagedQty;
            // Tính flash_sale_price nếu đang có Flash Sale
            Integer flashSaleId    = null;
            Double  flashSalePrice = null;
            if (comicInfo.get("flash_sale_id") != null) {
                flashSaleId    = ((Number) comicInfo.get("flash_sale_id")).intValue();
                double discount = ((Number) comicInfo.get("discount_percent")).doubleValue();
                flashSalePrice  = priceAtPurchase * (1 - discount / 100.0);
            }
            return handle.createUpdate(
                            "INSERT INTO cart_item " +
                                    "  (cart_id, comic_id, quantity, " +
                                    "   price_at_purchase, flash_sale_price, flash_sale_id, " +
                                    "   created_at, updated_at) " +
                                    "VALUES " +
                                    "  (:cartId, :comicId, " +
                                    "   LEAST(:qty, :available), " +
                                    "   :priceAtPurchase, :flashSalePrice, :flashSaleId, " +
                                    "   NOW(), NOW()) " +
                                    "ON DUPLICATE KEY UPDATE " +
                                    "  quantity         = LEAST(quantity + VALUES(quantity), :available), " +
                                    "  flash_sale_price = VALUES(flash_sale_price), " +
                                    "  flash_sale_id    = VALUES(flash_sale_id), " +
                                    "  updated_at       = NOW()")
                    .bind("cartId",          cartId)
                    .bind("comicId",         comicId)
                    .bind("qty",             quantity)
                    .bind("available",       available)
                    .bind("priceAtPurchase", priceAtPurchase)
                    .bind("flashSalePrice",  flashSalePrice)
                    .bind("flashSaleId",     flashSaleId)
                    .execute();
        });
    }
    public int updateQuantity(int cartId, int comicId, int newQuantity) {
        return jdbi.withHandle(handle -> {

            Map<String, Object> stock = handle
                    .createQuery(
                            "SELECT stock_quantity, COALESCE(damaged_quantity, 0) AS damaged_quantity " +
                                    "FROM comics WHERE id = :comicId AND is_deleted = 0")
                    .bind("comicId", comicId)
                    .mapToMap()
                    .findOne()
                    .orElse(null);

            if (stock == null) return 0;

            int available = ((Number) stock.get("stock_quantity")).intValue()
                    - ((Number) stock.get("damaged_quantity")).intValue();
            int safeQty   = Math.max(1, Math.min(newQuantity, available));

            return handle.createUpdate(
                            "UPDATE cart_item " +
                                    "SET quantity = :qty, updated_at = NOW() " +
                                    "WHERE cart_id = :cartId AND comic_id = :comicId")
                    .bind("qty",     safeQty)
                    .bind("cartId",  cartId)
                    .bind("comicId", comicId)
                    .execute();
        });
    }

}


