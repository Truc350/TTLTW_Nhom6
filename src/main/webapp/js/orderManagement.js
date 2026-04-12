function isPaginationRow(row) {
    const classList = row.classList;
    return classList.contains('pagination-row') ||
        classList.contains('pagination-row-pickup') ||
        classList.contains('pagination-row-delivering') ||
        classList.contains('pagination-row-delivered') ||
        classList.contains('pagination-row-return') ||
        classList.contains('pagination-row-cancelled');
}

function renderPaginationButtons(paginationContainer, totalPages, currentPage, onPageClick) {
    paginationContainer.innerHTML = '';

    if (totalPages <= 1) return;

    const windowSize = 3;

    const groupIndex = Math.floor((currentPage - 1) / windowSize);
    const groupStart = groupIndex * windowSize + 1;
    const groupEnd   = Math.min(groupStart + windowSize - 1, totalPages);

    const prevBtn = document.createElement('button');
    prevBtn.className = 'page-btn prev-btn';
    prevBtn.innerHTML = '&#171;';
    prevBtn.disabled = groupStart === 1;
    prevBtn.addEventListener('click', function () {
        if (currentPage > 1) onPageClick(groupStart - 1);
    });
    paginationContainer.appendChild(prevBtn);

    for (let i = groupStart; i <= groupEnd; i++) {
        const pageBtn = document.createElement('button');
        pageBtn.className = 'page-btn' + (i === currentPage ? ' active' : '');
        pageBtn.dataset.page = i;
        pageBtn.textContent = i;
        const pageIndex = i;
        pageBtn.addEventListener('click', function () {
            onPageClick(pageIndex);
        });
        paginationContainer.appendChild(pageBtn);
    }
    const nextBtn = document.createElement('button');
    nextBtn.className = 'page-btn next-btn';
    nextBtn.innerHTML = '&#187;';
    nextBtn.disabled = groupEnd === totalPages;
    nextBtn.addEventListener('click', function () {
        if (currentPage < totalPages) onPageClick(currentPage + 1);
    });
    paginationContainer.appendChild(nextBtn);
}


function initDynamicPagination(tbodyId, paginationId, pageButtonClass, rowsPerPage = 5) {
    const tbody = document.getElementById(tbodyId);
    const paginationContainer = document.getElementById(paginationId);

    if (!tbody || !paginationContainer) {
        console.error(`Không tìm thấy tbody (${tbodyId}) hoặc pagination (${paginationId})`);
        return;
    }

    const allRows = Array.from(tbody.querySelectorAll('tr')).filter(r => !isPaginationRow(r));
    const totalPages = Math.ceil(allRows.length / rowsPerPage);

    let currentPage = 1;

    function goToPage(page) {
        currentPage = page;
        showPage(page, allRows, rowsPerPage);
        renderPaginationButtons(paginationContainer, totalPages, currentPage, goToPage);
    }

    renderPaginationButtons(paginationContainer, totalPages, currentPage, goToPage);

    if (totalPages > 0) {
        showPage(1, allRows, rowsPerPage);
    }
}


function showPage(pageNumber, rows, rowsPerPage) {
    const start = (pageNumber - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    rows.forEach((row, idx) => {
        row.style.display = (idx >= start && idx < end) ? '' : 'none';
    })
}

function initShipConfirmButtons() {
    document.querySelectorAll('.ship-confirm-btn').forEach(btn => {
        const newBtn = btn.cloneNode(true);
        btn.parentNode.replaceChild(newBtn, btn);

        newBtn.addEventListener('click', function () {
            const orderId = this.dataset.orderId;
            handleShipConfirm(orderId, this);
        });
    });
}


function handleShipConfirm(orderId, buttonElement) {
    const originalText = buttonElement.textContent;
    buttonElement.disabled = true;
    buttonElement.textContent = 'Đang xử lý...';

    fetch(`${BASE_URL}/admin/orders`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `action=confirmShipped&orderId=${orderId}`
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showNotification('success', data.message || 'Đã xác nhận giao cho ĐVVC thành công!');
                setTimeout(() => {
                    location.reload();
                }, 1000);
            } else {
                showNotification('error', 'Lỗi: ' + (data.message || data.error));
                buttonElement.disabled = false;
                buttonElement.textContent = originalText;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('error', 'Lỗi kết nối: ' + error);
            buttonElement.disabled = false;
            buttonElement.textContent = originalText;
        });
}

