<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="vn.edu.hcmuaf.fit.ttltw_nhom6.dao.WishlistDAO" %>
<%@ page import="vn.edu.hcmuaf.fit.ttltw_nhom6.model.Comic" %>
<%@ page import="vn.edu.hcmuaf.fit.ttltw_nhom6.model.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/fontend/css/publicCss/FooterStyle.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/fontend/css/publicCss/nav.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/fontend/css/UserBCss/profile.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&family=Noto+Sans:wght@300;400;500;700&display=swap"
          rel="stylesheet">
</head>
<body>

<jsp:include page="/fontend/public/header.jsp"/>

<main>
    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
    <script>
        window.onload = function() {
            alert("<%= message %>");
        };
    </script>
    <%
        }
    %>


    <div class="profile-container">
        <div class="profile-header">
            <div class="avatar">
                <img src="https://mayweddingstudio.vn/wp-content/uploads/anh-dai-dien-facebook-nu-9.webp" alt="Avatar">
            </div>
            <div class="user-info">
                <h2>Huỳnh Trúc</h2>
                <p class="membership">Thành viên Bạc</p>
                <p class="C-point">C-Point tích lũy: 0</p>
            </div>
        </div>
        <div class="menu">
            <ul>
                <li><a href="#">Thông tin tài khoản </a></li>
                <li><a href="#">Đổi mật khẩu</a></li>
                <li><a href="#">Ưu đãi C-Point</a></li>
                <li><a href="#">Đơn hàng của tôi</a></li>
                <li><a href="#">Thông báo</a></li>
                <li><a href="#">Sản phẩm yêu thích</a></li>
            </ul>
        </div>
    </div>
    <div class="profile-form-container">
        <h2>Hồ sơ cá nhân</h2>
        <form action="${pageContext.request.contextPath}/updateUser" method="post">
            <div class="form-group">
                <label for="ho">Họ: *</label>
                <input type="text" id="ho" name="ho" value="" placeholder="Nhập họ" required>
            </div>
            <div class="form-group">
                <label for="ten">Tên: *</label>
                <input type="text" id="ten" name="ten" value="" placeholder="Nhập tên" required>
            </div>
            <div class="form-group">
                <label for="phone">Số điện thoại:</label>
                <input type="tel" id="phone" name="phone"placeholder="Nhập số điện thoại"
                        required pattern="[0-9]{10}" maxlength="10" title="nhập lại sdt"/>

            </div>
            <div class="form-group">
                <label for="email">Email: </label>
                <input type="email" id="email" name="email" value="" placeholder="Nhập email" required>
            </div>
            <div class="form-group">
                <div class="genders">
                    <label>Giới tính: *</label>
                    <div class="gender-options">
                        <label><input type="radio" name="gender" value="male" checked> Nam</label>
                        <label><input type="radio" name="gender" value="female"> Nữ</label>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="birthday">
                    <label for="day">Ngày tháng năm sinh: *</label>
                    <input type="number" id="day" name="day" value="27" min="1" max="31" required> /
                    <input type="number" id="month" name="month" value="10" min="1" max="12" required> /
                    <input type="number" id="year" name="year" value="2005" min="1900" max="2025" required>
                </div>
            </div>
            <div class="form-group">
                <div class="address">
                    <label for="country">Quốc gia: *</label>
                    <select id="country" name="country" required>
                        <option value="" disabled selected>Chọn quốc gia</option>
                        <option value="Vietnam">Việt Nam</option>

                    </select>
                    <div class="diaChi">
                        <div class="address-group">
                            <label for="province">Tỉnh/Thành phố: *</label>
                            <select id="province" name="province" required>
                                <option value="" disabled selected>Chọn tỉnh/thành phố</option>
                            </select>
                        </div>

                        <div class="address-group">
                            <label for="district">Huyện: *</label>
                            <select id="district" name="district" required>
                                <option value="" disabled selected>Chọn huyện</option>
                            </select>
                        </div>
                    </div>

                    <label for="house-number">Số nhà: *</label>
                    <input type="text" id="house-number" name="house-number" placeholder="Nhập số nhà, xã" required>
                </div>
            </div>
            <button type="submit" class="save-btn">Lưu thay đổi</button>
        </form>



    </div>
    <div class="change-password-container" style="display: none;">
        <h2>Đổi Mật Khẩu</h2>
        <% if (request.getAttribute("error") != null) { %>
        <div class="alert" style="
    background-color: #ffebee;          /* Nền đỏ rất nhạt */
    color: #b71c1c;                     /* Chữ đỏ đậm nhưng không chói */
    border: 1px solid #ef9a9a;          /* Viền đỏ nhạt */
    border-left: 5px solid #d32f2f;     /* Thanh trái nổi bật */
    padding: 15px;
    margin: 15px 0;
    border-radius: 4px;
    opacity: 0.92;                      /* Làm mờ nhẹ để chữ lợt hơn */
    font-size: 1rem;
">
            <strong>Lỗi:</strong> <%= request.getAttribute("error") %>
        </div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
        <div class="alert" style="
    background-color: #ffebee;
    color: #b71c1c;
    border: 1px solid #ef9a9a;
    border-left: 5px solid #d32f2f;
    padding: 15px;
    margin: 15px 0;
    border-radius: 4px;
    opacity: 0.92;
    font-size: 1rem;
">
            <strong>Thành công:</strong> <%= request.getAttribute("success") %>
        </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/change-password" method="post">
            <div class="form-group">
                <label for="current-password">Mật khẩu hiện tại*:</label>
                <input type="password" id="current-password" name="current-password" required>

            </div>
            <div class="form-group">
                <label for="new-password">Mật khẩu mới*:</label>
                <input type="password" id="new-password" name="new-password" required>

                <p class="password-requirement">Yêu cầu: Tối thiểu 8 ký tự, bao gồm chữ hoa và thường, số và ký tự đặc biệt.</p>
            </div>
            <div class="form-group">
                <label for="confirm-password">Xác nhận mật khẩu mới*:</label>
                <input type="password" id="confirm-password" name="confirm-password" required>

            </div>
            <div class="form-group">
                <p class="security-note">Lưu ý: Để bảo mật, không chia sẻ mật khẩu với bất kỳ ai. Nếu quên mật khẩu, hãy
                    liên hệ hỗ trợ qua email comicstore365@gmail.com.</p>
            </div>
            <button type="submit" class="save-btn">Lưu thay đổi</button>
        </form>
    </div>


    <div class="order-history-container" style="display: none;">
        <h2>Đơn hàng của tôi</h2>
        <div class="order-filters">
            <div class="filter-tabs">
                <button class="scroll-btn scroll-left" title="Cuộn trái">&lt;</button>
                <div class="tabs-container">
                    <button class="filter-tab" data-filter="pending">Chờ xác nhận</button>
                    <button class="filter-tab" data-filter="shipping">Vận chuyển</button>
                    <button class="filter-tab" data-filter="delivery">Chờ giao hàng</button>
                    <button class="filter-tab active" data-filter="completed">Hoàn thành</button>
                    <button class="filter-tab" data-filter="canceled">Đã hủy</button>
                    <button class="filter-tab" data-filter="refund">Trả hàng/Hoàn tiền</button>
                </div>
                <button class="scroll-btn scroll-right" title="Cuộn phải">&gt;</button>
            </div>
        </div>
        <div class="orders-list">
            <div class="order-item" data-status="completed">
                <div class="order-header">
                    <span class="order-id">ID đơn hàng: #10000095</span>
                    <span class="order-date">09/11/2025</span>
                    <span class="order-status completed">Hoàn thành</span>
                </div>
                <div class="order-content">
                    <div class="product-item">
                        <img src="https://product.hstatic.net/200000343865/product/thanh-pho-len-day-cot-2_b38b1803c2894d72ba2852cacb606a78_master.jpg"
                             alt="truyen tranh"
                             class="product-img">
                        <div class="product-info">
                            <h3>Doraemon - Nobita và cuộc phiêu lưu <br> ở thành phố dây cót</h3>
                            <p class="product-quantity">Số lượng: x1</p>
                            <div class="price-details">
                                <span class="original-price">100.000đ</span>
                                <span class="discount-price">95.000đ</span>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="order-total">
                    <span>Tổng tiền: <strong>95.000đ</strong></span>
                </div>
                <div class="order-actions">
                    <a href="cart.jsp">
                        <button class="action-btn buy-again">Mua lại</button>
                    </a>
                    <button id="Evaluate" class="action-btn contact-seller">Đánh giá</button>
                </div>
            </div>
            <div class="order-item" data-status="pending">
                <div class="order-header">
                    <span class="order-id">ID đơn hàng: #10000123</span>
                    <span class="order-date">10/11/2025</span>
                    <span class="order-status pending">Chờ xác nhận</span>
                </div>
                <div class="order-content">
                    <div class="product-item">
                        <img src="https://salt.tikicdn.com/media/catalog/product/b/i/bia_1-_co_be_ban_diem_1.jpg"
                             alt="truyen tranh" class="product-img">
                        <div class="product-info">
                            <h3>Cô bé bán diêm</h3>
                            <p class="product-quantity">Số lượng: x1</p>
                            <div class="price-details">
                                <span class="original-price">60.000đ</span>
                                <span class="discount-price">55.000đ</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="order-total">
                    <span>Tổng tiền: <strong>55.000đ</strong></span>
                </div>
                <div class="order-actions">
                    <a href="https://zalo.me/0394158994" target="_blank">
                        <button class="action-btn contact-seller">Liên hệ</button>
                    </a>
                    <button class="action-btn cancel-order">Hủy đơn hàng</button>
                </div>
            </div>
            <div class="order-item" data-status="shipping">
                <div class="order-header">
                    <span class="order-id">ID đơn hàng: #10000265</span>
                    <span class="order-date">10/11/2025</span>
                    <span class="order-status shipping">Vận chuyển</span>
                </div>
                <div class="order-content">
                    <div class="product-item">
                        <img src="https://i.pinimg.com/originals/3a/a9/47/3aa9473f3ce582ddfcc0cf8cf2a12edf.jpg"
                             alt="truyen tranh" class="product-img">
                        <div class="product-info">
                            <h3>Shin cậu bé bút chì - Học viện hoa lệ Tenkasu</h3>
                            <p class="product-quantity">Số lượng: x1</p>
                            <div class="price-details">
                                <span class="original-price">89.000đ</span>
                                <span class="discount-price">80.000đ</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="order-total">
                    <span>Tổng tiền: <strong>80.000đ</strong></span>
                </div>
                <div class="order-actions">
                    <a href="https://zalo.me/0394158994" target="_blank">
                        <button class="action-btn contact-seller">Liên hệ</button>
                    </a>
                </div>
            </div>
            <div class="order-item" data-status="delivery">
                <div class="order-header">
                    <span class="order-id">ID đơn hàng: #10000234</span>
                    <span class="order-date">10/11/2025</span>
                    <span class="order-status delivery">Chờ giao hàng</span>
                </div>

                <div class="order-content">
                    <div class="product-item">
                        <img src="https://product.hstatic.net/200000287623/product/5cm_5f3b4ebe155b4d6491c244b8623657b5_master.jpg"
                             alt="truyen tranh" class="product-img">
                        <div class="product-info">
                            <h3>5cm/s</h3>
                            <a class="product-quantity">Số lượng: x1</a>
                            <div class="price-details">
                                <span class="original-price">125.000đ</span>
                                <span class="discount-price">120.000đ</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="order-total">
                    <span>Tổng tiền: <strong>120.000đ</strong></span>
                </div>

                <div class="order-actions">
                    <button class="action-btn contact-seller">Trả hàng</button>
                    <button class="action-btn receive-order">Đã nhận hàng</button>
                </div>
            </div>
            <div class="order-item" data-status="canceled">
                <div class="order-header">
                    <span class="order-id">ID đơn hàng: #10000345</span>
                    <span class="order-date">09/11/2025</span>
                    <span class="order-status canceled">Đã hủy</span>
                </div>

                <div class="order-content">
                    <div class="product-item">
                        <img src="https://bookbuy.vn/Res/Images/Product/cardcaptor-sakura-tap-7_44502_1.jpg"
                             alt="truyen tranh" class="product-img">
                        <div class="product-info">
                            <h3>Sakura thủ lĩnh thẻ bài</h3>
                            <p class="product-quantity">Số lượng: x1</p>
                            <div class="price-details">
                                <span class="original-price">50.000đ</span>
                                <span class="discount-price">48.000đ</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="order-total">
                    <span>Tổng tiền: <strong>48.000đ</strong></span>
                </div>

                <div class="order-actions">
                    <a href="cart.jsp">
                        <button class="action-btn buy-again">Mua lại</button>
                    </a>
                    <a href="https://zalo.me/0394158994" target="_blank">
                        <button class="action-btn contact-seller">Liên hệ</button>
                    </a>
                </div>
            </div>

            <div class="order-item" data-status="refund">
                <div class="order-header">
                    <span class="order-id">ID đơn hàng: #10000478</span>
                    <span class="order-date">08/11/2025</span>
                    <span class="order-status refund">Trả hàng/Hoàn tiền</span>
                </div>

                <div class="order-content">
                    <div class="product-item">
                        <img src="https://www.netabooks.vn/Data/Sites/1/Product/66328/kho-do-danh-tap-1-2.jpg"
                             alt="truyen tranh" class="product-img">
                        <div class="product-info">
                            <h3>Khó ỗ dành</h3>
                            <p class="product-quantity">Số lượng: x1</p>
                            <div class="price-details">
                                <span class="original-price">128.000đ</span>
                                <span class="discount-price">125.000đ</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="order-total">
                    <span>Tổng tiền: <strong>125.000đ</strong></span>
                </div>

                <div class="order-actions">
                    <a href="cart.jsp">
                        <button class="action-btn buy-again">Mua lại</button>
                    </a>
                    <a href="https://zalo.me/0394158994" target="_blank">
                        <button class="action-btn contact-seller">Liên hệ</button>
                    </a>
                </div>
            </div>


            <div class="no-orders"
                 style="display: none; text-align: center; padding: 60px 20px; color: #666; font-size: 16px;">
                <div style="margin-bottom: 20px;">
                    <img src="https://png.pngtree.com/png-clipart/20230418/original/pngtree-order-confirm-line-icon-png-image_9065104.png"
                         alt="No orders" style="width: 120px; height: auto;">
                </div>
                <p style="margin: 0; font-weight: 500; color: #555;">Bạn chưa có đơn hàng nào cả</p>
            </div>


        </div>
    </div>

    <div class="voucher-container" style="display: none;">
        <h2>Ví voucher</h2>
        <div class="vouchers-grid" id="vouchers-grid">
            <!-- Voucher 1: Mã giảm 10K - Toàn sàn -->
            <div class="voucher-card" data-voucher='{
            "code": "FHS10KT11",
            "value": "Giảm 10.000 VNĐ",
            "condition": "Đơn hàng từ 130.000 VNĐ",
            "exclude": "Manga, Ngoại văn,...",
            "expiry": "30/11/2025",
            "status": "Chưa sử dụng"
        }'>
                <div class="voucher-header">
                    <span class="voucher-type">Voucher chưa sử dụng</span>
                    <span class="voucher-status unused">Chưa sử dụng</span>
                </div>
                <div class="voucher-body">
                    <div class="voucher-title">Mã Giảm 10K - Toàn Sàn</div>
                    <div class="voucher-desc">Đơn hàng từ 130k - Không bao gồm giá trị của các sản phẩm sau Manga, Ngoại
                        văn...
                    </div>
                    <div class="voucher-code">FHS10KT11</div>
                    <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                </div>
                <div class="voucher-footer">
                    <span class="voucher-expiry">HSD: 30/11/2025</span>
                    <button class="copy-code-btn">Copy mã</button>
                </div>
            </div>

            <!-- Voucher 2: Mã giảm 20K - Toàn sàn -->
            <div class="voucher-card" data-voucher='{
            "code": "FHS20KT11",
            "value": "Giảm 20.000 VNĐ",
            "condition": "Đơn hàng từ 240.000 VNĐ",
            "exclude": "Manga, Ngoại văn,...",
            "expiry": "30/11/2025",
            "status": "Chưa sử dụng"
        }'>
                <div class="voucher-header">
                    <span class="voucher-type">Voucher chưa sử dụng</span>
                    <span class="voucher-status unused">Chưa sử dụng</span>
                </div>
                <div class="voucher-body">
                    <div class="voucher-title">Mã Giảm 20K - Toàn Sàn</div>
                    <div class="voucher-desc">Đơn hàng từ 240k - Không bao gồm giá trị của các sản phẩm sau Manga, Ngoại
                        văn...
                    </div>
                    <div class="voucher-code">FHS20KT11</div>
                    <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                </div>
                <div class="voucher-footer">
                    <span class="voucher-expiry">HSD: 30/11/2025</span>
                    <button class="copy-code-btn">Copy mã</button>
                </div>
            </div>

            <!-- Voucher 3: Mã giảm 40K - Toàn sàn -->
            <div class="voucher-card" data-voucher='{
            "code": "FHS40KT11",
            "value": "Giảm 40.000 VNĐ",
            "condition": "Đơn hàng từ 490.000 VNĐ",
            "exclude": "Manga, Ngoại văn,...",
            "expiry": "30/11/2025",
            "status": "Chưa sử dụng"
        }'>
                <div class="voucher-header">
                    <span class="voucher-type">Voucher chưa sử dụng</span>
                    <span class="voucher-status unused">Chưa sử dụng</span>
                </div>
                <div class="voucher-body">
                    <div class="voucher-title">Mã Giảm 40K - Toàn Sàn</div>
                    <div class="voucher-desc">Đơn hàng từ 490k - Không bao gồm giá trị của các sản phẩm sau Manga, Ngoại
                        văn...
                    </div>
                    <div class="voucher-code">FHS40KT11</div>
                    <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                </div>
                <div class="voucher-footer">
                    <span class="voucher-expiry">HSD: 30/11/2025</span>
                    <button class="copy-code-btn">Copy mã</button>
                </div>
            </div>

            <!-- Voucher 4: Mã giảm 80K - Toàn sàn -->
            <div class="voucher-card" data-voucher='{
            "code": "FHS80KT11",
            "value": "Giảm 80.000 VNĐ",
            "condition": "Đơn hàng từ 990.000 VNĐ",
            "exclude": "Manga, Ngoại văn,...",
            "expiry": "30/11/2025",
            "status": "Chưa sử dụng"
        }'>
                <div class="voucher-header">
                    <span class="voucher-type">Voucher chưa sử dụng</span>
                    <span class="voucher-status unused">Chưa sử dụng</span>
                </div>
                <div class="voucher-body">
                    <div class="voucher-title">Mã Giảm 80K - Toàn Sàn</div>
                    <div class="voucher-desc">Đơn hàng từ 990k - Không bao gồm giá trị của các sản phẩm sau Manga, Ngoại
                        văn...
                    </div>
                    <div class="voucher-code">FHS80KT11</div>
                    <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                </div>
                <div class="voucher-footer">
                    <span class="voucher-expiry">HSD: 30/11/2025</span>
                    <button class="copy-code-btn">Copy mã</button>
                </div>
            </div>
        </div>
    </div>

    <%
        WishlistDAO wishlistDAO = new WishlistDAO();
        User currentUser = (User) session.getAttribute("currentUser");

        List<Comic> wishlistComics = null;
        int wishlistCount = 0;

        if (currentUser != null) {
            wishlistComics = wishlistDAO.getWishlistComics(currentUser.getId());
            wishlistCount = wishlistComics != null ? wishlistComics.size() : 0;
        }
    %>
    <div class="wishlist-container" style="display: none;">
        <h2>Sản phẩm yêu thích (<%= currentUser != null ? wishlistCount : 0 %> sản phẩm)</h2>

        <% if (currentUser == null) { %>
        <%-- Trường hợp 1: Chưa đăng nhập --%>
        <div style="text-align:center; padding:80px 20px; color:#666;">
            <i class="fa-regular fa-heart" style="font-size:80px; color:#ddd; margin-bottom:20px; display:block;"></i>
            <p style="font-size:18px; margin:10px 0; font-weight:500;">Vui lòng đăng nhập để xem danh sách yêu thích</p>
            <p style="font-size:14px; color:#999; margin:10px 0;">Lưu trữ những truyện yêu thích để không bao giờ bỏ lỡ!</p>
            <a href="${pageContext.request.contextPath}/login"
               style="display:inline-block; padding:12px 30px; background:#007bff; color:white;
                      text-decoration:none; border-radius:6px; margin-top:20px; font-weight:500;
                      transition: all 0.3s ease;">
                Đăng nhập ngay
            </a>
        </div>

        <% } else if (wishlistComics == null || wishlistComics.isEmpty()) { %>
        <%-- Trường hợp 2: Đã đăng nhập nhưng chưa có sản phẩm yêu thích --%>
        <div style="text-align:center; padding:80px 20px; color:#666;">
            <i class="fa-regular fa-heart" style="font-size:80px; color:#ddd; margin-bottom:20px; display:block;"></i>
            <p style="font-size:18px; margin:10px 0; font-weight:500;">Danh sách yêu thích còn trống</p>
            <p style="font-size:14px; color:#999; margin:10px 0;">Hãy thêm những truyện bạn yêu thích vào đây!</p>
            <a href="${pageContext.request.contextPath}/home"
               style="display:inline-block; padding:12px 30px; background:#007bff; color:white;
                      text-decoration:none; border-radius:6px; margin-top:20px; font-weight:500;
                      transition: all 0.3s ease;">
                Khám phá ngay!
            </a>
        </div>

        <% } else { %>
        <%-- Trường hợp 3: Có sản phẩm yêu thích --%>
        <div class="wishlist-grid">
            <% for (Comic comic : wishlistComics) { %>
            <div class="wishlist-item" data-comic-id="<%= comic.getId() %>">
                <a href="${pageContext.request.contextPath}/comic-detail?id=<%= comic.getId() %>"
                   style="text-decoration: none;">
                    <img src="<%= comic.getThumbnailUrl() != null ? comic.getThumbnailUrl() : "" %>"
                         alt="<%= comic.getNameComics() %>"
                         class="wishlist-img"
                         onerror="this.src='${pageContext.request.contextPath}/images/default-comic.jpg'">
                </a>
                <div class="wishlist-info">
                    <h3>
                        <a href="${pageContext.request.contextPath}/comic-detail?id=<%= comic.getId() %>">
                            <%= comic.getNameComics() %>
                        </a>
                    </h3>
                    <div class="wishlist-price">
                            <span class="current-price">
                                <fmt:formatNumber value="<%= comic.getDiscountPrice() %>"
                                                  type="number"
                                                  groupingUsed="true"/> đ
                            </span>
                        <% if (comic.hasDiscount()) { %>
                        <span class="original-price">
                                    <fmt:formatNumber value="<%= comic.getPrice() %>"
                                                      type="number"
                                                      groupingUsed="true"/> đ
                                </span>
                        <span class="discount-badge">
                                    -<%= Math.round(comic.getDiscountPercent()) %>%
                                </span>
                        <% } %>
                    </div>

                    <% if (comic.getStockQuantity() > 0) { %>
                    <p class="stock-status available">
                        <i class="fas fa-check-circle"></i> Còn hàng
                    </p>
                    <% } else { %>
                    <p class="stock-status out-of-stock">
                        <i class="fas fa-times-circle"></i> Hết hàng
                    </p>
                    <% } %>

                    <div class="wishlist-actions">
                        <button class="add-to-cart-btn"
                                data-comic-id="<%= comic.getId() %>"
                                <%= comic.getStockQuantity() == 0 ? "disabled" : "" %>>
                            <i class="fas fa-shopping-cart"></i> Thêm vào giỏ
                        </button>
                        <button class="remove-wishlist-btn"
                                data-comic-id="<%= comic.getId() %>">
                            <i class="fas fa-trash-alt"></i> Xóa
                        </button>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
        <% } %>
    </div>

    <div class="xu-container" style="display: none">
        <div class="xu-header">
            <div class="xu-text">
                <p class="label">Số xu hiện có</p>
                <h1>2,500</h1>
            </div>
            <img src="${pageContext.request.contextPath}/img/dollar.png" class="xu-icon">
        </div>


        <div class="history">
            <h3>Lịch sử giao dịch</h3>

            <div class="item">
                <div class="left">
                    <div class="light-icon">⚡</div>
                    <p>Hoàn thành đơn hàng</p>
                </div>
                <span class="value plus">+200 C-Point</span>
            </div>

            <div class="item">
                <div class="left">
                    <div class="light-icon">⚡</div>
                    <p>Hoàn thành đơn hàng</p>
                </div>
                <span class="value plus">+500 C-Point</span>
            </div>
        </div>

    </div>

    <div class="notification-container" style="display: none">
        <div class="tabs">
            <div class="tab active">Tất cả</div>
            <div class="tab">Đơn Hàng</div>
            <div class="tab">Sự kiện</div>
        </div>
        <div class="line"></div>
        <div class="empty-message">Không có thông báo.</div>
    </div>

    <div class="popup-backdrop-review" style="display: none;"></div>

    <div class="container-write-review" style="display: none;">
        <div class="header-review">
            <p id="title">VIẾT ĐÁNH GIÁ SẢN PHẨM</p>
            <div class="rating-stars" id="rating-stars">
                <span data-value="1">★</span>
                <span data-value="2">★</span>
                <span data-value="3">★</span>
                <span data-value="4">★</span>
                <span data-value="5">★</span>
            </div>
        </div>

        <div class="field">
            <label for="comment" class="nameDisplay">Nhận xét</label>
            <textarea id="comment" placeholder="Nhập nhận xét của bạn"></textarea>
        </div>

        <div class="field-img">
            <label for="image-upload" class="nameDisplay">Tải ảnh</label>
            <input type="file" id="image-upload" accept="image/*" multiple/>
            <div id="image-preview"></div>
        </div>

        <div class="actions-write-review">
            <button class="cancel-review btn-reivew">Hủy</button>
            <button class="submit-review btn-reivew primary">Gửi nhận xét</button>
        </div>
    </div>


</main>

<div id="voucher-detail-popup" class="voucher-popup" style="display: none;">
    <div class="voucher-popup-content">
        <span class="close-popup" onclick="closeVoucherPopup()">&times;</span>

        <h2>Chi tiết mã giảm giá</h2>

        <div class="voucher-detail-row">
            <span class="label">Mã giảm giá:</span>
            <span id="detail-code"></span>
        </div>

        <div class="voucher-detail-row">
            <span class="label">Giá trị:</span>
            <span id="detail-value"></span>
        </div>

        <div class="voucher-detail-row">
            <span class="label">Điều kiện:</span>
            <span id="detail-condition"></span>
        </div>

        <div class="voucher-detail-row">
            <span class="label">Không áp dụng cho:</span>
            <span id="detail-exclude"></span>
        </div>

        <div class="voucher-detail-row">
            <span class="label">Hạn sử dụng:</span>
            <span id="detail-expiry"></span>
        </div>

        <div class="voucher-detail-row">
            <span class="label">Trạng thái:</span>
            <span id="detail-status"></span>
        </div>

        <button class="use-voucher-btn">Sử dụng ngay</button>
    </div>
</div>

<jsp:include page="/fontend/public/Footer.jsp"/>

<script>
    function showVoucherDetails(button) {
        const voucherCard = button.closest('.voucher-card');
        const voucherData = JSON.parse(voucherCard.getAttribute('data-voucher'));
        document.getElementById('detail-code').textContent = voucherData.code;
        document.getElementById('detail-value').textContent = voucherData.value;
        document.getElementById('detail-condition').textContent = voucherData.condition;
        document.getElementById('detail-exclude').textContent = voucherData.exclude;
        document.getElementById('detail-expiry').textContent = voucherData.expiry;
        document.getElementById('detail-status').textContent = voucherData.status;
        document.getElementById('voucher-detail-popup').style.display = 'flex';
    }

    function closeVoucherPopup() {
        document.getElementById('voucher-detail-popup').style.display = 'none';
    }
    document.addEventListener('DOMContentLoaded', function () {
        const voucherContainer = document.querySelector('.voucher-container');
        const popupBackdrop = document.querySelector('.popup-backdrop');
        const voucherSelect = document.querySelector('.voucher-select');
        const closePopupBtn = document.querySelector('.close-popup-btn');

        function closeVoucherPopup() {
            if (voucherContainer) {
                voucherContainer.classList.remove('popup-active');
                voucherContainer.style.display = 'none';
            }
            if (popupBackdrop) {
                popupBackdrop.classList.remove('active');
            }
        }

        if (voucherSelect) {
            voucherSelect.addEventListener('click', function (e) {
                e.preventDefault();
                if (voucherContainer && popupBackdrop) {
                    if (voucherContainer.classList.contains('popup-active')) {
                        closeVoucherPopup();
                    } else {
                        voucherContainer.style.display = 'block';
                        voucherContainer.classList.add('popup-active');
                        popupBackdrop.classList.add('active');
                        const allVouchers = voucherContainer.querySelectorAll('.voucher-card');
                        allVouchers.forEach(v => {
                            v.style.display = v.classList.contains('used') ? 'none' : 'block';
                        });
                    }
                }
            });
        }

        if (popupBackdrop) {
            popupBackdrop.addEventListener('click', function (e) {
                if (e.target === popupBackdrop) {
                    closeVoucherPopup();
                }
            });
        }

        if (closePopupBtn) {
            closePopupBtn.addEventListener('click', function (e) {
                e.preventDefault();
                closeVoucherPopup();
            });
        }
        if (voucherContainer) {
            voucherContainer.addEventListener('click', function (e) {
                e.stopPropagation();
            });
        }
        document.addEventListener('click', function (e) {
            if (e.target.classList.contains('copy-code-btn') && !e.target.disabled) {
                const code = e.target.closest('.voucher-card')?.querySelector('.voucher-code')?.textContent;
                if (code) {
                    navigator.clipboard.writeText(code).then(() => {
                        alert('Đã copy mã: ' + code);
                    }).catch(() => {
                        alert('Không thể copy mã. Vui lòng copy thủ công.');
                    });
                }
            }
        });

        document.addEventListener('click', function (e) {
            if (e.target.classList.contains('voucher-detail-btn')) {
                const title = e.target.closest('.voucher-card')?.querySelector('.voucher-title')?.textContent;
                if (title) {
                    alert('Chi tiết voucher: ' + title + '\n(Ở đây có thể mở modal hoặc trang mới)');
                }
            }
        });
        const reviewContainer = document.querySelector('.container-write-review');
        const popupBackdropReview = document.querySelector('.popup-backdrop-review');
        const evaluateBtn = document.querySelector('#Evaluate');
        const cancelReviewBtn = document.querySelector('.cancel-review');
        const submitReviewBtn = document.querySelector('.submit-review');

        evaluateBtn.addEventListener('click', () => {
            reviewContainer.style.display = 'block';
            popupBackdropReview.style.display = 'block';
        });

        function closeReviewPopup() {
            reviewContainer.style.display = 'none';
            popupBackdropReview.style.display = 'none';
        }

        cancelReviewBtn.addEventListener('click', closeReviewPopup);
        popupBackdropReview.addEventListener('click', closeReviewPopup);

        const ratingStars = document.querySelectorAll('#rating-stars span');

        ratingStars.forEach(star => {
            star.addEventListener('click', () => {
                const value = star.getAttribute('data-value');
                ratingStars.forEach(s => s.classList.remove('active'));
                for (let i = 0; i < value; i++) {
                    ratingStars[i].classList.add('active');
                }
            });
        });
        document.querySelector('#image-upload').addEventListener('change', function () {
            const preview = document.querySelector('#image-preview');
            const files = Array.from(this.files).slice(0, 3); // Chỉ lấy 3 ảnh đầu
            preview.innerHTML = '';

            if (this.files.length > 3) {
                alert('Bạn chỉ được tải lên tối đa 3 ảnh!');
            }

            files.forEach(file => {
                if (file.type.startsWith('image/')) {
                    const img = document.createElement('img');
                    img.src = URL.createObjectURL(file);
                    img.style.width = "80px";
                    img.style.height = "80px";
                    img.style.objectFit = "cover";
                    img.style.marginRight = "8px";
                    img.style.borderRadius = "4px";
                    img.style.border = "1px solid #ddd";
                    preview.appendChild(img);
                }
            });

            const dataTransfer = new DataTransfer();
            files.forEach(file => dataTransfer.items.add(file));
            this.files = dataTransfer.files;
        });

        submitReviewBtn.addEventListener('click', () => {
            const name = document.querySelector('#display-name').value.trim();
            const comment = document.querySelector('#comment').value.trim();
            const rating = document.querySelectorAll('#rating-stars .active').length;

            if (!name || !comment || rating === 0) {
                alert("Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            alert("Gửi đánh giá thành công!");
            closeReviewPopup();
        });
    });
</script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const provinceSelect = document.getElementById('province');
        const districtSelect = document.getElementById('district');
        const profileForm = document.querySelector('.profile-form-container');
        const changePasswordForm = document.querySelector('.change-password-container');
        const orderHistoryForm = document.querySelector('.order-history-container');
        const voucherContainer = document.querySelector('.voucher-container');
        const wishlistContainer = document.querySelector('.wishlist-container');
        const xuContainer = document.querySelector('.xu-container'); // Added xu-container
        const menuItems = document.querySelectorAll('.menu ul li');
        const notificationContainer = document.querySelector('.notification-container');

        const fallbackDistricts = {
            '1': [{code: '001', name: 'Ba Đình'}, {code: '002', name: 'Hoàn Kiếm'}, {code: '003', name: 'Cầu Giấy'}],
            '79': [{code: '268', name: 'Quận 1'}, {code: '269', name: 'Quận 3'}, {code: '270', name: 'Quận 7'}],
            '48': [{code: '161', name: 'Hải Châu'}, {code: '162', name: 'Thanh Khê'}]
        };

        fetch('https://provinces.open-api.vn/api/?depth=1')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                data.forEach(item => {
                    const option = document.createElement('option');
                    option.value = item.code;
                    option.textContent = item.name;
                    provinceSelect.appendChild(option);
                });
                console.log('Đã load thành công', data.length, 'tỉnh/thành phố');
            })
            .catch(error => console.error('Error loading provinces:', error));

        provinceSelect.addEventListener('change', function () {
            const provinceCode = this.value;
            districtSelect.innerHTML = '<option value="" disabled selected>Đang tải huyện...</option>';
            if (provinceCode) {
                fetch(`https://esgoo.net/api-quanhuyen?tinhthanh=${provinceCode}`)
                    .then(response => {
                        if (!response.ok) throw new Error('Network response was not ok');
                        return response.json();
                    })
                    .then(data => {
                        districtSelect.innerHTML = '<option value="" disabled selected>Chọn huyện</option>';
                        if (data.length > 0) {
                            data.forEach(item => {
                                const option = document.createElement('option');
                                option.value = item.id;
                                option.textContent = item.ten_quan_huyen;
                                districtSelect.appendChild(option);
                            });
                            console.log('Đã load thành công', data.length, 'huyện cho tỉnh', provinceCode);
                        } else {
                            loadFallbackDistricts(provinceCode);
                        }
                    })
                    .catch(error => {
                        console.error('Lỗi tải huyện từ Esgoo API cho tỉnh', provinceCode, ':', error);
                        loadFallbackDistricts(provinceCode);
                    });
            } else {
                districtSelect.innerHTML = '<option value="" disabled selected>Chọn huyện</option>';
            }
        });

        function loadFallbackDistricts(provinceCode) {
            districtSelect.innerHTML = '<option value="" disabled selected>Chọn huyện</option>';
            const districts = fallbackDistricts[provinceCode] || [{code: 'none', name: 'Không có dữ liệu'}];
            districts.forEach(item => {
                const option = document.createElement('option');
                option.value = item.code;
                option.textContent = item.name;
                districtSelect.appendChild(option);
            });
            console.log('Đã load fallback huyện cho tỉnh', provinceCode);
        }

        menuItems.forEach(item => {
            item.addEventListener('click', function (e) {
                menuItems.forEach(i => i.classList.remove('active'));
                this.classList.add('active');
                const text = this.querySelector('a').textContent.trim();

                profileForm.style.display = 'none';
                changePasswordForm.style.display = 'none';
                orderHistoryForm.style.display = 'none';
                voucherContainer.style.display = 'none';
                wishlistContainer.style.display = 'none';
                xuContainer.style.display = 'none';
                notificationContainer.style.display = 'none';

                if (text === 'Đổi mật khẩu') {
                    changePasswordForm.style.display = 'block';
                } else if (text === 'Đơn hàng của tôi') {
                    orderHistoryForm.style.display = 'block';
                } else if (text === 'Ví voucher') {
                    voucherContainer.style.display = 'block';
                    const allVouchers = voucherContainer.querySelectorAll('.voucher-card');
                    allVouchers.forEach(v => {
                        v.style.display = v.classList.contains('used') ? 'none' : 'block';
                    });
                } else if (text === 'Sản phẩm yêu thích') {
                    wishlistContainer.style.display = 'block';
                } else if (text === 'Ưu đãi C-Point') {
                    xuContainer.style.display = 'block'; // Show xu-container
                } else if (text == 'Thông báo') {
                    notificationContainer.style.display = 'block';
                    loadNotifications();
                } else {
                    profileForm.style.display = 'block'; // Default to profile form
                }
                e.preventDefault();
            });
        });
        menuItems[0].classList.add('active');
        profileForm.style.display = 'block';
        changePasswordForm.style.display = 'none';
        orderHistoryForm.style.display = 'none';
        voucherContainer.style.display = 'none';
        wishlistContainer.style.display = 'none';
        xuContainer.style.display = 'none';

        const tabsContainer = document.querySelector('.tabs-container');
        const scrollLeftBtn = document.querySelector('.scroll-left');
        const scrollRightBtn = document.querySelector('.scroll-right');

        scrollLeftBtn.addEventListener('click', () => {
            tabsContainer.scrollBy({left: -150, behavior: 'smooth'});
        });

        scrollRightBtn.addEventListener('click', () => {
            tabsContainer.scrollBy({left: 150, behavior: 'smooth'});
        });
        const filterTabs = document.querySelectorAll('.filter-tab');
        const orderItems = document.querySelectorAll('.order-item');
        const noOrdersMessage = document.querySelector('.no-orders');

        filterTabs.forEach(tab => {
            tab.addEventListener('click', function () {
                filterTabs.forEach(t => t.classList.remove('active'));
                this.classList.add('active');

                const filterValue = this.getAttribute('data-filter');
                let visibleCount = 0;

                orderItems.forEach(item => {
                    const status = item.getAttribute('data-status');
                    if (filterValue === 'all' || status === filterValue) {
                        item.style.display = 'block';
                        visibleCount++;
                    } else {
                        item.style.display = 'none';
                    }
                });

                noOrdersMessage.style.display = visibleCount === 0 ? 'block' : 'none';
            });
        });
        document.querySelector('.filter-tab.active').click();
        document.addEventListener('click', function (e) {
            if (e.target.classList.contains('copy-code-btn') && !e.target.disabled) {
                const code = e.target.closest('.voucher-card').querySelector('.voucher-code').textContent;
                navigator.clipboard.writeText(code).then(() => {
                    alert('Đã copy mã: ' + code);
                }).catch(() => {
                    alert('Không thể copy mã. Vui lòng copy thủ công.');
                });
            }
        });
        document.addEventListener('click', function (e) {
            if (e.target.classList.contains('voucher-detail-btn')) {
                const title = e.target.closest('.voucher-card').querySelector('.voucher-title').textContent;
                alert('Chi tiết voucher: ' + title + '\n(Ở đây có thể mở modal hoặc trang mới)');
            }
        });
        document.addEventListener('click', function (e) {
            if (e.target.classList.contains('add-to-cart-btn')) {
                const title = e.target.closest('.wishlist-item').querySelector('h3').textContent;
                alert('Đã thêm "' + title.trim() + '" vào giỏ hàng!');
            } else if (e.target.classList.contains('remove-wishlist-btn')) {
                const item = e.target.closest('.wishlist-item');
                if (confirm('Bạn có chắc muốn xóa sản phẩm này khỏi danh sách yêu thích?')) {
                    item.style.display = 'none';
                }
            }
        });
    });
