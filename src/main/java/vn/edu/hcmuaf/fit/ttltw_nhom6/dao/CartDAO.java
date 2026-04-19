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
            Optional<Integer> cartId = handle
                    .createQuery("SELECT id FROM cart " +
                            "WHERE user_id = :uid AND status = 'active' LIMIT 1")
                    .bind("uid", userId)
                    .mapTo(Integer.class)
                    .findOne();
            if (cartId.isPresent()) return cartId.get();

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
                    .createQuery("SELECT id FROM cart " +
                            "WHERE session_id = :sid AND user_id IS NULL AND status = 'active' LIMIT 1")
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
                                    "  AND c.status    = 'available' " +
                                    "  AND c.is_hidden = 0 " +
                                    "  AND c.is_deleted = 0 " +
                                    "LIMIT 1")
                    .bind("comicId", comicId)
                    .mapToMap()
                    .findOne()
                    .orElse(null);
            System.out.println("[CartDAO] addItem called: cartId=" + cartId
                    + " comicId=" + comicId + " quantity=" + quantity);
            System.out.println("[CartDAO] comicInfo=" + comicInfo);
            // San pham khong ton tai / bi an / bi xoa
            if (comicInfo == null) {
                System.out.println("[CartDAO] comicInfo NULL -> return -1");
                return -1;
            }

            double priceAtPurchase = ((Number) comicInfo.get("price")).doubleValue();
            int stockQty = ((Number) comicInfo.get("stock_quantity")).intValue();
            int damagedQty = ((Number) comicInfo.get("damaged_quantity")).intValue();
            int available = stockQty - damagedQty;
            System.out.println("[CartDAO] available=" + available
                    + " price=" + priceAtPurchase);
            // Het hang hoan toan
            if (available <= 0) return 0;

            Integer flashSaleId = null;
            Double flashSalePrice = null;
            if (comicInfo.get("flash_sale_id") != null) {
                flashSaleId = ((Number) comicInfo.get("flash_sale_id")).intValue();
                double discount = ((Number) comicInfo.get("discount_percent")).doubleValue();
                flashSalePrice = priceAtPurchase * (1 - discount / 100.0);
            }

            int affected = handle.createUpdate(
                            "INSERT INTO cart_item " +
                                    "  (cart_id, comic_id, quantity, " +
                                    "   price_at_purchase, flash_sale_price, flash_sale_id, " +
                                    "   created_at, updated_at) " +
                                    "VALUES " +
                                    "  (:cartId, :comicId, LEAST(:qty, :available), " +
                                    "   :priceAtPurchase, :flashSalePrice, :flashSaleId, " +
                                    "   NOW(), NOW()) " +
                                    "ON DUPLICATE KEY UPDATE " +
                                    "  quantity         = LEAST(quantity + VALUES(quantity), :available), " +
                                    "  flash_sale_price = VALUES(flash_sale_price), " +
                                    "  flash_sale_id    = VALUES(flash_sale_id), " +
                                    "  updated_at       = NOW()")
                    .bind("cartId", cartId)
                    .bind("comicId", comicId)
                    .bind("qty", quantity)
                    .bind("available", available)
                    .bind("priceAtPurchase", priceAtPurchase)
                    .bind("flashSalePrice", flashSalePrice)
                    .bind("flashSaleId", flashSaleId)
                    .execute();
            System.out.println("[CartDAO] INSERT affected=" + affected);
            return affected;
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
            int safeQty = Math.max(1, Math.min(newQuantity, available));

            return handle.createUpdate(
                            "UPDATE cart_item " +
                                    "SET quantity = :qty, updated_at = NOW() " +
                                    "WHERE cart_id = :cartId AND comic_id = :comicId")
                    .bind("qty", safeQty)
                    .bind("cartId", cartId)
                    .bind("comicId", comicId)
                    .execute();
        });
    }

    public int removeItem(int cartId, int comicId) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(
                                "DELETE FROM cart_item WHERE cart_id = :cartId AND comic_id = :comicId")
                        .bind("cartId", cartId)
                        .bind("comicId", comicId)
                        .execute()
        );
    }

    public List<CartItem> getCartItems(int cartId) {
        System.out.println("[CartDAO] getCartItems called: cartId=" + cartId);
        return jdbi.withHandle(handle -> {
            List<CartItem> items =
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
                                      AND c.status    = 'available'
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
                        .list();

            System.out.println("[CartDAO] getCartItems result size=" + items.size()); // THÊM
            return items;
        });
    }


    public void mergeCart(String sessionId, int userId) {
        jdbi.useHandle(handle -> {

            // Tim guest cart theo sessionId
            Optional<Integer> guestCartIdOpt = handle
                    .createQuery(
                            "SELECT id FROM cart " +
                                    "WHERE session_id = :sid AND user_id IS NULL AND status = 'active' LIMIT 1")
                    .bind("sid", sessionId)
                    .mapTo(Integer.class)
                    .findOne();

            if (guestCartIdOpt.isEmpty()) return; // Khong co guest cart, bo qua

            int guestCartId = guestCartIdOpt.get();
            int userCartId = getOrCreateCartByUserId(userId);

            // Lay items tu guest cart (chi lay san pham con active)
            List<Map<String, Object>> guestItems = handle
                    .createQuery(
                            "SELECT ci.comic_id, ci.quantity, " +
                                    "       ci.price_at_purchase, ci.flash_sale_price, ci.flash_sale_id, " +
                                    "       c.stock_quantity, COALESCE(c.damaged_quantity,0) AS damaged_quantity " +
                                    "FROM cart_item ci " +
                                    "JOIN comics c ON ci.comic_id = c.id " +
                                    "WHERE ci.cart_id = :cartId " +
                                    "  AND c.status = 'available' AND c.is_hidden = 0 AND c.is_deleted = 0")
                    .bind("cartId", guestCartId)
                    .mapToMap()
                    .list();

            for (Map<String, Object> item : guestItems) {
                int comicId = ((Number) item.get("comic_id")).intValue();
                int qty = ((Number) item.get("quantity")).intValue();
                double priceAtPurchase = ((Number) item.get("price_at_purchase")).doubleValue();
                int stockQty = ((Number) item.get("stock_quantity")).intValue();
                int damagedQty = ((Number) item.get("damaged_quantity")).intValue();
                int available = stockQty - damagedQty;

                if (available <= 0) continue; // Bo qua san pham het hang

                // Kiem tra flash sale con active khong
                Double flashSalePrice = null;
                Integer flashSaleId = null;
                if (item.get("flash_sale_id") != null) {
                    Integer fsId = ((Number) item.get("flash_sale_id")).intValue();
                    Optional<Map<String, Object>> activeFs = handle
                            .createQuery(
                                    "SELECT id FROM flashsale " +
                                            "WHERE id = :fsId AND status = 'active' " +
                                            "  AND NOW() BETWEEN start_time AND end_time " +
                                            "  AND is_deleted = 0 LIMIT 1")
                            .bind("fsId", fsId)
                            .mapToMap()
                            .findOne();
                    if (activeFs.isPresent()) {
                        flashSaleId = fsId;
                        flashSalePrice = item.get("flash_sale_price") != null
                                ? ((Number) item.get("flash_sale_price")).doubleValue()
                                : null;
                    }
                    // Neu flash sale het han -> de null (dung gia thuong)
                }

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
                        .bind("cartId", userCartId)
                        .bind("comicId", comicId)
                        .bind("qty", qty)
                        .bind("available", available)
                        .bind("priceAtPurchase", priceAtPurchase)
                        .bind("flashSalePrice", flashSalePrice)
                        .bind("flashSaleId", flashSaleId)
                        .execute();
            }

            // Xoa guest cart sau khi da merge xong
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

    public int checkout(int cartId, int userId, int shippingAddressId,
                        String recipientName, String shippingPhone,
                        String shippingAddress, String shippingProvider,
                        double shippingFee, int pointsUsed) throws Exception {

        return jdbi.inTransaction(handle -> {

            // 1.Load cart items
            List<Map<String, Object>> items = handle.createQuery(
                            "SELECT ci.quantity, ci.price_at_purchase, ci.flash_sale_price, ci.flash_sale_id, " +
                                    "       c.id AS comic_id, c.name_comics " +
                                    "FROM cart_item ci " +
                                    "JOIN comics c ON ci.comic_id = c.id " +
                                    "WHERE ci.cart_id = :cartId " +
                                    "  AND c.status    = 'available' " +
                                    "  AND c.is_hidden = 0 " +
                                    "  AND c.is_deleted = 0")
                    .bind("cartId", cartId)
                    .mapToMap()
                    .list();

            if (items.isEmpty()) throw new Exception("Giỏ hàng trống!");

            // 2. Lock đúng rows comics (FOR UPDATE riêng)
            List<Integer> comicIds = items.stream()
                    .map(i -> ((Number) i.get("comic_id")).intValue())
                    .collect(java.util.stream.Collectors.toList());

            String inClause = comicIds.stream()
                    .map(String::valueOf)
                    .collect(java.util.stream.Collectors.joining(","));

            List<Map<String, Object>> lockedStocks = handle.createQuery(
                            "SELECT id AS comic_id, " +
                                    "       stock_quantity, " +
                                    "       COALESCE(damaged_quantity, 0) AS damaged_quantity " +
                                    "FROM comics " +
                                    "WHERE id IN (" + inClause + ") " +
                                    "FOR UPDATE")  // ← lock đúng rows comics
                    .mapToMap()
                    .list();

            Map<Integer, Map<String, Object>> stockMap = new java.util.HashMap<>();
            for (Map<String, Object> row : lockedStocks) {
                stockMap.put(((Number) row.get("comic_id")).intValue(), row);
            }

            // 3. Kiểm tra flash sale còn hạn không
            List<Integer> flashSaleIds = items.stream()
                    .filter(i -> i.get("flash_sale_id") != null)
                    .map(i -> ((Number) i.get("flash_sale_id")).intValue())
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());

            java.util.Set<Integer> activeFlashSaleIds = new java.util.HashSet<>();
            if (!flashSaleIds.isEmpty()) {
                String fsInClause = flashSaleIds.stream()
                        .map(String::valueOf)
                        .collect(java.util.stream.Collectors.joining(","));

                handle.createQuery(
                                "SELECT id FROM flashsale " +
                                        "WHERE id IN (" + fsInClause + ") " +
                                        "  AND status = 'active' " +
                                        "  AND NOW() BETWEEN start_time AND end_time " +
                                        "  AND is_deleted = 0")
                        .mapTo(Integer.class)
                        .list()
                        .forEach(activeFlashSaleIds::add);
            }

            // 4. Validate stock + trừ kho
            for (Map<String, Object> item : items) {
                int comicId = ((Number) item.get("comic_id")).intValue();
                int qty     = ((Number) item.get("quantity")).intValue();
                String name = (String) item.get("name_comics");

                Map<String, Object> stock = stockMap.get(comicId);
                if (stock == null) {
                    throw new Exception("Không tìm thấy sản phẩm \"" + name + "\"!");
                }

                int stockQty   = ((Number) stock.get("stock_quantity")).intValue();
                int damagedQty = ((Number) stock.get("damaged_quantity")).intValue();
                int available  = stockQty - damagedQty;

                if (available < qty) {
                    throw new Exception("Sản phẩm \"" + name + "\" chỉ còn " + available + " cuốn!");
                }

                // Trừ kho — có điều kiện chống oversell (rowsAffected = 0 => hết hàng)
                int rowsAffected = handle.createUpdate(
                                "UPDATE comics " +
                                        "SET stock_quantity = stock_quantity - :qty " +
                                        "WHERE id = :comicId " +
                                        "  AND (stock_quantity - COALESCE(damaged_quantity, 0)) >= :qty")
                        .bind("qty",     qty)
                        .bind("comicId", comicId)
                        .execute();

                // Double-check: nếu rowsAffected = 0 => có race condition xảy ra
                if (rowsAffected == 0) {
                    throw new Exception("Sản phẩm \"" + name + "\" vừa hết hàng, vui lòng thử lại!");
                }
            }
// GỌI thêm INSERT INTO inventory_transaction Ở ĐÂY

            // 5.  Tính tổng tiền
            double subtotal = 0;
            for (Map<String, Object> item : items) {
                int qty = ((Number) item.get("quantity")).intValue();

                // Chỉ dùng flash_sale_price nếu flash sale còn hạn
                double unitPrice;
                Integer flashSaleId = item.get("flash_sale_id") != null
                        ? ((Number) item.get("flash_sale_id")).intValue() : null;

                if (flashSaleId != null && activeFlashSaleIds.contains(flashSaleId)
                        && item.get("flash_sale_price") != null) {
                    unitPrice = ((Number) item.get("flash_sale_price")).doubleValue(); // giá sale
                } else {
                    unitPrice = ((Number) item.get("price_at_purchase")).doubleValue(); // giá gốc
                }

                subtotal += unitPrice * qty;
            }
            double totalAmount = subtotal + shippingFee;

            // 6. Tạo order
            int orderId = handle.createUpdate(
                            "INSERT INTO orders " +
                                    "  (user_id, status, total_amount, shipping_address_id, " +
                                    "   recipient_name, shipping_phone, shipping_address, " +
                                    "   shipping_provider, shipping_fee, points_used, order_date, created_at) " +
                                    "VALUES " +
                                    "  (:userId, 'pending', :totalAmount, :shippingAddressId, " +
                                    "   :recipientName, :shippingPhone, :shippingAddress, " +
                                    "   :shippingProvider, :shippingFee, :pointsUsed, NOW(), NOW())")
                    .bind("userId",            userId)
                    .bind("totalAmount",       totalAmount)
                    .bind("shippingAddressId", shippingAddressId)
                    .bind("recipientName",     recipientName)
                    .bind("shippingPhone",     shippingPhone)
                    .bind("shippingAddress",   shippingAddress)
                    .bind("shippingProvider",  shippingProvider)
                    .bind("shippingFee",       shippingFee)
                    .bind("pointsUsed",        pointsUsed)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();

            // 7. Tạo order_items
            for (Map<String, Object> item : items) {
                int comicId = ((Number) item.get("comic_id")).intValue();
                int qty     = ((Number) item.get("quantity")).intValue();

                Integer flashSaleId = item.get("flash_sale_id") != null
                        ? ((Number) item.get("flash_sale_id")).intValue() : null;

                double finalPrice;
                if (flashSaleId != null && activeFlashSaleIds.contains(flashSaleId)
                        && item.get("flash_sale_price") != null) {
                    finalPrice = ((Number) item.get("flash_sale_price")).doubleValue();
                } else {
                    finalPrice = ((Number) item.get("price_at_purchase")).doubleValue();
                    flashSaleId = null; // flash sale hết hạn → lưu null vào order_items
                }

                handle.createUpdate(
                                "INSERT INTO order_items " +
                                        "  (order_id, comic_id, quantity, price_at_purchase, flashsale_id) " +
                                        "VALUES " +
                                        "  (:orderId, :comicId, :qty, :price, :flashSaleId)")
                        .bind("orderId",     orderId)
                        .bind("comicId",     comicId)
                        .bind("qty",         qty)
                        .bind("price",       finalPrice)
                        .bind("flashSaleId", flashSaleId)
                        .execute();
            }

            // 8. Đánh dấu cart checked_out
            handle.createUpdate(
                            "UPDATE cart SET status = 'checked_out', updated_at = NOW() " +
                                    "WHERE id = :cartId")
                    .bind("cartId", cartId)
                    .execute();

            return orderId;
        });
    }



}


