package vn.edu.hcmuaf.fit.ttltw_nhom6.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.ltw_nhom5.db.JdbiConnector;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDAO {
    private final Jdbi jdbi;

    public ReportDAO() {
        this.jdbi = JdbiConnector.get();
    }

    public ReportDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
    }


    public Map<String, Object> getOverviewStats(LocalDate startDate, LocalDate endDate) {
        return jdbi.withHandle(handle -> {
            Map<String, Object> stats = new HashMap<>();


            Double totalRevenue = handle.createQuery(
                            "SELECT COALESCE(SUM(total_amount), 0) " +
                                    "FROM orders " +
                                    "WHERE order_date IS NOT NULL " +
                                    "AND DATE(order_date) BETWEEN :start AND :end"
                    )
                    .bind("start", startDate)
                    .bind("end", endDate)
                    .mapTo(Double.class)
                    .one();


            Integer orderCount = handle.createQuery(
                            "SELECT COUNT(*) " +
                                    "FROM orders " +
                                    "WHERE order_date IS NOT NULL " +
                                    "AND DATE(order_date) BETWEEN :start AND :end"
                    )
                    .bind("start", startDate)
                    .bind("end", endDate)
                    .mapTo(Integer.class)
                    .one();


            Double avgOrderValue = orderCount > 0 ? totalRevenue / orderCount : 0.0;

            Map<String, Object> topProduct = handle.createQuery(
                            "SELECT c.name_comics, SUM(oi.quantity) as total_sold " +
                                    "FROM order_items oi " +
                                    "JOIN comics c ON oi.comic_id = c.id " +
                                    "JOIN orders o ON oi.order_id = o.id " +
                                    "WHERE o.order_date IS NOT NULL " +
                                    "AND DATE(o.order_date) BETWEEN :start AND :end " +
                                    "GROUP BY c.id, c.name_comics " +
                                    "ORDER BY total_sold DESC " +
                                    "LIMIT 1"
                    )
                    .bind("start", startDate)
                    .bind("end", endDate)
                    .mapToMap()
                    .findFirst()
                    .orElse(null);

            stats.put("revenue", totalRevenue);
            stats.put("totalOrders", orderCount);
            stats.put("avgOrderValue", avgOrderValue);
            stats.put("bestProduct", topProduct != null ? topProduct.get("name_comics") : "Chưa có dữ liệu");

            return stats;
        });
    }


    public List<Map<String, Object>> getDailyRevenue(LocalDate startDate, LocalDate endDate) {
        return jdbi.withHandle(handle -> {
            List<Map<String, Object>> result = handle.createQuery(
                            "SELECT DATE(order_date) as date, " +
                                    "COALESCE(SUM(total_amount), 0) as revenue, " +
                                    "COUNT(*) as order_count " +
                                    "FROM orders " +
                                    "WHERE order_date IS NOT NULL " +
                                    "AND DATE(order_date) BETWEEN :start AND :end " +
                                    "GROUP BY DATE(order_date) " +
                                    "ORDER BY date ASC"
                    )
                    .bind("start", startDate)
                    .bind("end", endDate)
                    .mapToMap()
                    .list();

            return result;
        });
    }

    /**
     * Lấy top sản phẩm bán chạy
     */
    public List<Map<String, Object>> getTopSellingProducts(LocalDate startDate, LocalDate endDate, int limit) {
        return jdbi.withHandle(handle -> {
            List<Map<String, Object>> result = handle.createQuery(
                            "SELECT c.id, c.name_comics, " +
                                    "SUM(oi.quantity) as total_sold, " +
                                    "SUM(oi.quantity * oi.price_at_purchase) as total_revenue " +
                                    "FROM order_items oi " +
                                    "JOIN comics c ON oi.comic_id = c.id " +
                                    "JOIN orders o ON oi.order_id = o.id " +
                                    "WHERE o.order_date IS NOT NULL " +
                                    "AND DATE(o.order_date) BETWEEN :start AND :end " +
                                    "GROUP BY c.id, c.name_comics " +
                                    "ORDER BY total_sold DESC " +
                                    "LIMIT :limit"
                    )
                    .bind("start", startDate)
                    .bind("end", endDate)
                    .bind("limit", limit)
                    .mapToMap()
                    .list();

            return result;
        });
    }
}