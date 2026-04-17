package vn.edu.hcmuaf.fit.ttltw_nhom6.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.ttltw_nhom6.db.JdbiConnector;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.CartItem;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Comic;

import java.time.LocalDateTime;
import java.util.List;
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
                   "SELECT id FROM cart "+
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
        System.out.println("[DEBUG] getOrCreateCartBySessionId called, sessionId=" + sessionId);
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

    public int removeItem(int cartId, int comicId) {
        return jdbi.withHandle(handle ->
                handle.createUpdate("DELETE FROM cart_item " +
                                "WHERE cart_id = :cartId AND comic_id = :comicId")
                        .bind("cartId",  cartId)
                        .bind("comicId", comicId)
                        .execute()
        );
    }

    public List<CartItem> getCartItems(int cartId) {
        return JdbiConnector.get().withHandle(handle ->
                handle.createQuery("""
                    SELECT
                        ci.id              AS cart_item_id,
                        ci.cart_id,
                        ci.quantity,
                        ci.price_at_purchase,
                        ci.flash_sale_price,
                        ci.flash_sale_id,
                        ci.created_at,
                        ci.updated_at,
                        c.id               AS comic_id,
                        c.name_comics,
                        c.author,
                        c.publisher,
                        c.price,
                        c.stock_quantity,
                        c.status,
                        c.thumbnail_url,
                        c.discount_percent,
                        COALESCE(c.damaged_quantity, 0) AS damaged_quantity
                    FROM cart_item ci
                    JOIN comics c ON ci.comic_id = c.id
                    WHERE ci.cart_id  = :cartId
                      AND c.status    = 'active'
                      AND c.is_hidden = 0
                      AND c.is_deleted = 0
                    ORDER BY ci.created_at DESC
                """)
                        .bind("cartId", cartId)
                        .map((rs, ctx) -> {
                            Comic comic = new Comic();
                            comic.setId(rs.getInt("comic_id"));
                            comic.setNameComics(rs.getString("name_comics"));
                            comic.setAuthor(rs.getString("author"));
                            comic.setPublisher(rs.getString("publisher"));
                            comic.setPrice(rs.getDouble("price"));
                            comic.setStockQuantity(rs.getInt("stock_quantity"));
                            comic.setStatus(rs.getString("status"));
                            comic.setThumbnailUrl(rs.getString("thumbnail_url"));
                            comic.setDiscountPercent(rs.getObject("discount_percent", Double.class));
                            comic.setDamagedQuantity(rs.getInt("damaged_quantity"));

                            return new CartItem(
                                    rs.getInt("cart_item_id"),
                                    rs.getInt("cart_id"),
                                    comic,
                                    rs.getInt("quantity"),
                                    rs.getDouble("price_at_purchase"),
                                    rs.getObject("flash_sale_price", Double.class),
                                    rs.getObject("flash_sale_id", Integer.class),
                                    rs.getObject("created_at", LocalDateTime.class),
                                    rs.getObject("updated_at", LocalDateTime.class)
                            );
                        })
                        .list()
        );
    }


    public void mergeCart(String sessionId, int userId) {
        jdbi.useHandle(handle -> {

            // Tìm cart guest
            Optional<Integer> guestCartIdOpt = handle
                    .createQuery(
                            "SELECT id FROM cart " +
                                    "WHERE session_id = :sid AND user_id IS NULL AND status = 'active' " +
                                    "LIMIT 1")
                    .bind("sid", sessionId)
                    .mapTo(Integer.class)
                    .findOne();

            if (guestCartIdOpt.isEmpty()) return;

            int guestCartId = guestCartIdOpt.get();
            int userCartId  = getOrCreateCartByUserId(userId);

            List<Map<String, Object>> guestItems = handle
                    .createQuery(
                            "SELECT comic_id, quantity, " +
                                    "       price_at_purchase, flash_sale_price, flash_sale_id " +
                                    "FROM cart_item WHERE cart_id = :cartId")
                    .bind("cartId", guestCartId)
                    .mapToMap()
                    .list();

            for (Map<String, Object> item : guestItems) {
                int     comicId         = ((Number) item.get("comic_id")).intValue();
                int     qty             = ((Number) item.get("quantity")).intValue();
                double  priceAtPurchase = ((Number) item.get("price_at_purchase")).doubleValue();
                Double  flashSalePrice  = item.get("flash_sale_price") != null
                        ? ((Number) item.get("flash_sale_price")).doubleValue()
                        : null;
                Integer flashSaleId     = item.get("flash_sale_id") != null
                        ? ((Number) item.get("flash_sale_id")).intValue()
                        : null;

                Map<String, Object> stock = handle
                        .createQuery(
                                "SELECT stock_quantity, COALESCE(damaged_quantity, 0) AS damaged_quantity " +
                                        "FROM comics WHERE id = :cid AND is_deleted = 0")
                        .bind("cid", comicId)
                        .mapToMap()
                        .findOne()
                        .orElse(null);

                if (stock == null) continue;

                int available = ((Number) stock.get("stock_quantity")).intValue()
                        - ((Number) stock.get("damaged_quantity")).intValue();

                handle.createUpdate(
                                "INSERT INTO cart_item " +
                                        "  (cart_id, comic_id, quantity, " +
                                        "   price_at_purchase, flash_sale_price, flash_sale_id, " +
                                        "   created_at, updated_at) " +
                                        "VALUES " +
                                        "  (:cartId, :comicId, LEAST(:qty, :available), " +
                                        "   :priceAtPurchase, :flashSalePrice, :flashSaleId, " +
                                        "   NOW(), NOW()) " +
                                        "ON DUPLICATE KEY UPDATE " +
                                        "  quantity   = LEAST(quantity + VALUES(quantity), :available), " +
                                        "  updated_at = NOW()")
                        .bind("cartId",          userCartId)
                        .bind("comicId",         comicId)
                        .bind("qty",             qty)
                        .bind("available",       available)

                        .bind("priceAtPurchase", priceAtPurchase)
                        .bind("flashSalePrice",  flashSalePrice)
                        .bind("flashSaleId",     flashSaleId)
                        .execute();
            }

            handle.createUpdate("DELETE FROM cart_item WHERE cart_id = :cartId")
                    .bind("cartId", guestCartId).execute();
            handle.createUpdate("DELETE FROM cart WHERE id = :cartId")
                    .bind("cartId", guestCartId).execute();
        });
    }

    public Optional<Integer> findCartIdByUserId(int userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT id FROM cart WHERE user_id = :uid AND status = 'active' LIMIT 1")
                        .bind("uid", userId)
                        .mapTo(Integer.class)
                        .findOne()
        );
    }

    public Optional<Integer> findCartIdBySessionId(String sessionId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT id FROM cart " +
                                        "WHERE session_id = :sid AND user_id IS NULL AND status = 'active' LIMIT 1")
                        .bind("sid", sessionId)
                        .mapTo(Integer.class)
                        .findOne()
        );
    }

    public int clearCartItems(int cartId) {
        return jdbi.withHandle(handle ->
                handle.createUpdate("DELETE FROM cart_item WHERE cart_id = :cartId")
                        .bind("cartId", cartId)
                        .execute()
        );
    }


}


