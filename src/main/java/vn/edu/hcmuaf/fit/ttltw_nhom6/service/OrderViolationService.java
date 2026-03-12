package vn.edu.hcmuaf.fit.ttltw_nhom6.service;

import vn.edu.hcmuaf.fit.ltw_nhom5.dao.OrderDAO;
import vn.edu.hcmuaf.fit.ltw_nhom5.dao.OrderHistoryDAO;
import vn.edu.hcmuaf.fit.ltw_nhom5.dao.UserDao;
import vn.edu.hcmuaf.fit.ltw_nhom5.model.User;

import java.util.Optional;

public class OrderViolationService {
    private static OrderViolationService instance;
    private static final int VIOLATION_THRESHOLD = 5;

    private OrderViolationService() {
    }

    public static OrderViolationService getInstance() {
        if (instance == null) {
            instance = new OrderViolationService();
        }
        return instance;
    }

    /**
     * Kiểm tra user có hủy quá nhiều đơn trong 1 giờ không
     * Nếu có, thông báo cho admin
     */
    public void checkCancelViolation(int userId) {

        OrderDAO orderDAO = new OrderDAO();
        int cancelCount = OrderHistoryDAO.countUserCancelledOrdersLastHour(userId);


        // 2. Nếu vi phạm (>= 5 đơn)
        if (cancelCount >= VIOLATION_THRESHOLD) {
            // Lấy thông tin user
            User user = UserDao.getInstance().getUserById(userId);
            String username = user != null ? user.getFullName() : "User #" + userId;

            // Gửi thông báo cho admin
            NotificationService.getInstance().notifyAdminUserViolation(
                    userId,
                    username,
                    "HỦY ĐƠN HÀNG NHIỀU LẦN",
                    String.format("Đã hủy %d đơn hàng trong vòng 1 giờ", cancelCount)
            );

        }
    }

    /**
     * Kiểm tra đăng nhập thất bại nhiều lần
     * Sử dụng cột failed_login_attempts trong bảng Users
     */
    public void checkLoginFailureViolation(String username) {
        // Tìm user theo username hoặc email
        Optional<User> userOpt = UserDao.getInstance().findByUsernameOrEmail(username);

        if (!userOpt.isPresent()) {
            return;
        }

        User user = userOpt.get();
        int failCount = user.getFailedLoginAttempts();

        // Nếu vi phạm (>= 5 lần)
        if (failCount >= VIOLATION_THRESHOLD) {
            NotificationService.getInstance().notifyAdminUserViolation(
                    user.getId(),
                    user.getFullName() != null ? user.getFullName() : username,
                    "ĐĂNG NHẬP THẤT BẠI NHIỀU LẦN",
                    String.format("Đã đăng nhập thất bại %d lần liên tiếp. Nghi ngờ tấn công brute-force.", failCount)
            );

        }
    }

    /**
     * Reset số lần đăng nhập thất bại về 0 khi đăng nhập thành công
     *
     * @param userId ID của user
     */
    public void resetLoginFailureCount(int userId) {
        UserDao.getInstance().resetFailedLoginAttempts(userId);
    }

    /**
     * Tăng số lần đăng nhập thất bại lên 1
     */
    public void incrementLoginFailureCount(String username) {
        Optional<User> userOpt = UserDao.getInstance().findByUsernameOrEmail(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserDao.getInstance().incrementFailedLoginAttempts(user.getId());
            System.out.println("📈 [LoginViolation] Tăng failed_login_attempts cho user " + user.getId());
        }
    }
}
