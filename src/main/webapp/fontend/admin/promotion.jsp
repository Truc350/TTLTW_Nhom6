<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Khuyến mãi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/fontend/css/adminCss/stylePromotion.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/fontend/css/adminCss/adminHeader.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/fontend/css/adminCss/styleSidebar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<div class="container">
    <jsp:include page="/fontend/admin/ASide.jsp"/>
    <div class="main-content">
        <%@ include file="HeaderAdmin.jsp" %>
        <div class="promotion-page">
            <div class="promo-top">
                <div class="search-box">
                    <input type="text" id="searchInput" placeholder="Tìm kiếm mã...">
                    <i class="fas fa-magnifying-glass"></i>
                </div>
                <button class="btn-add" id="openAddPopup">
                    <i class="fas fa-plus"></i> Thêm mã
                </button>
                <select id="statusFilter">
                    <option value="">Tất cả trạng thái</option>
                    <option value="running">Đang chạy</option>
                    <option value="expired">Hết hạn</option>
                    <option value="out">Hết lượt</option>
                </select>
            </div>
            <table class="promo-table">
                <thead>
                <tr>
                    <th>Mã giảm giá</th>
                    <th>Giá trị</th>
                    <th>Áp dụng</th>
                    <th>Đã dùng/Tổng</th>
                    <th>Đơn tối thiểu</th>
                    <th>Hạn sử dụng</th>
                    <th>Trạng thái</th>
                    <th></th>
                </tr>
                </thead>
                <tbody id="promoTableBody">

                <c:forEach var="voucher" items="${allVouchers}">
                    <tr>
                        <td>${voucher.code}</td>
                        <td>${voucher.discountValue}</td>
                        <td>${voucher.discountTarget}</td>
                        <td>${voucher.usedCount}/${voucher.quantity}</td>
                        <td>${voucher.minOrderAmount}</td>
                        <td>${voucher.endDate}</td>

                        <c:choose>
                            <c:when test="${voucher.expired}">
                                <td><span class="status-out">Hết Hạn</span></td>
                            </c:when>
                            <c:when test="${voucher.usedCount == voucher.quantity}">
                                <td><span class="status-out">Hết lượt</span></td>
                            </c:when>

                            <c:otherwise>
                                <td><span class="status-active">Đang chạy</span></td>
                            </c:otherwise>
                        </c:choose>

                        <td>
                            <button class="edit-btn" onclick="openEdit('${voucher.code}')"><i class="fa-solid fa-pen-to-square"></i></button>
                            <button class="delete-btn" onclick="openDeletePopup('${voucher.code}')">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>

                <!-- Phân trang -->
                <tr class="pagination-row">
                    <td colspan="10">
                        <div class="pagination" id="tablePagination">
                            <button class="page-btn pro-page" data-page="1">1</button>
                            <button class="page-btn pro-page" data-page="2">2</button>
                            <button class="page-btn pro-page" data-page="3">3</button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>

        </div>
    </div>