</script>


<script>
    const bellIcon = document.querySelector('.bell-icon');
    const notificationPanel = document.querySelector('.notification-panel');

    if (bellIcon && notificationPanel) {
        bellIcon.addEventListener('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            notificationPanel.style.display = (notificationPanel.style.display === 'block') ? 'none' : 'block';
        });
        document.addEventListener('click', function (e) {
            if (!bellIcon.contains(e.target) && !notificationPanel.contains(e.target)) {
                notificationPanel.style.display = 'none';
            }
        });
        notificationPanel.addEventListener('click', function (e) {
            e.stopPropagation();
        });
    }
</script>

<script>
    document.querySelectorAll('.notification-container .tab').forEach(tab => {
        tab.addEventListener('click', function () {
            document.querySelectorAll('.notification-container .tab').forEach(t =>
                t.classList.remove('active')
            );
            this.classList.add('active');
        });
    });
</script>

<script>
    async function loadNotifications() {
        const container = document.querySelector('.notification-container');
        if (!container || container.style.display === 'none') return;
        const activeTab = container.querySelector('.tab.active');
        let type = 'ALL';
        if (activeTab) {
            const tabText = activeTab.textContent.trim();
            if (tabText === 'Đơn Hàng') type = 'ORDER';
            if (tabText === 'Sự kiện') type = 'EVENT';
        }
        let listArea = document.getElementById('notification-list-area');
        if (!listArea) {
            container.innerHTML = `
            <div class="tabs">
                <div class="tab active">Tất cả</div>
                <div class="tab">Đơn Hàng</div>
                <div class="tab">Sự kiện</div>
            </div>
            <div class="line" style="height:1px;background:#eee;margin:10px 0;"></div>
            <div id="notification-list-area" style="min-height:300px;"></div>
            <div style="text-align:center;padding:20px;">
                <button id="mark-all-read-btn" style="padding:10px 20px;background:#007bff;color:white;border:none;border-radius:4px;cursor:pointer;">Đánh dấu tất cả đã đọc</button>
            </div>
        `;
            listArea = document.getElementById('notification-list-area');
            document.querySelectorAll('.notification-container .tab').forEach(tab => {
                tab.addEventListener('click', function () {
                    document.querySelectorAll('.notification-container .tab').forEach(t => t.classList.remove('active'));
                    this.classList.add('active');
                    loadNotifications();
                });
            });
            document.getElementById('mark-all-read-btn').addEventListener('click', markAllRead);
        }

        listArea.innerHTML = '<div style="text-align:center;padding:60px;color:#999;">Đang tải thông báo...</div>';

        try {
            const response = await fetch('${pageContext.request.contextPath}/NotificationServlet/list?type=' + type);
            if (!response.ok) throw new Error('HTTP ' + response.status);

            const data = await response.json();

            if (!data.notifications || data.notifications.length === 0) {
                listArea.innerHTML = '<div style="text-align:center;padding:80px;color:#999;font-size:16px;">Không có thông báo nào.</div>';
                document.getElementById('mark-all-read-btn').style.display = 'none';
                return;
            }

            document.getElementById('mark-all-read-btn').style.display = 'block';

            let html = '';
            data.notifications.forEach(n => {
                const unreadStyle = n.is_read ? '' : 'background:#f0f8ff; font-weight:600; border-left:4px solid #007bff; padding-left:16px;';
                html += `
                <div style="padding:16px; margin-bottom:8px; border-bottom:1px solid #eee; border-radius:6px; cursor:pointer; ${unreadStyle}"
                     onclick="markReadAndGo(${n.id}, '${n.link}')">
                    <strong style="font-size:16px;display:block;margin-bottom:6px;">${n.title}</strong>
                    <div style="color:#555;font-size:14px;margin-bottom:10px;">${n.message}</div>
                    <small style="color:#999;font-size:13px;">${n.formatted_date}</small>
                </div>
            `;
            });

            listArea.innerHTML = html;

        } catch (err) {
            console.error('Lỗi load thông báo:', err);
            listArea.innerHTML = '<div style="text-align:center;padding:60px;color:red;">Lỗi tải thông báo<br><small>Vui lòng làm mới trang</small></div>';
        }
    }

    async function markReadAndGo(id, link) {
        try {
            await fetch('${pageContext.request.contextPath}/NotificationServlet/mark-read?id=' + id, {method: 'POST'});
            loadNotifications(); // refresh danh sách
            if (link && link !== '#' && link !== '') {
                window.location.href = link;
            }
        } catch (err) {
            alert('Lỗi đánh dấu đã đọc');
        }
    }

    async function markAllRead() {
        if (!confirm('Đánh dấu tất cả thông báo là đã đọc?')) return;
        try {
            await fetch('${pageContext.request.contextPath}/NotificationServlet/mark-all-read', {method: 'POST'});
            loadNotifications();
        } catch (err) {
            alert('Lỗi thực hiện');
        }
    }