function showNotification(type, message) {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-circle'}"></i>
            <span>${message}</span>
        </div>
    `;

    document.body.appendChild(notification);
    setTimeout(() => {
        notification.classList.add('show');
    }, 10);

    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            notification.remove();
        }, 300);
    }, 3000);
}

function initSearchWithDynamicPagination(searchInputId, tbodyId, paginationId, pageButtonClass, rowsPerPage = 5) {
    const searchInput = document.getElementById(searchInputId);
    const tbody = document.getElementById(tbodyId);
    const paginationContainer = document.getElementById(paginationId);

    if (!searchInput || !tbody || !paginationContainer) {
        return;
    }

    const searchIcon = searchInput.parentElement.querySelector('i.fa-magnifying-glass');

    const performSearch = function () {
        const keyword = searchInput.value.toLowerCase().trim();

        if (!keyword) {
            const allRows = Array.from(tbody.querySelectorAll('tr')).filter(r => !isPaginationRow(r));
            allRows.forEach(row => row.style.display = '');
            initDynamicPagination(tbodyId, paginationId, pageButtonClass, rowsPerPage);
            return;
        }

        const allRows = Array.from(tbody.querySelectorAll('tr')).filter(r => !isPaginationRow(r));
        const visibleRows = allRows.filter(row => {
            const orderCode = row.cells[0].textContent.toLowerCase();
            const customerName = row.cells[1].textContent.toLowerCase();
            return orderCode.includes(keyword) || customerName.includes(keyword);
        });

        allRows.forEach(row => row.style.display = 'none');
        const totalPages = Math.ceil(visibleRows.length / rowsPerPage);
        let currentPage = 1;

        function goToPage(page) {
            currentPage = page;
            showPage(page, visibleRows, rowsPerPage);
            renderPaginationButtons(paginationContainer, totalPages, currentPage, goToPage);
        }

        renderPaginationButtons(paginationContainer, totalPages, currentPage, goToPage);
        if (totalPages > 0) showPage(1, visibleRows, rowsPerPage);
    };

    searchInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            performSearch();
        }
    });

    searchInput.addEventListener('input', function () {
        if (this.value.trim() === '') {
            performSearch();
        }
    });

    if (searchIcon) {
        searchIcon.style.cursor = 'pointer';
        searchIcon.addEventListener('click', performSearch);
    }
}

document.addEventListener('DOMContentLoaded', function () {

    initDynamicPagination('confirmTableBody', 'tablePagination', 'confirm-page', 5);
    initSearchWithDynamicPagination('pendingSearch', 'confirmTableBody', 'tablePagination', 'confirm-page', 5);

    initShipConfirmButtons();
    initOrderDetailButtons();
});


function saveCurrentTab(tabIndex) {
    localStorage.setItem('orderManagementActiveTab', tabIndex);
}

function getSavedTab() {
    const saved = localStorage.getItem('orderManagementActiveTab');
    return saved !== null ? parseInt(saved) : null;
}

function clearSavedTab() {
    localStorage.removeItem('orderManagementActiveTab');
}


function initOrderDetailButtons() {
    document.querySelectorAll('.btn-de-detail').forEach(btn => {
        const newBtn = btn.cloneNode(true);
        btn.parentNode.replaceChild(newBtn, btn);

        newBtn.addEventListener('click', function () {
            const orderId = this.dataset.orderId;
            showOrderDetailPopup(orderId);
        });
    });
}


function showOrderDetailPopup(orderId) {
    showLoadingPopup();

    fetch(`${BASE_URL}/admin/orders?action=detail&orderId=${orderId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success && data.order) {
                renderOrderDetailPopup(data.order);
            } else {
                hideLoadingPopup();
                showNotification('error', 'Không thể tải thông tin đơn hàng: ' + (data.error || 'Lỗi không xác định'));
            }
        })
        .catch(error => {
            console.error('Error fetching order detail:', error);
            hideLoadingPopup();
            showNotification('error', 'Lỗi kết nối: ' + error);
        });
}