</div>
<div class="popup-overlay" id="addPopup">
    <div class="popup-box">
        <form action="${pageContext.request.contextPath}/admin/vouchers" method="post">
            <h3>Thêm mã khuyến mãi</h3>

            <div class="popup-grid">
                <!-- Dòng 1: Mã + Loại -->
                <div>
                    <label>Tên mã</label>
                    <input type="text" id="addCode" placeholder="Nhập mã" maxlength="20" name="code">
                </div>
                <div>
                    <label>Áp dụng</label>
                    <select id="addType" name="discount_target"  required>
                        <option value="Vận chuyển">Vận chuyển</option>
                        <option value="Giảm giá">Giảm giá</option>
                    </select>
                </div>
                <div>
                    <label>Loại</label>
                    <select id="addType"  name="discount_type" required>
                        <option value="">Chọn loại</option>
                        <option value="percent">Phần trăm (%)</option>
                        <option value="fixed">Số tiền (₫)</option>
                    </select>
                </div>

                <!-- Dòng 2: Giá trị + Đơn tối thiểu -->
                <div>
                    <label>Giá trị giảm</label>
                    <input type="number" id="addValue" min="0" name="discount_value" placeholder="10 hoặc 20000">
                </div>
                <div>
                    <label>Đơn tối thiểu</label>
                    <input type="number" id="addMinOrder" min="0" name="min_order_amount" placeholder="0" value="0">
                </div>

                <!-- Dòng 3: Số lượng + Áp dụng cho -->
                <div>
                    <label>Số lượng</label>
                    <input type="number" id="addMaxUsage" name="quantity" min="1" value="100">
                </div>
                <div>
                    <label>Áp dụng</label>
                    <select id="addApply" name="apply_scope">
                        <option value="all">Toàn bộ sản phẩm</option>
                        <option value="category">Một thể loại</option>
                    </select>
                </div>

                <!-- Checkbox 1 lần/khách -->
                <div class="checkbox-row">
                    <input type="checkbox" id="addSingleUse" name="is_single_use">
                    <label for="addSingleUse">Mỗi khách chỉ dùng 1 lần</label>
                </div>

                <!-- Chọn thể loại (ẩn mặc định) -->
                <div id="addCategoryBox" style="grid-column: 1 / -1;">
                    <label>Thể loại</label>
                    <select id="addCategory" name="cate" disabled>
                        <option value="">-- Chọn thể loại --</option>
                        <option>Trinh thám</option>
                        <option>Hài hước</option>
                        <option>Ngôn tình</option>
                        <option>Hành động</option>
                        <option>Kinh dị</option>
                        <option>Phiêu lưu</option>
                        <option>Học đường</option>
                        <option>Giả tưởng</option>
                    </select>
                </div>

                <!-- Ngày bắt đầu + kết thúc -->
                <div class="date-row">
                    <div>
                        <label>Từ ngày</label>
                        <input type="datetime-local" id="addStart" name="start_date">
                    </div>
                    <div>
                        <label>Đến ngày</label>
                        <input type="datetime-local" id="addEnd" name="end_date">
                    </div>
                </div>
            </div>

            <div class="btn-row">
                <button type="submit" class="btn-cancel" id="closeAddPopup">Hủy</button>
                <button type="submit" class="btn-save" id="saveAddBtn">Tạo mã</button>
            </div>
        </form>
    </div>
</div>
<div class="popup-overlay" id="editPopup">
    <div class="popup-box">
        <h3>Sửa mã khuyến mãi</h3>
        <div class="popup-grid">
            <div>
                <label>Tên mã</label>
                <p id="editCode" class="readonly"></p>
            </div>
            <div>
                <label>Loại giảm giá</label>
                <p id="editTypeDisplay" class="readonly"></p>
            </div>
            <div>
                <label>Giá trị giảm</label>
                <p id="editValueDisplay" class="readonly"></p>
            </div>
            <div>
                <label>Áp dụng cho</label>
                <p id="editApplyDisplay" class="readonly">Toàn bộ sản phẩm</p>
            </div>
            <div>
                <label>Đơn tối thiểu</label>
                <input type="number" id="editMinOrder" min="0" placeholder="0">
            </div>
            <div>
                <label>Số lượng tối đa</label>
                <input type="number" id="editMaxUsage" min="1">
            </div>
            <div class="checkbox-row">
                <input type="checkbox" id="editSingleUse">
                <label for="editSingleUse">Mỗi khách chỉ dùng được 1 lần</label>
            </div>
            <div class="date-row">
                <div>
                    <label>Từ ngày</label>
                    <input type="date" id="editStart">
                </div>
                <div>
                    <label>Đến ngày</label>
                    <input type="date" id="editEnd">
                </div>
            </div>
        </div>

        <div class="btn-row">
            <button class="btn-cancel" id="closeEditPopup">Hủy</button>
            <button class="btn-save" id="saveEditBtn">Cập nhật</button>
        </div>
    </div>