</script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        if (window.location.hash === '#notifications') {
            const menuItems = document.querySelectorAll('.menu ul li');
            menuItems.forEach(item => {
                const linkText = item.querySelector('a').textContent.trim();
                if (linkText === 'Thông báo') {
                    menuItems.forEach(i => i.classList.remove('active'));
                    item.classList.add('active');
                    document.querySelectorAll('.profile-form-container, .change-password-container, .order-history-container, .voucher-container, .wishlist-container, .xu-container, .notification-container')
                        .forEach(el => el.style.display = 'none');
                    document.querySelector('.notification-container').style.display = 'block';
                    if (typeof loadNotifications === 'function') {
                        loadNotifications();
                    }
                }
            });
        }
    });
</script>
<script>
    document.querySelectorAll('.remove-wishlist-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const comicId = this.dataset.comicId;
            const item = this.closest('.wishlist-item');

            fetch('${pageContext.request.contextPath}/wishlist', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: 'action=remove&comic_id=' + comicId
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        item.remove();
                        alert(data.message);
                        // Cập nhật số lượng
                        document.querySelector('h2').innerHTML =
                            `Sản phẩm yêu thích (${data.count} sản phẩm)`;
                    } else {
                        alert(data.message);
                    }
                });
        });
    });
</script>


<script>
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('remove-wishlist-btn') ||
            e.target.closest('.remove-wishlist-btn')) {

            const btn = e.target.classList.contains('remove-wishlist-btn') ?
                e.target : e.target.closest('.remove-wishlist-btn');
            const comicId = btn.dataset.comicId;
            const item = btn.closest('.wishlist-item');

            if (!confirm('Bạn có chắc muốn xóa sản phẩm này khỏi danh sách yêu thích?')) {
                return;
            }
            btn.disabled = true;
            const originalHTML = btn.innerHTML;
            btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xóa...';

            fetch('${pageContext.request.contextPath}/WishlistServlet', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                credentials: 'include',
                body: 'action=remove&comic_id=' + comicId
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        // Hiệu ứng fade out
                        item.style.transition = 'all 0.3s ease';
                        item.style.opacity = '0';
                        item.style.transform = 'scale(0.8)';

                        setTimeout(() => {
                            item.remove();

                            // Cập nhật số lượng trong tiêu đề
                            const titleElement = document.querySelector('.wishlist-container h2');
                            if (titleElement) {
                                titleElement.textContent = 'Sản phẩm yêu thích (' + data.count + ' sản phẩm)';
                            }

                            // Kiểm tra nếu không còn sản phẩm nào thì reload
                            const grid = document.querySelector('.wishlist-grid');
                            if (grid && grid.children.length === 0) {
                                location.reload();
                            }

                            // Hiển thị thông báo
                            showWishlistToast(data.message, 'success');
                        }, 300);
                    } else {
                        alert(data.message);
                        btn.disabled = false;
                        btn.innerHTML = originalHTML;
                    }
                })
                .catch(err => {
                    console.error('Lỗi:', err);
                    alert('Lỗi kết nối, vui lòng thử lại');
                    btn.disabled = false;
                    btn.innerHTML = originalHTML;
                });
        }
    });
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('add-to-cart-btn') ||
            e.target.closest('.add-to-cart-btn')) {

            e.preventDefault(); // Ngăn hành vi mặc định

            const btn = e.target.classList.contains('add-to-cart-btn') ?
                e.target : e.target.closest('.add-to-cart-btn');
            const comicId = btn.dataset.comicId;
            const comicName = btn.closest('.wishlist-item').querySelector('h3 a').textContent;
            if (btn.disabled) {
                showWishlistToast('Sản phẩm đã hết hàng', 'error');
                return;
            }
            btn.disabled = true;
            const originalHTML = btn.innerHTML;
            btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang thêm...';

            // Gọi API thêm vào giỏ
            fetch('${pageContext.request.contextPath}/cart?action=add&comicId=' + comicId + '&quantity=1&ajax=true', {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        showAddToCartPopup(comicName, data.cartCount);
                        updateCartCountBadge(data.cartCount);
                        btn.disabled = false;
                        btn.innerHTML = originalHTML;
                    } else {
                        showWishlistToast(data.message || 'Không thể thêm vào giỏ', 'error');
                        btn.disabled = false;
                        btn.innerHTML = originalHTML;
                    }
                })
                .catch(err => {
                    console.error('Lỗi:', err);
                    showWishlistToast('Lỗi kết nối, vui lòng thử lại', 'error');
                    btn.disabled = false;
                    btn.innerHTML = originalHTML;
                });
        }
    });

    function showAddToCartPopup(productName, cartCount) {
        // Xóa popup cũ nếu có
        const oldPopup = document.querySelector('.cart-success-popup');
        if (oldPopup) oldPopup.remove();

        // Tạo popup mới
        const popup = document.createElement('div');
        popup.className = 'cart-success-popup';
        popup.innerHTML = `
        <div class="cart-popup-overlay"></div>
        <div class="cart-popup-content">
            <div class="cart-popup-icon">
                <i class="fas fa-check-circle"></i>
            </div>
            <h3>Đã thêm vào giỏ hàng!</h3>
            <p class="product-name">${productName}</p>
            <div class="cart-info">
                <span>Giỏ hàng hiện có: <strong>${cartCount} sản phẩm</strong></span>
            </div>
            <div class="cart-popup-actions">
                <button class="btn-continue" onclick="closeCartPopup()">
                    <i class="fas fa-arrow-left"></i> Tiếp tục mua
                </button>
                <a href="${pageContext.request.contextPath}/cart" class="btn-view-cart">
                    <i class="fas fa-shopping-cart"></i> Xem giỏ hàng
                </a>
            </div>
        </div>
    `;

        document.body.appendChild(popup);

        // Hiển thị với animation
        setTimeout(() => popup.classList.add('show'), 10);

        // Tự động đóng sau 5 giây
        setTimeout(() => {
            closeCartPopup();
        }, 5000);
    }

    // Đóng popup
    function closeCartPopup() {
        const popup = document.querySelector('.cart-success-popup');
        if (popup) {
            popup.classList.remove('show');
            setTimeout(() => popup.remove(), 300);
        }
    }

    function updateCartCountBadge(count) {
        const badge = document.querySelector('.cart-count, .cart-badge, #cart-count');
        if (badge) {
            badge.textContent = count;
            badge.classList.add('bounce');
            setTimeout(() => badge.classList.remove('bounce'), 500);
        }
    }
    function showWishlistToast(message, type = 'success') {
        const oldToast = document.querySelector('.wishlist-toast');
        if (oldToast) {
            oldToast.remove();
        }

        const icons = {
            success: '<i class="fas fa-check-circle"></i>',
            error: '<i class="fas fa-exclamation-circle"></i>',
            info: '<i class="fas fa-info-circle"></i>'
        };

        const toast = document.createElement('div');
        toast.className = 'wishlist-toast ' + type;
        toast.innerHTML = icons[type] + ' ' + message;
        document.body.appendChild(toast);

        setTimeout(() => toast.classList.add('show'), 10);

        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }
    document.querySelectorAll('.wishlist-container a[href*="login"], .wishlist-container a[href*="home"]').forEach(link => {
        link.addEventListener('mouseenter', function() {
            this.style.transform = 'scale(1.05)';
            this.style.boxShadow = '0 4px 12px rgba(0,123,255,0.3)';
        });
        link.addEventListener('mouseleave', function() {
            this.style.transform = 'scale(1)';
            this.style.boxShadow = 'none';
        });
    });
</script>

</body>
</html>