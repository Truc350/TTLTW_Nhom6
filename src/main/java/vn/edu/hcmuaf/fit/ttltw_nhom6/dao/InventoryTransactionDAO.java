package vn.edu.hcmuaf.fit.ttltw_nhom6.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.fit.ttltw_nhom6.db.JdbiConnector;

public class InventoryTransactionDAO {
    private final Jdbi jdbi;

    public InventoryTransactionDAO() {
        this.jdbi = JdbiConnector.get();
    }
//nhập hàng
    public boolean insertImport(int comic_id, int quantity, String note) {
        String sql = "insert into inventory_transaction(comic_id, type, quantity, note, created_at) " +
                "values (:comic_id, 'IMPORT', :quantity, :note, NOW());";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("comic_id", comic_id)
                        .bind("quantity", quantity)
                        .bind("note", note)
                        .execute()) >0;
    }



    public void updateStockQuantity(int comic_id, int quantity) {
        String sql = "update comics set stock_quantity = stock_quantity + quantity where id = :comic_id;";
        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("stock_quantity", quantity)
                        .bind("comic_id", comic_id)
                        .execute());
    }

//bán hàng

    public void insertExport(int comic_id, String type, int quantity, String note) {
        String sql = "insert into inventory_transaction(comic_id, type, quantity, note, created_at)  values (:comic_id, 'EXPORT', :quantity, note, NOW());";
        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("comic_id", comic_id)
                        .bind("quantity", quantity)
                        .bind("note", note)
                        .execute());
    }
    public boolean updateStockQuantity(String comic_id, int quantity_sell) {
        String sql = "update comics set stock_quantity = stock_quantity - :quantity_sell where id = :comic_id and stock_quantity >= :quantity_sell;";
        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("comic_id", comic_id)
                        .bind("quantity_sell", quantity_sell)
                        .execute()) > 0;
    }

// Hàng hỏng
    public boolean insertDAMAGEItem(String comic_id, int quantity, String note) {
        String sql = "insert into inventory_transaction(comic_id, type, quantity, note, created_at) values (:comic_id, 'DAMAGE', :quantity, :note, NOW()); ";
        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("comic_id", comic_id)
                        .bind("quantity", quantity)
                        .bind("note", note)
                        .execute()) > 0;
    }

    public boolean updateDamagedQuantity(String comic_id, int quantity_damaged) {
        String sql = "update comics set stock_quantity = stock_quantity + :quantity_damaged where id = :comic_id;";
        return jdbi.withHandle(handle ->
            handle.createUpdate(sql)
                    .bind("comic_id", comic_id)
                    .bind("quantity_damaged", quantity_damaged)
                    .execute()) > 0;
}



}