</div>
<div class="popup-overlay" id="deleteConfirmPopup">
    <form  action="${pageContext.request.contextPath}/admin/deleteVoucher" method="POST">

        <input type="hidden" name="deleted-code" id="deleteCode">

        <div class="popup-box small">
            <i class="fa-solid fa-triangle-exclamation"></i>
            <h3>Xác nhận xóa</h3>
            <p>Bạn có chắc muốn xóa mã <b id="deleteVoucherCode"></b> này không?</p>
            <div class="btn-row">
                <button class="btn-cancel" id="cancelDeleteBtn">Không</button>
                <button class="btn-save" id="confirmDeleteBtn" type="submit">Có</button>
            </div>
        </div>
    </form>
</div>

<c:if test = "${not empty message}" >
    <script type="text/javascript">
        window.onload = function () {
            setTimeout(() => {
                alert("${message}");
            }, 300);
        }
    </script>
</c:if>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const addPopup       = document.getElementById('addPopup');
        const addApply       = document.getElementById('addApply');
        const addCategoryBox = document.getElementById('addCategoryBox');
        const tbody          = document.getElementById('promoTableBody');
        const editPopup      = document.getElementById('editPopup');
        const deletePopup    = document.getElementById('deleteConfirmPopup');
        const toggleCategory = () => {
            addCategoryBox.classList.toggle('hide', addApply.value !== 'category');
        };
        // Gắn event change
        addApply.addEventListener('change', () => {
            const categorySelect = document.getElementById('addCategory');
            if (addApply.value === 'category') {
                categorySelect.disabled = false;
                categorySelect.style.opacity = '1';
                categorySelect.style.pointerEvents = 'auto';
            } else {
                categorySelect.disabled = true;
                categorySelect.style.opacity = '0.5';
                categorySelect.style.pointerEvents = 'none';
                categorySelect.value = ''; // xóa lựa chọn cũ
            }
        });
        document.getElementById('openAddPopup').onclick = () => {
            addPopup.style.display = 'flex';
            document.getElementById('addCode').value = '';
            document.getElementById('addType').value = '';
            document.getElementById('addValue').value = '';
            document.getElementById('addMinOrder').value = '0';
            document.getElementById('addMaxUsage').value = '100';
            document.getElementById('addSingleUse').checked = false;
            document.getElementById('addStart').value = '';
            document.getElementById('addEnd').value = '';
            addApply.value = 'all';
            document.getElementById('addCategory').disabled = true;
            document.getElementById('addCategory').style.opacity = '0.5';
            document.getElementById('addCategory').value = '';
        };
        document.getElementById('closeAddPopup').onclick = () => addPopup.style.display = 'none';
        window.openEdit = function(code) {
            const rows = Array.from(tbody.querySelectorAll('tr')).filter(r => !r.classList.contains('pagination-row'));
            const row = rows.find(r => r.cells[0].textContent.trim() === code);
            if (!row) return alert('Không tìm thấy mã!');
            document.getElementById('editCode').textContent = row.cells[0].textContent.trim();
            document.getElementById('editValueDisplay').textContent = row.cells[1].textContent.trim();
            document.getElementById('editTypeDisplay').textContent = row.cells[2].textContent.trim();
            document.getElementById('editApplyDisplay').textContent = 'Toàn bộ sản phẩm';
            const minOrder = row.cells[4].textContent.replace(/[^\d]/g, '');
            document.getElementById('editMinOrder').value = minOrder || 0;
            const usage = row.cells[3].textContent.trim();
            const maxUsage = usage.split('/')[1]?.trim() || '100';
            document.getElementById('editMaxUsage').value = maxUsage.replace(/\D/g, '');
            const [d, m, y] = row.cells[5].textContent.trim().split('/');
            document.getElementById('editEnd').value = `${y}-${m.padStart(2,'0')}-${d.padStart(2,'0')}`;
            document.getElementById('editSingleUse').checked = false;
            document.getElementById('editStart').value = '';
            editPopup.style.display = 'flex';
        };
        document.getElementById('closeEditPopup').onclick = () => editPopup.style.display = 'none';
        document.getElementById('saveEditBtn').onclick = () => {
            alert('Cập nhật thành công!');
            editPopup.style.display = 'none';
        };
        const ROWS_PER_PAGE = 5;
        const rows = Array.from(tbody.querySelectorAll('tr')).filter(r => !r.classList.contains('pagination-row'));
        document.querySelectorAll('.pro-page').forEach(btn => {
            btn.onclick = () => {
                const page = +btn.dataset.page;
                rows.forEach((row, i) => {
                    row.style.display = (i >= (page-1)*ROWS_PER_PAGE && i < page*ROWS_PER_PAGE) ? '' : 'none';
                });
                document.querySelectorAll('.pro-page').forEach(b => b.classList.toggle('active', +b.dataset.page === page));
            };
        });
        document.querySelector('.pro-page[data-page="1"]')?.click();
        const currentPage = window.location.pathname.split("/").pop();
        document.querySelectorAll(".sidebar a").forEach(link => {
            if (link.getAttribute("href") === currentPage) {
                link.classList.add("active");
            }
        });
    });