function renderOrderDetailPopup(order) {
    const popupHTML = `
        <div class="order-detail-popup active" id="orderDetailPopup">
            <div class="popup-overlay" onclick="closeOrderDetailPopup()"></div>
            <div class="popup-content">
                <div class="popup-header">
                    <h3>Chi tiết đơn hàng #${order.orderCode}</h3>
                    <button class="close-popup" onclick="closeOrderDetailPopup()">&times;</button>
                </div>
                
                <div class="popup-body">
                    <div class="info-section">
                        <h4>Thông tin đơn hàng</h4>
                        <div class="info-row">
                            <span class="label">Mã đơn hàng:</span>
                            <span class="value">#${order.orderCode}</span>
                        </div>
                        <div class="info-row">
                            <span class="label">Khách hàng:</span>
                            <span class="value">${order.userName}</span>
                        </div>
                        <div class="info-row">
                            <span class="label">Số điện thoại:</span>
                            <span class="value">${order.shippingPhone}</span>
                        </div>
                        <div class="info-row">
                            <span class="label">Địa chỉ giao hàng:</span>
                            <span class="value">${order.shippingAddress}</span>
                        </div>
                        <div class="info-row">
                            <span class="label">Đơn vị vận chuyển:</span>
                            <span class="value">${order.shippingProvider}</span>
                        </div>
                        <div class="info-row">
                            <span class="label">Ngày đặt:</span>
                            <span class="value">${order.orderDateFormatted}</span>
                        </div>
                    </div>

                    <div class="info-section">
                        <h4>Sản phẩm (${order.itemCount})</h4>
                        <div class="product-list">
                            ${renderProductList(order.items)}
                        </div>
                    </div>

                    <div class="info-section">
                        <h4>Thông tin thanh toán</h4>
                        <div class="info-row">
                            <span class="label">Phương thức:</span>
                            <span class="value">${order.paymentMethodDisplay}</span>
                        </div>
                        <div class="info-row">
                            <span class="label">Trạng thái:</span>
                            <span class="value">${order.paymentStatusDisplay}</span>
                        </div>
                        ${order.transactionId ? `
                        <div class="info-row">
                            <span class="label">Mã giao dịch:</span>
                            <span class="value">${order.transactionId}</span>
                        </div>
                        ` : ''}
                        <div class="info-row">
                            <span class="label">Phí vận chuyển:</span>
                            <span class="value">${formatCurrency(order.shippingFee)}</span>
                        </div>
                        ${order.pointUsed > 0 ? `
                        <div class="info-row">
                            <span class="label">Xu đã sử dụng:</span>
                            <span class="value highlight">${order.pointUsed} xu</span>
                        </div>
                        ` : ''}
                        <div class="info-row total">
                            <span class="label">Tổng tiền:</span>
                            <span class="value">${order.formattedAmount}</span>
                        </div>
                    </div>

                    <div class="info-section">
                        <h4>Trạng thái giao hàng</h4>
                        <div class="info-row timeline">
                            ${renderTimeline(order.status)}
                        </div>
                    </div>
                </div>
                
                <div class="popup-footer">
                    <button class="btn-close" onclick="closeOrderDetailPopup()">Đóng</button>
                </div>
            </div>
        </div>
    `;

    const oldPopup = document.getElementById('orderDetailPopup');
    if (oldPopup) {
        oldPopup.remove();
    }

    document.body.insertAdjacentHTML('beforeend', popupHTML);
    hideLoadingPopup();
}

function renderProductList(items) {
    if (!items || items.length === 0) {
        return '<p class="no-items">Không có sản phẩm</p>';
    }

    return items.map(item => `
        <div class="product-item">
            ${item.comicImage ? `
            <div class="product-image">
                <img src="${item.comicImage}" alt="${item.comicName}" onerror="this.src='/images/no-image.png'">
            </div>
            ` : ''}
            <div class="product-info">
                <div class="product-name">${item.comicName}</div>
                <div class="product-details">
                    <span>Số lượng: ${item.quantity}</span>
                    <span>Đơn giá: ${item.formattedPrice}</span>
                </div>
            </div>
            <div class="product-total">
                ${formatCurrency(item.priceAtPurchase * item.quantity)}
            </div>
        </div>
    `).join('');
}


