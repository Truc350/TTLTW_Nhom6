<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setLocale value="vi_VN"/>
<fmt:setBundle basename="java.text.DecimalFormatSymbols"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Comic Store - Giỏ hàng</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/publicCss/FooterStyle.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/UserBCss/cartCss.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/frontend/css/publicCss/nav.css">
</head>

<body>
<jsp:include page="/frontend/public/header.jsp"/>

<form id="checkoutForm"
      action="${pageContext.request.contextPath}/checkout"
      method="post">
</form>

<main class="wrapper">
    <div class="cart-items-container">
        <div class="cart-header">GIỎ HÀNG</div>
        <div class="select-all">
            <input type="checkbox" class="item-checkbox" id="select-all"
                   form="checkoutForm"/>
            <label for="select-all"> Chọn tất cả (${fn:length(cartItems)} sản phẩm)</label>
            <div class="quantity-header">Số lượng</div>
            <div class="price-header">Thành tiền</div>
        </div>
        <div class="cart-items-scrollable">
            <div class="cart-items">
                <c:choose>
                    <c:when test="${empty cartItems}">
                        <div style="text-align: center; padding: 40px; color: #999;">
                            <i class="fas fa-shopping-cart" style="font-size: 64px; margin-bottom: 20px;"></i>
                            <p style="font-size: 18px;">Giỏ hàng của bạn đang trống</p>
                            <a href="${pageContext.request.contextPath}/"
                               style="display: inline-block; margin-top: 20px; padding: 10px 30px; background: #e74c3c; color: white; text-decoration: none; border-radius: 5px;">
                                Tiếp tục mua sắm
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${cartItems}">
                            <div class="cart-item" data-comic-id="${item.comic.id}">
                                <input type="checkbox" class="item-checkbox" name="selectedComics" value="${item.comic.id}"
                                       form="checkoutForm"/>
                                <img src="${item.comic.thumbnailUrl}"
                                     alt="${item.comic.nameComics}" class="item-image"/>
                                <div class="item-info">
                                    <div class="item-title">${item.comic.nameComics}</div>
                                    <div class="item-subtitle">${item.comic.nameComics}</div>
                                    <div class="item-price" data-price="${item.finalPrice}">
    <span class="discount-price">
        <fmt:formatNumber value="${item.finalPrice}" type="number" groupingUsed="true"/> đ
    </span>
                                        <c:if test="${item.flashSaleId != null}">
                                            <span class="flash-sale-badge">⚡ Flash Sale</span>
                                        </c:if>
                                        <c:if test="${item.finalPrice < item.comic.price}">
                                            <del class="original-price">
                                                <fmt:formatNumber value="${item.comic.price}" type="number" groupingUsed="true"/> đ
                                            </del>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="quantity-control">
                                    <button type="button" class="quantity-btn minus">-</button>
                                    <input type="text" value="${item.quantity}" class="quantity-input" readonly/>
                                    <button type="button" class="quantity-btn plus">+</button>
                                </div>
                                <div class="item-footer">
                                    <div class="item-total">
                                        <fmt:formatNumber value="${item.subtotal}" type="number" groupingUsed="true"/> đ
                                    </div>
                                    <button type="button" class="delete-btn">
                                        <i class="fa-solid fa-trash"></i>
                                    </button>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <div class="cart-summary">
        <div class="total-section">
            <div class="total-row">
                <span>Thành tiền</span>
                <span class="total-price"><fmt:formatNumber value="${checkoutTotal != null ? checkoutTotal : 0}"
                                                            type="number" groupingUsed="true"/> đ
                </span>
            </div>
            <div class="total-row final">
                <span>Tổng Tiền:</span>
                <span class="total-final"><fmt:formatNumber value="${checkoutTotal != null ? checkoutTotal : 0}"
                                                            type="number"
                                                            groupingUsed="true"/> đ</span>
            </div>
        </div>
        <div class="checkout-section">
            <button type="submit" id="btnCheckout" form="checkoutForm" class="btn-checkout ${not empty cartItems ? 'active' : ''}">THANH
                TOÁN
            </button>
        </div>
    </div>