</script>
<script>
    (function(){
        let currentPage = 1;
        const ROWS_PER_PAGE = 5;
        const tbody = document.getElementById('promoTableBody');
        const rows = Array.from(tbody.querySelectorAll('tr')).filter(r => !r.classList.contains('pagination-row'));
        const pageButtons = document.querySelectorAll('.pro-page');
        function showPage(page){
            const start = (page - 1) * ROWS_PER_PAGE;
            const end = start + ROWS_PER_PAGE;
            rows.forEach((r, idx) => {
                r.style.display = (idx >= start && idx < end) ? "" : "none";
            });
            pageButtons.forEach(btn => btn.classList.remove('active'));
            document.querySelector(`.pro-page[data-page="${page}"]`)?.classList.add('active');
            currentPage = page;
        }
        pageButtons.forEach(btn => {
            btn.addEventListener('click', () => {
                showPage(Number(btn.dataset.page));
            });
        });
        showPage(1);
    })();
</script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const current = window.location.pathname.split("/").pop();
        const links = document.querySelectorAll(".sidebar li a");
        links.forEach(link => {
            const linkPage = link.getAttribute("href");
            if (linkPage === current) {
                link.classList.add("active");
            }
        });
    });
</script>


<script>
    document.addEventListener("DOMContentLoaded", function() {
        const form = document.querySelector(".popup-box form");

        form.addEventListener("submit", function(event) {
            // Lấy giá trị các input
            const code = document.getElementById("addCode").value.trim();
            const discountTarget = document.getElementById("addType").value;
            const discountType = form.querySelector("select[name='discount_type']").value;
            const discountValue = document.getElementById("addValue").value;
            const minOrder = document.getElementById("addMinOrder").value;
            const quantity = document.getElementById("addMaxUsage").value;
            const scope = document.getElementById("addApply").value;
            const category = document.getElementById("addCategory").value;
            const startDate = document.getElementById("addStart").value;
            const endDate = document.getElementById("addEnd").value;

            let errorMsg = "";

            if (!code) errorMsg += "Tên mã không được để trống.\n";
            if (!discountTarget) errorMsg += "Phải chọn mục áp dụng.\n";
            if (!discountType) errorMsg += "Phải chọn loại giảm giá.\n";
            if (!discountValue || discountValue <= 0) errorMsg += "Giá trị giảm phải > 0.\n";
            if (!minOrder || minOrder < 0) errorMsg += "Đơn tối thiểu không hợp lệ.\n";
            if (!quantity || quantity <= 0) errorMsg += "Số lượng phải > 0.\n";
            if (scope === "category" && !category) errorMsg += "Phải chọn thể loại khi áp dụng theo thể loại.\n";
            if (!startDate) errorMsg += "Phải chọn ngày bắt đầu.\n";
            if (!endDate) errorMsg += "Phải chọn ngày kết thúc.\n";
            if (startDate && endDate && new Date(startDate) >= new Date(endDate)) {
                errorMsg += "Ngày kết thúc phải sau ngày bắt đầu.\n";
            }

            if (errorMsg) {
                event.preventDefault();
                alert(errorMsg);
            }
        });
    });
</script>

<script>
    function openDeletePopup(code) {
        document.getElementById("deleteVoucherCode").textContent = code;

        let input = document.getElementById("deleteCode");
        if (input) input.value = code;

        document.getElementById("deleteConfirmPopup").style.display = "flex";
    }
</script>

</body>
</html>