function renderTimeline(currentStatus) {
    const statuses = [
        {key: 'Pending', label: 'Chờ xác nhận'},
        {key: 'AwaitingPickup', label: 'Chờ lấy hàng'},
        {key: 'Shipping', label: 'Đang giao'},
        {key: 'Completed', label: 'Hoàn thành'}
    ];

    const currentIndex = statuses.findIndex(s => s.key === currentStatus);

    return statuses.map((status, index) => {
        let stepClass = 'step';

        if (index < currentIndex) {
            stepClass += ' done';
        } else if (index === currentIndex) {
            stepClass += ' active';
        }

        const isLast = index === statuses.length - 1;

        return `
            <div class="${stepClass}">
                <div class="circle">${index + 1}</div>
                ${!isLast ? '<div class="line"></div>' : ''}
                <p>${status.label}</p>
            </div>
        `;
    }).join('');
}


function closeOrderDetailPopup() {
    const popup = document.getElementById('orderDetailPopup');
    if (popup) {
        popup.classList.remove('active');
        setTimeout(() => {
            popup.remove();
        }, 300);
    }
}

function showLoadingPopup() {
    const loadingHTML = `
        <div class="loading-popup" id="loadingPopup">
            <div class="loading-spinner"></div>
            <p>Đang tải thông tin đơn hàng...</p>
        </div>
    `;
    document.body.insertAdjacentHTML('beforeend', loadingHTML);
}

function hideLoadingPopup() {
    const loadingPopup = document.getElementById('loadingPopup');
    if (loadingPopup) {
        loadingPopup.remove();
    }
}

function formatCurrency(amount) {
    const formatted = new Intl.NumberFormat('vi-VN').format(amount);
    return formatted + ' đ';
}

window.initOrderDetailButtons = initOrderDetailButtons;
window.closeOrderDetailPopup = closeOrderDetailPopup;