</main>
<div id="loginModal" class="login-modal">
    <div class="login-modal-content">
        <h3>Bạn cần đăng nhập để mua hàng</h3>
        <div class="login-modal-buttons">
            <a href="${pageContext.request.contextPath}/login" class="login-modal-btn login-modal-btn-primary"  method="post">
                Đăng nhập</a>
            <button type="button" id="closeLoginModal" class="login-modal-btn login-modal-btn-secondary">
                Đóng
            </button>
        </div>
    </div>
</div>
<div class="message-box" id="messageBox">
    <span class="close-msg">&times;</span>
    <p id="messageText"></p>
</div>
<jsp:include page="/frontend/public/Footer.jsp"/>
<script>
    <c:if test="${not empty sessionScope.clearCartLocalStorage}">
    localStorage.removeItem('cartCheckboxStates');
    <% session.removeAttribute("clearCartLocalStorage"); %>
    </c:if>
    <c:if test="${empty cartItems}">
    localStorage.removeItem('cartCheckboxStates');
    </c:if>
    const contextPath = '${pageContext.request.contextPath}';
    const isLoggedIn = "${not empty sessionScope.currentUser}" === "true";
    const loginModal = document.getElementById("loginModal");
    const closeLoginModal = document.getElementById("closeLoginModal");
    const selectAllCheckbox = document.getElementById("select-all");
    const selectAllLabel = document.querySelector(".select-all label[for='select-all']");
    const totalFinalElement = document.querySelector(".total-final");
    const totalPriceElement = document.querySelector(".total-price");
    const checkoutButton = document.getElementById("btnCheckout");
    function calculateTotal() {
        const cartItems = document.querySelectorAll(".cart-item");
        let total = 0;
        let count = 0;

        cartItems.forEach(item => {
            const checkbox = item.querySelector(".item-checkbox");
            if (checkbox && checkbox.checked) {
                const priceText = item.querySelector(".item-price").dataset.price;
                const quantity = parseInt(item.querySelector(".quantity-input").value);
                const price = parseFloat(priceText);

                console.log('Item:', {
                    name: item.querySelector('.item-title').textContent,
                    price: price,
                    quantity: quantity,
                    subtotal: price * quantity
                });

                total += price * quantity;
                count++;
            }
        });

        console.log('Total:', total, 'Count:', count);

        if (totalPriceElement) {
            totalPriceElement.textContent = total.toLocaleString('vi-VN') + ' đ';
        }
        if (totalFinalElement) {
            totalFinalElement.textContent = total.toLocaleString('vi-VN') + ' đ';
        }

        if (checkoutButton) {
            if (count > 0) {
                checkoutButton.classList.add("active");
            } else {
                checkoutButton.classList.remove("active");
            }
        }
    }
    function updateCartItemCount() {
        const itemCount = document.querySelectorAll(".cart-item").length;
        if (selectAllLabel) {
            selectAllLabel.textContent = "Chọn tất cả (" + itemCount + " sản phẩm)";
        }
    }
    function saveCheckboxStates() {
        const states = {};
        document.querySelectorAll(".cart-item").forEach(item => {
            const comicId = item.getAttribute("data-comic-id");
            const checkbox = item.querySelector(".item-checkbox");
            states[comicId] = checkbox.checked;
        });
        localStorage.setItem('cartCheckboxStates', JSON.stringify(states));
    }

    function restoreCheckboxStates() {
        const states = JSON.parse(localStorage.getItem('cartCheckboxStates'));
        if (!states) return;

        const currentComicIds = [];
        document.querySelectorAll(".cart-item").forEach(item => {
            currentComicIds.push(item.getAttribute("data-comic-id"));
        });

        document.querySelectorAll(".cart-item").forEach(item => {
            const comicId = item.getAttribute("data-comic-id");
            const checkbox = item.querySelector(".item-checkbox");

            if (states[comicId]) {
                checkbox.checked = true;
            }
        });

        const cleanedStates = {};
        currentComicIds.forEach(id => {
            if (states[id] !== undefined) {
                cleanedStates[id] = states[id];
            }
        });
        localStorage.setItem('cartCheckboxStates', JSON.stringify(cleanedStates));
    }

    window.addEventListener('DOMContentLoaded', function () {
        restoreCheckboxStates();
        updateCartItemCount();
        calculateTotal();
    });
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener("change", function () {
            const allCheckboxes = document.querySelectorAll(".cart-item .item-checkbox");
            allCheckboxes.forEach(checkbox => {
                checkbox.checked = this.checked;
            });
            saveCheckboxStates();
            calculateTotal();
        });
    }
    document.querySelectorAll(".cart-item .item-checkbox").forEach(checkbox => {
        checkbox.addEventListener("change", function () {
            if (!this.checked) {
                if (selectAllCheckbox) selectAllCheckbox.checked = false;
            } else {
                const totalCheckboxes = document.querySelectorAll(".cart-item .item-checkbox").length;
                const checkedCheckboxes = document.querySelectorAll(".cart-item .item-checkbox:checked").length;
                if (selectAllCheckbox && totalCheckboxes === checkedCheckboxes) {
                    selectAllCheckbox.checked = true;
                }
            }
            saveCheckboxStates();
            calculateTotal();
        });
    });
    document.querySelectorAll(".quantity-control .quantity-btn").forEach(button => {
        button.addEventListener("click", function () {
            const quantityInput = this.parentElement.querySelector(".quantity-input");
            const comicId = this.closest(".cart-item").getAttribute("data-comic-id");
            let currentValue = parseInt(quantityInput.value);
            if (this.classList.contains("plus")) {
                currentValue++;
            } else if (this.classList.contains("minus") && currentValue > 1) {
                currentValue--;
            }
            window.location.href = contextPath + "/cart?action=update&comicId=" + comicId + "&quantity=" + currentValue;
        });
    });
    document.querySelectorAll(".delete-btn").forEach(button => {
        button.addEventListener("click", function () {
            const comicId = this.closest(".cart-item").getAttribute("data-comic-id");
            if (confirm("Bạn có chắc muốn xóa sản phẩm này?")) {
                window.location.href = contextPath + "/cart?action=remove&comicId=" + comicId;
            }
        });
    });
    const form = document.getElementById("checkoutForm");
    form.addEventListener("submit", function (e) {
        if (!isLoggedIn) {
            e.preventDefault();
            loginModal.style.display = "block";
            return;
        }
        const checked = document.querySelectorAll(
            ".cart-item .item-checkbox:checked"
        );

        if (checked.length === 0) {
            e.preventDefault();
            return;
        }
    });
    if (closeLoginModal) {
        closeLoginModal.addEventListener("click", function () {
            if (loginModal) loginModal.style.display = "none";
        });
    }
    window.addEventListener("click", function (event) {
        if (event.target === loginModal) {
            loginModal.style.display = "none";
        }
    });
    <c:if test="${not empty cartItems}">
    <c:set var="hasFlashSale" value="false" />
    <c:forEach var="item" items="${cartItems}">
    <c:if test="${item.flashSaleId != null}">
    <c:set var="hasFlashSale" value="true" />
    </c:if>
    </c:forEach>

    <c:if test="${hasFlashSale}">
    setInterval(function() {
        console.log(' Đang cập nhật giá Flash Sale...');
        location.reload();
    }, 60000);
    console.log(' Giá sản phẩm Flash Sale sẽ được cập nhật tự động');
    </c:if>
    </c:if>
</script>
</body>
</html>