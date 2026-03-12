package vn.edu.hcmuaf.fit.ttltw_nhom6.dao;

import vn.edu.hcmuaf.fit.ttltw_nhom6.model.FlashSale;

import java.time.LocalDateTime;
import java.util.List;

public class FlashSaleDAO extends ADao {
    public FlashSale getActiveFlashSaleEndingSoon() {
        String sql = """
                    SELECT *
                    FROM FlashSale
                    WHERE status = 'active'
                      AND start_time <= NOW()
                      AND end_time >= NOW()
                    ORDER BY end_time ASC
                    LIMIT 1
                """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(FlashSale.class)
                        .findOne()
                        .orElse(null)
        );
    }


    public int insert(FlashSale flashSale) {
        String sql = """
                    INSERT INTO FlashSale (name, discount_percent, start_time, end_time, status)
                    VALUES (:name, :discountPercent, :startTime, :endTime, :status)
                """;

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(flashSale)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(int.class)
                        .one()
        );
    }

    public List<FlashSale> getAllFlashSales() {

        updateStatuses();

        String sql = """
                    SELECT id, name, discount_percent, start_time, end_time, status, created_at
                    FROM FlashSale
                    ORDER BY created_at ASC
                """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(FlashSale.class)
                        .list()
        );
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM FlashSale WHERE id = ?";

        String deleteLinksSql = "DELETE FROM FlashSale_Comics WHERE flashsale_id = ?";

        return jdbi.withHandle(handle -> {
            handle.createUpdate(deleteLinksSql)
                    .bind(0, id)
                    .execute();

            int rows = handle.createUpdate(sql)
                    .bind(0, id)
                    .execute();
            return rows > 0;
        });
    }

    public FlashSale getById(int id) {

        updateStatuses();

        String sql = "SELECT * FROM FlashSale WHERE id = ?";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, id)
                        .mapToBean(FlashSale.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public boolean updateFlashSale(int id, String name, double discountPercent, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = """
                    UPDATE FlashSale 
                    SET name = :name,
                        discount_percent = :discountPercent,
                        start_time = :startTime,
                        end_time = :endTime
                    WHERE id = :id
                """;

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("name", name)
                        .bind("discountPercent", discountPercent)
                        .bind("startTime", startTime)
                        .bind("endTime", endTime)
                        .bind("id", id)
                        .execute() > 0
        );
    }

    public void updateStatuses() {
        String sql = """
                    UPDATE FlashSale
                    SET status = CASE
                        WHEN NOW() < start_time THEN 'scheduled'
                        WHEN NOW() >= start_time AND NOW() <= end_time THEN 'active'
                        WHEN NOW() > end_time THEN 'ended'
                    END
                    WHERE status != CASE
                        WHEN NOW() < start_time THEN 'scheduled'
                        WHEN NOW() >= start_time AND NOW() <= end_time THEN 'active'
                        WHEN NOW() > end_time THEN 'ended'
                    END
                """;

        jdbi.useHandle(handle -> handle.createUpdate(sql).execute());
    }

    /**
     * Lấy danh sách Flash Sale đang active và sắp diễn ra
     * Sắp xếp theo thời gian bắt đầu
     */
    public List<FlashSale> getUpcomingAndActiveFlashSales() {

        updateStatuses();

        String sql = """
                    SELECT *
                    FROM FlashSale
                    WHERE status IN ('active', 'scheduled')
                    ORDER BY start_time ASC
                """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(FlashSale.class)
                        .list()
        );
    }

    /**
     * Lấy Flash Sale theo trạng thái
     */
    public List<FlashSale> getFlashSalesByStatus(String status) {
        updateStatuses();

        String sql = """
        SELECT *
        FROM FlashSale
        WHERE status = :status
        ORDER BY start_time ASC
    """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("status", status)
                        .mapToBean(FlashSale.class)
                        .list()
        );
    }
}
