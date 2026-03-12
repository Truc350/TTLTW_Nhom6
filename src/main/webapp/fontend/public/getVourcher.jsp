<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="vn.edu.hcmuaf.fit.ttltw_nhom6.model.Banner" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="../css/publicCss/nav.css">
    <link rel="stylesheet" href="../css/UserBCss/profile.css">
    <link rel="stylesheet" href="../css/publicCss/getVourcher.css">
    <link rel="stylesheet" href="../css/publicCss/FooterStyle.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body>
<jsp:include page="/fontend/public/header.jsp"/>

    <div class="container">
        <div class="voucher-group">

            <div class="voucher-time">
                <h1>Ưu đãi khung giờ ngập tràn</h2>
                    <div class="voucher-card" data-voucher='{
                        "value": "Giảm 10.000 VNĐ",
                        "condition": "Đơn hàng từ 130.000 VNĐ",
                        "exclude": "Manga, Ngoại văn,...",
                        "expiry": "30/11/2025",
                        "status": "Chưa sử dụng"
                        "code": "FHS10KT11",
                    }'>
                        <div class="voucher-header">
                            <span class="voucher-type">Voucher chưa sử dụng</span>
                            <span class="voucher-status unused">Chưa sử dụng</span>
                        </div>
                        <div class="voucher-body">
                            <div class="voucher-title">Mã Giảm 10K - Toàn Sàn</div>
                            <div class="voucher-desc">Đơn hàng từ 130k - Không bao gồm giá trị của các sản phẩm sau
                                Manga,
                                Ngoại
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
                    <div class="voucher-card" data-voucher='{
                        "value": "Giảm 20.000 VNĐ",
                        "condition": "Đơn hàng từ 240.000 VNĐ",
                        "exclude": "Manga, Ngoại văn,...",
                        "expiry": "30/11/2025",
                        "status": "Chưa sử dụng"
                        "code": "FHS20KT11",
                    }'>
                        <div class="voucher-header">
                            <span class="voucher-type">Voucher chưa sử dụng</span>
                            <span class="voucher-status unused">Chưa sử dụng</span>
                        </div>
                        <div class="voucher-body">
                            <div class="voucher-title">Mã Giảm 20K - Toàn Sàn</div>
                            <div class="voucher-desc">Đơn hàng từ 240k - Không bao gồm giá trị của các sản phẩm sau
                                Manga,
                                Ngoại
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
                    <div class="voucher-card" data-voucher='{
                        "value": "Giảm 40.000 VNĐ",
                        "condition": "Đơn hàng từ 490.000 VNĐ",
                        "exclude": "Manga, Ngoại văn,...",
                        "expiry": "30/11/2025",
                        "status": "Chưa sử dụng"
                        "code": "FHS40KT11",
                    }'>
                        <div class="voucher-header">
                            <span class="voucher-type">Voucher chưa sử dụng</span>
                            <span class="voucher-status unused">Chưa sử dụng</span>
                        </div>
                        <div class="voucher-body">
                            <div class="voucher-title">Mã Giảm 40K - Toàn Sàn</div>
                            <div class="voucher-desc">Đơn hàng từ 490k - Không bao gồm giá trị của các sản phẩm sau
                                Manga,
                                Ngoại
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
                    <div class="voucher-card" data-voucher='{
                        "value": "Giảm 80.000 VNĐ",
                        "condition": "Đơn hàng từ 990.000 VNĐ",
                        "exclude": "Manga, Ngoại văn,...",
                        "expiry": "30/11/2025",
                        "status": "Chưa sử dụng"
                        "code": "FHS80KT11",
                    }'>
                        <div class="voucher-header">
                            <span class="voucher-type">Voucher chưa sử dụng</span>
                            <span class="voucher-status unused">Chưa sử dụng</span>
                        </div>
                        <div class="voucher-body">
                            <div class="voucher-title">Mã Giảm 80K - Toàn Sàn</div>
                            <div class="voucher-desc">Đơn hàng từ 990k - Không bao gồm giá trị của các sản phẩm sau
                                Manga,
                                Ngoại
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

            <div class="voucher-ship">
                <h1>Ưu đãi vận chuyển ngập tràn</h1>
                <div class="voucher-card" data-voucher='{
                    "value": "Giảm 10.000 VNĐ",
                    "condition": "Đơn hàng từ 130.000 VNĐ",
                    "exclude": "Manga, Ngoại văn,...",
                    "expiry": "30/11/2025",
                    "status": "Chưa sử dụng"
                    "code": "FHS10KT11",
                }'>
                    <div class="voucher-header">
                        <span class="voucher-type">Voucher chưa sử dụng</span>
                        <span class="voucher-status unused">Chưa sử dụng</span>
                    </div>
                    <div class="voucher-body">
                        <div class="voucher-title">Mã Giảm 10K - Toàn Sàn</div>
                        <div class="voucher-desc">Đơn hàng từ 130k - Áp dụng cho tàn sàn</div>
                        <div class="voucher-code">FHS10KT11</div>
                        <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                    </div>
                    <div class="voucher-footer">
                        <span class="voucher-expiry">HSD: 30/11/2025</span>
                        <button class="copy-code-btn">Copy mã</button>
                    </div>
                </div>
                <div class="voucher-card" data-voucher='{
                    "value": "Giảm 20.000 VNĐ",
                    "condition": "Đơn hàng từ 200.000 VNĐ",
                    "exclude": "Truyên hành động",
                    "expiry": "15/12/2025",
                    "status": "Chưa sử dụng"
                    "code": "SHIP20K2025",
                }'>
                    <div class="voucher-header">
                        <span class="voucher-type">Voucher chưa sử dụng</span>
                        <span class="voucher-status unused">Chưa sử dụng</span>
                    </div>
                    <div class="voucher-body">
                        <div class="voucher-title">Mã Giảm 20K - Vận chuyển</div>
                        <div class="voucher-desc">Đơn hàng từ 200k - Ap dụng cho tất cả</div>
                        <div class="voucher-code">SHIP20K2025</div>
                        <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                    </div>
                    <div class="voucher-footer">
                        <span class="voucher-expiry">HSD: 15/12/2025</span>
                        <button class="copy-code-btn">Copy mã</button>
                    </div>
                </div>
                <div class="voucher-card" data-voucher='{
                    "value": "Miễn phí vận chuyển",
                    "condition": "Đơn hàng từ 50.000 VNĐ",
                    "exclude": "Không áp dụng cho đơn hàng COD",
                    "expiry": "31/12/2025",
                    "status": "Chưa sử dụng"
                    "code": "FREESHIP50",
                }'>
                    <div class="voucher-header">
                        <span class="voucher-type">Voucher chưa sử dụng</span>
                        <span class="voucher-status unused">Chưa sử dụng</span>
                    </div>
                    <div class="voucher-body">
                        <div class="voucher-title">Miễn phí vận chuyển</div>
                        <div class="voucher-desc">Đơn hàng từ 50k - Không áp dụng cho COD</div>
                        <div class="voucher-code">FREESHIP50</div>
                        <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                    </div>
                    <div class="voucher-footer">
                        <span class="voucher-expiry">HSD: 31/12/2025</span>
                        <button class="copy-code-btn">Copy mã</button>
                    </div>
                </div>
                <div class="voucher-card" data-voucher='{
                    "value": "Giảm 5.000 VNĐ",
                    "condition": "Không yêu cầu giá trị đơn hàng",
                    "exclude": "Không áp dụng cho giao hàng tiêu chuẩn",
                    "expiry": "01/01/2026",
                    "status": "Chưa sử dụng"
                    "code": "SHIP5KFAST",
                }'>
                    <div class="voucher-header">
                        <span class="voucher-type">Voucher chưa sử dụng</span>
                        <span class="voucher-status unused">Chưa sử dụng</span>
                    </div>
                    <div class="voucher-body">
                        <div class="voucher-title">Giảm 5K - Giao nhanh</div>
                        <div class="voucher-desc">Không yêu cầu giá trị đơn hàng - Chỉ áp dụng cho giao nhanh</div>
                        <div class="voucher-code">SHIP5KFAST</div>
                        <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                    </div>
                    <div class="voucher-footer">
                        <span class="voucher-expiry">HSD: 01/01/2026</span>
                        <button class="copy-code-btn">Copy mã</button>
                    </div>
                </div>
                <div class="voucher-card" data-voucher='{
                    "value": "Giảm 15.000 VNĐ",
                    "condition": "Dành cho khách hàng mới",
                    "exclude": "Không áp dụng cho đơn hàng combo",
                    "expiry": "10/12/2025",
                    "status": "Chưa sử dụng"
                    "code": "SHIP15KNEW",
                }'>
                    <div class="voucher-header">
                        <span class="voucher-type">Voucher chưa sử dụng</span>
                        <span class="voucher-status unused">Chưa sử dụng</span>
                    </div>
                    <div class="voucher-body">
                        <div class="voucher-title">Giảm 15K - Khách mới</div>
                        <div class="voucher-desc">Chỉ áp dụng cho khách hàng mới - Không áp dụng cho combo</div>
                        <div class="voucher-code">SHIP15KNEW</div>
                        <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                    </div>
                    <div class="voucher-footer">
                        <span class="voucher-expiry">HSD: 10/12/2025</span>
                        <button class="copy-code-btn">Copy mã</button>
                    </div>
                </div>
            </div>

            <div class="voucher-other">
                <h1>Ưu đãi ngoại lệ ngập tràn</h1>
                <div class="voucher-card" data-voucher='{
                    "exclude": "Không áp dụng cho combo",
                    "expiry": "30/11/2025",
                    "status": "Chưa sử dụng",
                    "code": "KID10K2025"
                    "value": "Giảm 10.000 VNĐ",
                    "condition": "Áp dụng cho truyện trinh thám",
                }'>
                    <div class="voucher-header">
                        <span class="voucher-type">Voucher chưa sử dụng</span>
                        <span class="voucher-status unused">Chưa sử dụng</span>
                    </div>
                    <div class="voucher-body">
                        <div class="voucher-title">Giảm 10K - Truyện trinh thám</div>
                        <div class="voucher-desc">Áp dụng cho truyện trinh thám - Không áp dụng cho combo</div>
                        <div class="voucher-code">KID10K2025</div>
                        <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                    </div>
                    <div class="voucher-footer">
                        <span class="voucher-expiry">HSD: 30/11/2025</span>
                        <button class="copy-code-btn">Copy mã</button>
                    </div>
                </div>
                <div class="voucher-card" data-voucher='{
                    "exclude": "Không áp dụng cho truyện đang giảm giá",
                    "expiry": "15/12/2025",
                    "status": "Chưa sử dụng",
                    "code": "BUY3GET15K"
                    "value": "Giảm 15.000 VNĐ",
                    "condition": "Mua từ 3 cuốn truyện trở lên",
                }'>
                    <div class="voucher-header">
                        <span class="voucher-type">Voucher chưa sử dụng</span>
                        <span class="voucher-status unused">Chưa sử dụng</span>
                    </div>
                    <div class="voucher-body">
                        <div class="voucher-title">Giảm 15K - Mua 3 cuốn</div>
                        <div class="voucher-desc">Áp dụng khi mua từ 3 cuốn - Không áp dụng cho truyện đang giảm giá
                        </div>
                        <div class="voucher-code">BUY3GET15K</div>
                        <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                    </div>
                    <div class="voucher-footer">
                        <span class="voucher-expiry">HSD: 15/12/2025</span>
                        <button class="copy-code-btn">Copy mã</button>
                    </div>
                </div>
                <div class="voucher-card" data-voucher='{
                    "exclude": "Không áp dụng cho bản đặc biệt",
                    "expiry": "31/12/2025",
                    "status": "Chưa sử dụng",
                    "code": "BL20K2025"
                    "value": "Giảm 20.000 VNĐ",
                    "condition": "Áp dụng cho truyện hành động",
                }'>
                    <div class="voucher-header">
                        <span class="voucher-type">Voucher chưa sử dụng</span>
                        <span class="voucher-status unused">Chưa sử dụng</span>
                    </div>
                    <div class="voucher-body">
                        <div class="voucher-title">Giảm 20K - Truyện hành động</div>
                        <div class="voucher-desc">Áp dụng cho truyện hành động - Không áp dụng cho bản đặc biệt</div>
                        <div class="voucher-code">BL20K2025</div>
                        <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                    </div>
                    <div class="voucher-footer">
                        <span class="voucher-expiry">HSD: 31/12/2025</span>
                        <button class="copy-code-btn">Copy mã</button>
                    </div>
                </div>
                <div class="voucher-card" data-voucher='{
                    "exclude": "Không áp dụng cho đơn hàng Flash Sale",
                    "expiry": "01/01/2026",
                    "status": "Chưa sử dụng",
                    "code": "WEEKEND12K"
                    "value": "Giảm 12.000 VNĐ",
                    "condition": "Đơn hàng đặt vào Thứ 7 & Chủ Nhật",
                }'>
                    <div class="voucher-header">
                        <span class="voucher-type">Voucher chưa sử dụng</span>
                        <span class="voucher-status unused">Chưa sử dụng</span>
                    </div>
                    <div class="voucher-body">
                        <div class="voucher-title">Giảm 12K - Cuối tuần</div>
                        <div class="voucher-desc">Áp dụng vào Thứ 7 & Chủ Nhật - Không áp dụng cho Flash Sale</div>
                        <div class="voucher-code">WEEKEND12K</div>
                        <button class="voucher-detail-btn" onclick="showVoucherDetails(this)">Chi tiết</button>
                    </div>
                    <div class="voucher-footer">
                        <span class="voucher-expiry">HSD: 01/01/2026</span>
                        <button class="copy-code-btn">Copy mã</button>
                    </div>
                </div>
            </div>
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

                    <button class="use-voucher-btn">Thêm vào ví vourcher</button>
                </div>
            </div>

        </div>
    </div>

    <footer class="footer">
        <div class="footer-container">
            <div class="footer-column">
                <div class="logo">
                    <a href="#">
                        <img src="../../img/logo.png" alt="logo">
                    </a>
                </div>
                <p><b>ComicStore</b> là cửa hàng truyện tranh<br> trực tuyến hàng đầu Việt Nam<br> — nơi bạn có thể
                    mua
                    truyện
                    giấy,<br>
                    đọc truyện online và<br> khám phá thế giới<br> manga – manhwa – comic đa dạng.</p>
                <p>Thành lâp năm <strong>2025</strong>, chúng tôi mang đến hơn
                    <str>10.000+</str>
                    <br>
                    truyện hấp dẫn cho bạn
                </p>
            </div>

            <div class="footer-column">
                <h4><i class="fa-solid fa-link"></i> Liên kết nhanh</h4>
                <ul>
                    <li><a href="homePage.html">Trang chủ</a></li>
                    <li><a href="FlashSale.html">Khuyến mãi</a></li>
                    <li><a href="cart.html">Giỏ hàng</a></li>
                    <li><a href="chat.html">Liên hệ</a></li>
                </ul>
            </div>

            <div class="footer-column">
                <h4><i class="fa-solid fa-headset"></i> Hỗ trợ khách hàng</h4>
                <ul>
                    <li><a href="../nguoiB/RefundPolicy.html">Chính sách đổi trả</a></li>
                    <li><a href="../nguoiB/shippingPolicy.html">Chính sách vận chuyển</a></li>
                </ul>
            </div>

            <div class="footer-column">
                <h4><i class="fa-solid fa-envelope"></i> Liên hệ</h4>
                <p><i class="fa-solid fa-envelope"></i> support@metruyen.vn</p>
                <p><i class="fa-solid fa-phone"></i> 0123 456 789</p>
                <p><i class="fa-solid fa-location-dot"></i> 123 Nguyễn Huệ, Q.1, TP.HCM</p>

                <div class="social-links">
                    <a href="https://www.facebook.com/share/1MVc1miHnd/" title="Facebook"><i
                            class="fab fa-facebook-f"></i></a>
                    <a href="https://www.instagram.com/comic.store/" title="Instagram"><i
                            class="fab fa-instagram"></i></a>
                    <a href="https://www.tiktok.com/@comics_store.oficial" title="TikTok"><i
                            class="fab fa-tiktok"></i></a>
                </div>
            </div>

            <div class="footer-column">
                <h4><i class="fa-solid fa-shield-halved"></i> Thanh toán & Bảo mật</h4>
                <p>Hỗ trợ thanh toán qua:</p>
                <div class="payment-icons">
                    <img src="../../img/momo.png" alt="Momo">
                    <img src="../../img/zalopay.png" alt="ZaloPay">
                </div>
                <p>Website đã đăng ký với Bộ Công Thương.</p>
            </div>
        </div>

        <div class="footer-bottom">
            <p>© 2025 <strong>ComicStore.vn</strong> — All rights reserved.</p>
        </div>
    </footer>



    <script>
        function showVoucherDetails(button) {
            const voucherCard = button.closest('.voucher-card');
            let voucherData = voucherCard.getAttribute('data-voucher');
            voucherData = voucherData
                .replace(/(\s*"status":\s*"[^"]+")(\s*"code":)/, '$1,$2') // thêm dấu phẩy giữa status và code
                .replace(/,\s*}/, '}'); // loại bỏ dấu phẩy cuối cùng nếu có

            const voucher = JSON.parse(voucherData);
            document.getElementById('detail-code').textContent = voucher.code || '';
            document.getElementById('detail-value').textContent = voucher.value || '';
            document.getElementById('detail-condition').textContent = voucher.condition || '';
            document.getElementById('detail-exclude').textContent = voucher.exclude || '';
            document.getElementById('detail-expiry').textContent = voucher.expiry || '';
            document.getElementById('detail-status').textContent = voucher.status || '';
            document.getElementById('voucher-detail-popup').style.display = 'block';
        }

        function closeVoucherPopup() {
            document.getElementById('voucher-detail-popup').style.display = 'none';
        }

    </script>
</body>

</html>