(function () {
    'use strict';

    const SEARCH_CONFIG = {
        'pickupSearch': {
            status: 'AwaitingPickup',
            tbody: 'pickupTableBody',
            pagination: 'pickupPagination',
            pageButtonClass: 'pickup-page',
            noResultMessage: 'Không tìm thấy đơn hàng nào'
        },
        'deliverSearch': {
            status: 'Shipping',
            tbody: 'deliverTableBody',
            pagination: 'deliveringPagination',
            pageButtonClass: 'delivering-page',
            noResultMessage: 'Không tìm thấy đơn hàng nào'
        },
        'deliveredSearch': {
            status: 'Completed',
            tbody: 'deliveredTableBody',
            pagination: 'deliveredPagination',
            pageButtonClass: 'delivered-page',
            noResultMessage: 'Không tìm thấy đơn hàng nào'
        },
        'returnSearch': {
            status: 'Returned',
            tbody: 'returnTableBody',
            pagination: 'returnPagination',
            pageButtonClass: 'return-page',
            noResultMessage: 'Không tìm thấy đơn hàng nào'
        },
        'cancelledSearch': {
            status: 'Cancelled',
            tbody: 'cancelledTableBody',
            pagination: 'cancelledPagination',
            pageButtonClass: 'cancelled-page',
            noResultMessage: 'Không tìm thấy đơn hàng nào'
        }
    };


    function searchOrders(keyword, config) {
        const tbody = document.getElementById(config.tbody);
        const paginationContainer = document.getElementById(config.pagination);
        if (!tbody || !paginationContainer) return;

        if (!keyword || keyword.trim() === '') {
            const allRows = Array.from(tbody.querySelectorAll('tr')).filter(r => !isPaginationRow(r));
            allRows.forEach(row => row.style.display = '');
            const rowsPerPage = 5;
            const totalPages = Math.ceil(allRows.length / rowsPerPage);
            let currentPage = 1;
            function goToPage(page) {
                currentPage = page;
                showPage(page, allRows, rowsPerPage);
                renderPaginationButtons(paginationContainer, totalPages, currentPage, goToPage);
            }
            renderPaginationButtons(paginationContainer, totalPages, currentPage, goToPage);
            if (totalPages > 0) showPage(1, allRows, rowsPerPage);
            return;
        }

        showLoading(tbody);

        fetch(`${BASE_URL}/admin/orders?action=searchByTab&keyword=${encodeURIComponent(keyword)}&status=${config.status}`)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    renderSearchResults(data.orders, tbody, config, paginationContainer);
                } else {
                    showError(tbody, data.error || 'Lỗi khi tìm kiếm');
                }
            })
            .catch(error => {
                console.error('Search error:', error);
                showError(tbody, 'Lỗi kết nối: ' + error.message);
            });
    }

    function renderSearchResults(orders, tbody, config, paginationContainer) {
        const rows = tbody.querySelectorAll('tr');
        rows.forEach(row => {
            if (!isPaginationRow(row)) {
                row.remove();
            }
        });

        if (!orders || orders.length === 0) {
            showNoResults(tbody, config.noResultMessage);
            paginationContainer.innerHTML = '';
            return;
        }

        const fragment = document.createDocumentFragment();
        orders.forEach(order => {
            const row = createOrderRow(order, config.status);
            fragment.appendChild(row);
        });

        const paginationRow = Array.from(tbody.querySelectorAll('tr')).find(row => isPaginationRow(row));
        if (paginationRow) {
            tbody.insertBefore(fragment, paginationRow);
        } else {
            tbody.appendChild(fragment);
        }

        attachButtonEvents(tbody, config.status);

        const allRows = Array.from(tbody.querySelectorAll('tr')).filter(r =>
            !isPaginationRow(r) &&
            !r.classList.contains('loading-row') &&
            !r.classList.contains('error-row') &&
            !r.classList.contains('no-result-row')
        );

        const rowsPerPage = 5;
        const totalPages = Math.ceil(allRows.length / rowsPerPage);
        let currentPage = 1;

        function goToPage(page) {
            currentPage = page;
            showPage(page, allRows, rowsPerPage);
            renderPaginationButtons(paginationContainer, totalPages, currentPage, goToPage);
        }

        renderPaginationButtons(paginationContainer, totalPages, currentPage, goToPage);
        if (totalPages > 0) showPage(1, allRows, rowsPerPage);
    }


    function createOrderRow(order, status) {
        const tr = document.createElement('tr');

        switch (status) {
            case 'Pending':
                tr.innerHTML = createPendingRow(order);
                break;
            case 'AwaitingPickup':
                tr.innerHTML = createPickupRow(order);
                break;
            case 'Shipping':
                tr.innerHTML = createShippingRow(order);
                break;
            case 'Completed':
                tr.innerHTML = createCompletedRow(order);
                break;
            case 'Returned':
                tr.innerHTML = createReturnedRow(order);
                break;
            case 'Cancelled':
                tr.innerHTML = createCancelledRow(order);
                break;
        }
        return tr;
    }


    function createPendingRow(order) {
        return `
            <td>${order.orderCode || order.id}</td>
            <td>${order.userName || order.recipientName || ''}</td>
            <td>${order.orderDateFormatted || formatDate(order.orderDate)}</td>
            <td>${order.formattedAmount || formatCurrency(order.totalAmount)}</td>
            <td>${order.paymentMethodDisplay || order.paymentMethod || '—'}</td>
            <td class="product-cell">${order.productSummary || ''}</td>
            <td>${order.fullAddress || order.shippingAddress || ''}</td>
            <td>${order.shippingProvider || '—'}</td>
            <td>
                <button class="confirm-btn" data-order-id="${order.id}">Xác nhận</button>
                <button class="cancel-btn" data-order-id="${order.id}">Hủy</button>
            </td>
        `;
    }

    function createPickupRow(order) {
        return `
            <td>${order.orderCode || order.id}</td>
            <td>${order.userName || order.recipientName || ''}</td>
            <td>${order.formattedAmount || formatCurrency(order.totalAmount)}</td>
            <td>${order.shippingProvider || '—'}</td>
            <td>${order.shippingAddress || ''}</td>
            <td>
                <button class="ship-confirm-btn" data-order-id="${order.id}">
                    Xác nhận đã giao cho ĐVVC
                </button>
            </td>
        `;
    }

    function createShippingRow(order) {
        return `
            <td>${order.orderCode || order.id}</td>
            <td>${order.userName || order.recipientName || ''}</td>
            <td>${order.shippingProvider || '—'}</td>
            <td class="action-cell">
                <button class="btn-de-detail" data-order-id="${order.id}">
                    Xem chi tiết đơn
                </button>
            </td>
        `;
    }

    function createCompletedRow(order) {
        return `
            <td>${order.orderCode || order.id}</td>
            <td>${order.userName || order.recipientName || ''}</td>
            <td>${order.orderDateFormatted || formatDate(order.orderDate)}</td>
            <td>${order.formattedAmount || formatCurrency(order.totalAmount)}</td>
            <td>${order.paymentMethodDisplay || order.paymentMethod || '—'}</td>
            <td>${order.transactionId || '—'}</td>
            <td class="stars">
                <i class="fa-solid fa-star"></i>
                <i class="fa-solid fa-star"></i>
                <i class="fa-solid fa-star"></i>
                <i class="fa-solid fa-star"></i>
                <i class="fa-regular fa-star-half-stroke"></i>
            </td>
        `;
    }

    function createReturnedRow(order) {
        const returnStatus = order.return_status || order.returnStatus || '';
        const isPending = returnStatus === 'Pending';

        let displayReason = order.reason || '';
        if (displayReason.includes('Lý do từ chối:')) {
            displayReason = displayReason.split(' | ')[0];
        }

        const actionButtons = isPending
            ? `<button class="btn-refund" data-return-id="${order.return_id}" 
                   onclick="confirmRefund(this)">Xác nhận hoàn tiền</button>
           <button class="btn-reject" 
                   data-return-id="${order.return_id}" 
                   data-order-code="${order.order_code || ''}" 
                   data-customer="${order.customer_name || ''}" 
                   onclick="openRejectPopup(this)">Từ chối</button>`
            : `<span style="color: #999;">-</span>`;

        return `
        <td>${order.order_code || order.id}</td>
        <td>${order.customer_name || ''}</td>
        <td class="reason-cell">${displayReason}</td>
        <td class="amount-cell">${order.formatted_refund_amount || ''}</td>
        <td><span class="status ${order.status_class || ''}">${order.status_display || ''}</span></td>
        <td>
            <button class="btn-detail"
                data-return-id="${order.return_id}"
                data-order-code="${order.order_code || ''}"
                data-customer="${order.customer_name || ''}"
                data-product="${order.product_name || ''}"
                data-quantity="${order.total_quantity || order.quantity || ''}"
                data-amount="${order.formatted_refund_amount || ''}"
                data-date="${order.formatted_return_date || ''}">
                Xem
            </button>
        </td>
        <td class="action-buttons">${actionButtons}</td>
    `;
    }

    function createCancelledRow(order) {
        const cancelledBy = order.cancelledBy;
        let cancelledByDisplay = '<span style="color: #999;">N/A</span>';

        if (cancelledBy === 'Admin') {
            cancelledByDisplay = '<span style="color: #dc2626; font-weight: 500;"><i class="fas fa-user-shield"></i> Admin</span>';
        } else if (cancelledBy === 'Customer') {
            cancelledByDisplay = '<span style="color: #2563eb; font-weight: 500;"><i class="fas fa-user"></i> Khách hàng</span>';
        }

        return `
            <td>${order.orderCode || order.id}</td>
            <td>${order.userName || order.recipientName || ''}</td>
            <td>${order.orderDateFormatted || formatDate(order.orderDate)}</td>
            <td>${order.cancellationReason || '<span style="color: #999; font-style: italic;">Không có lý do</span>'}</td>
            <td>${cancelledByDisplay}</td>
            <td>${order.cancelledAtFormatted || formatDate(order.cancelledAt)}</td>
        `;
    }

    function formatCurrency(amount) {
        if (!amount) return '0đ';
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    }

    function formatDate(dateStr) {
        if (!dateStr) return 'N/A';
        const date = new Date(dateStr);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        return `${day}/${month}/${year} ${hours}:${minutes}`;
    }

    function showLoading(tbody) {
        const rows = tbody.querySelectorAll('tr:not([class*="pagination-row"])');
        rows.forEach(row => row.remove());

        const loadingRow = document.createElement('tr');
        loadingRow.className = 'loading-row';
        loadingRow.innerHTML = `
            <td colspan="10" style="text-align: center; padding: 30px;">
                <i class="fas fa-spinner fa-spin" style="font-size: 24px; color: #3b82f6;"></i>
                <p style="margin-top: 10px; color: #666;">Đang tìm kiếm...</p>
            </td>
        `;

        const paginationRow = tbody.querySelector('[class*="pagination-row"]');
        if (paginationRow) {
            tbody.insertBefore(loadingRow, paginationRow);
        } else {
            tbody.appendChild(loadingRow);
        }
    }

    function showError(tbody, message) {
        const rows = tbody.querySelectorAll('tr:not([class*="pagination-row"])');
        rows.forEach(row => row.remove());

        const errorRow = document.createElement('tr');
        errorRow.className = 'error-row';
        errorRow.innerHTML = `
        <td colspan="10" style="text-align: center; padding: 50px 30px;">
            <svg width="120" height="120" viewBox="0 0 24 24" fill="none" 
                 style="margin: 0 auto 20px; display: block; opacity: 0.3;">
                <circle cx="11" cy="11" r="8" stroke="currentColor" stroke-width="2"/>
                <path d="M21 21l-4.35-4.35" stroke="currentColor" 
                      stroke-width="2" stroke-linecap="round"/>
            </svg>
            <p style="color: #999; font-size: 16px; margin: 0;">${message}</p>
        </td>
    `;

        const paginationRow = tbody.querySelector('[class*="pagination-row"]');
        if (paginationRow) {
            tbody.insertBefore(errorRow, paginationRow);
        } else {
            tbody.appendChild(errorRow);
        }
    }


    function showNoResults(tbody, message) {
        const rows = tbody.querySelectorAll('tr:not([class*="pagination-row"])');
        rows.forEach(row => row.remove());

        const noResultRow = document.createElement('tr');
        noResultRow.className = 'no-result-row';
        noResultRow.innerHTML = `
            <td colspan="10" style="text-align: center; padding: 50px 30px;">
                <svg width="120" height="120" viewBox="0 0 24 24" fill="none" style="margin: 0 auto 20px; display: block; opacity: 0.3;">
                    <circle cx="11" cy="11" r="8" stroke="currentColor" stroke-width="2"/>
                    <path d="M21 21l-4.35-4.35" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                </svg>
                <p style="color: #999; font-size: 16px; margin: 0;">${message}</p>
            </td>
        `;

        const paginationRow = tbody.querySelector('[class*="pagination-row"]');
        if (paginationRow) {
            tbody.insertBefore(noResultRow, paginationRow);
        } else {
            tbody.appendChild(noResultRow);
        }
    }

    function attachButtonEvents(tbody, status) {
        if (status === 'Pending') {
            tbody.querySelectorAll('.confirm-btn').forEach(btn => {
                btn.addEventListener('click', handleConfirmOrder);
            });

            tbody.querySelectorAll('.cancel-btn').forEach(btn => {
                btn.addEventListener('click', handleCancelOrder);
            });
        }

        if (status === 'AwaitingPickup') {
            tbody.querySelectorAll('.ship-confirm-btn').forEach(btn => {
                btn.addEventListener('click', handleShipConfirmInline);
            });
        }

        if (status === 'Shipping') {
            tbody.querySelectorAll('.btn-de-detail').forEach(btn => {
                btn.addEventListener('click', function () {
                    const orderId = this.dataset.orderId;
                    showOrderDetailPopup(orderId);
                });
            });
        }
        if (status === 'Returned') {

        }
    }


    function handleConfirmOrder(e) {
        const orderId = e.target.dataset.orderId;
        if (confirm('Xác nhận đơn hàng này?')) {
            fetch(BASE_URL + '/admin/orders', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'action=confirm&orderId=' + orderId
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        showToast(data.message);
                        setTimeout(() => location.reload(), 1500);
                    } else {
                        showToast('Lỗi: ' + data.message, 'error');
                    }
                })
                .catch(error => showToast('Lỗi kết nối: ' + error, 'error'));
        }
    }


    function handleCancelOrder(e) {
        const orderId = e.target.dataset.orderId;
        window.currentCancelOrderId = orderId;
        document.querySelector('.cancel-popup').style.display = 'flex';
        document.querySelector('.cancel-popup textarea').value = '';
    }

    function handleShipConfirmInline(e) {
        const orderId = e.target.dataset.orderId;
        handleShipConfirm(orderId, e.target);
    }


    function initializeSearch() {
        Object.entries(SEARCH_CONFIG).forEach(([inputId, config]) => {
            const searchInput = document.getElementById(inputId);
            if (searchInput) {
                const searchIcon = searchInput.parentElement.querySelector('i.fa-magnifying-glass');

                const performSearch = function () {
                    const keyword = searchInput.value.trim();
                    searchOrders(keyword, config);
                };
                searchInput.addEventListener('keypress', function (e) {
                    if (e.key === 'Enter') {
                        e.preventDefault();
                        performSearch();
                    }
                });
                if (searchIcon) {
                    searchIcon.style.cursor = 'pointer';
                    searchIcon.addEventListener('click', performSearch);
                }
            }
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initializeSearch);
    } else {
        initializeSearch();
    }


    // ===== LAZY LOAD TAB =====
    const TAB_STATUS_MAP = {
        1: 'AwaitingPickup',
        2: 'Shipping',
        3: 'Completed',
        4: 'Returned',
        5: 'Cancelled'
    };

    const loadedTabs = new Set([0]);
    function loadTabIfNeeded(tabIndex) {
        if (loadedTabs.has(tabIndex)) return;

        const status = TAB_STATUS_MAP[tabIndex];
        if (!status) return;

        loadedTabs.add(tabIndex);

        const tbodyMap = {
            'AwaitingPickup': 'pickupTableBody',
            'Shipping':       'deliverTableBody',
            'Completed':      'deliveredTableBody',
            'Returned':        'returnTableBody',
            'Cancelled':      'cancelledTableBody'
        };

        const paginationMap = {
            'AwaitingPickup': 'pickupPagination',
            'Shipping':       'deliveringPagination',
            'Completed':      'deliveredPagination',
            'Returned':        'returnPagination',
            'Cancelled':      'cancelledPagination'
        };

        const tbody = document.getElementById(tbodyMap[status]);
        if (!tbody) return;

        const paginationRow = tbody.querySelector('[class*="pagination-row"]');
        const loadingRow = document.createElement('tr');
        loadingRow.className = 'loading-row';
        loadingRow.innerHTML = `
        <td colspan="10" style="text-align:center; padding:40px;">
            <i class="fas fa-spinner fa-spin" style="font-size:24px; color:#ff4c4c;"></i>
            <p style="margin-top:10px; color:#666;">Đang tải dữ liệu...</p>
        </td>`;
        if (paginationRow) tbody.insertBefore(loadingRow, paginationRow);
        else tbody.appendChild(loadingRow);

        fetch(`${BASE_URL}/admin/orders?action=loadTab&status=${status}`)
            .then(r => r.json())
            .then(data => {
                if (status === 'Returned') {
                    console.log('Returns data sample:', data.orders?.[0]);
                }
                loadingRow.remove();

                if (!data.success) {
                    showNotification('error', 'Không thể tải dữ liệu: ' + data.message);
                    loadedTabs.delete(tabIndex);
                    return;
                }

                tbody.querySelectorAll('.no-result-message').forEach(r => r.remove());
                tbody.querySelectorAll('tr:not([class*="pagination-row"]):not(.loading-row)').forEach(r => r.remove());

                const fragment = document.createDocumentFragment();
                (data.orders || []).forEach(order => {
                    const row = createOrderRow(order, status);
                    fragment.appendChild(row);
                });
                if (paginationRow) tbody.insertBefore(fragment, paginationRow);
                else tbody.appendChild(fragment);

                attachButtonEvents(tbody, status);
                if (status === 'Shipping') initOrderDetailButtons();
                if (status === 'AwaitingPickup') initShipConfirmButtons();

                const paginationId = paginationMap[status];
                const tbodyId = tbodyMap[status];
                initDynamicPagination(tbodyId, paginationId, status + '-page', 5);
            })
            .catch(err => {
                loadingRow.remove();
                loadedTabs.delete(tabIndex);
                showNotification('error', 'Lỗi kết nối: ' + err);
            });
    }

    document.querySelectorAll('.tab-item').forEach((tab, index) => {
        tab.addEventListener('click', () => loadTabIfNeeded(index));
    });

    document.addEventListener('DOMContentLoaded', function() {
        const savedTab = typeof getSavedTab === 'function' ? getSavedTab() : 0;
    });
})();