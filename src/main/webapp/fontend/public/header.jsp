<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="vn.edu.hcmuaf.fit.ttltw_nhom6.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="vn.edu.hcmuaf.fit.ttltw_nhom6.dao.CategoriesDao" %>
<%@ page import="vn.edu.hcmuaf.fit.ttltw_nhom6.model.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<header class="navbar">
    <a href="${pageContext.request.contextPath}/home">
        <div class="logo">
            <img id="logo" src="${pageContext.request.contextPath}/img/logo.png" alt="Comic Store">
            <span>Comic Store</span>
        </div>
    </a>
    <nav class="menu">
        <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
        <div class="dropdown">
            <a href="#">Thể loại &#9662;</a>
            <div class="dropdown-content">
                <%
                    List<Category> listCategories = (List<Category>) request.getAttribute("listCategories");
                    if (listCategories == null || listCategories.isEmpty()) {
                        CategoriesDao categoriesDao = new CategoriesDao();
                        listCategories = categoriesDao.listCategories();
                    }

                    if (listCategories != null && !listCategories.isEmpty()) {
                        for (Category c : listCategories) {
                            if (c.getIs_hidden() == 0) {
                %>
                <a href="${pageContext.request.contextPath}/userCategory?id=<%= c.getId() %>">
                    <%= c.getNameCategories() %>
                </a>
                <%
                        }
                    }
                } else {
                %>
                <a href="#">Không có thể loại</a>
                <%
                    }
                %>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/fontend/public/AbouUS.jsp">Liên hệ</a>
    </nav>

    <div class="search-bar">
        <div class="search-box">
            <form action="${pageContext.request.contextPath}/search" method="get" accept-charset="UTF-8">
                <input type="text"
                       id="searchInput"
                       name="keyword"
                       placeholder="Tìm truyện..."
                       class="search-input"
                       autocomplete="off"
                       value="${param.keyword != null ? param.keyword : ''}">
                <button type="submit" class="search-button">
                    <i class="fas fa-magnifying-glass"></i>
                </button>
            </form>

            <div id="searchDropdown" class="search-history-dropdown">
                <div class="history-header">
                    <span>Lịch sử tìm kiếm</span>
                    <span class="clear-all" id="clearAll">Xóa tất cả</span>
                </div>

                <div class="history-tags-container" id="historyTagsContainer">
                </div>
            </div>
        </div>
    </div>

    <div class="contain-left">
        <div class="actions">
            <div class="notify-wrapper">
                <a href="#" class="bell-icon" id="bell-icon">
                    <i class="fa-solid fa-bell"></i>
                    <span class="notification-badge" id="notification-badge" style="display: none;">0</span>
                </a>
                <div class="notification-panel" id="notification-panel">
                    <div class="notification-header">
                        <div class="inform-num">
                            <i class="fa-solid fa-bell"></i>
                            <span>Thông báo</span>
                            <span class="notification-badge-count" id="header-badge-count">(0)</span>
                        </div>
                        <div class="inform-all">
                            <a href="${pageContext.request.contextPath}/fontend/nguoiB/notifications.jsp">Xem tất cả</a>
                        </div>
                    </div>

                    <div class="notification-list" id="header-notification-list">
                        <div class="empty-noti">Chưa có thông báo mới</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="actions cart-icon-wrapper">
            <a href="${pageContext.request.contextPath}/cart" class="cart-icon">
                <i class="fa-solid fa-cart-shopping"></i>
                <c:if test="${not empty sessionScope.cart && fn:length(sessionScope.cart.items) > 0}">
                    <span class="cart-badge">${fn:length(sessionScope.cart.items)}</span>
                </c:if>
            </a>
        </div>

        <div class="actions user-nav">
            <i class="fa-solid fa-user" id="user"></i>
            <div class="dropdown-user">
                <a href="${pageContext.request.contextPath}/updateUser">Người dùng</a>

                <%
                    User curnentUser = (User) session.getAttribute("currentUser");
                    if (curnentUser != null) {
                %>
                <a href="<%= request.getContextPath() %>/logout">Đăng xuất</a>
                <%
                } else {

                %>
                <a href="<%= request.getContextPath() %>/login">Đăng nhập</a>
                <%
                    }
                %>
            </div>
        </div>
    </div>

    <style>
        .social-float-container {
            position: fixed;
            bottom: 24px;
            right: 24px;
            z-index: 9999;
            display: flex;
            flex-direction: column-reverse;
            align-items: center;
            gap: 10px;
        }

        .social-toggle-btn {
            width: 52px;
            height: 52px;
            border-radius: 50%;
            background: #fff;
            border: none;
            cursor: pointer;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 22px;
            color: #fff;
            transition: transform 0.3s ease, background 0.3s ease;
        }

        .social-toggle-btn:hover {
            transform: scale(1.05);
        }

        .social-toggle-btn.open {
            /*background: #fff;*/
            transform: rotate(20deg);
        }

        .social-items {
            background: #fff;
            display: flex;
            flex-direction: column-reverse;
            align-items: center;
            gap: 10px;
            overflow: hidden;
            max-height: 0;
            opacity: 0;
            transition: max-height 0.4s ease, opacity 0.3s ease;
        }

        .social-items.open {
            max-height: 200px;
            opacity: 1;
            background: #fff;
        }

        .social-float-btn {
            width: 46px;
            height: 46px;
            border-radius: 50%;
            overflow: hidden;
            box-shadow: 0 4px 14px rgba(0, 0, 0, 0.18);
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            display: block;
        }

        .social-float-btn:hover {
            transform: scale(1.12);
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
        }

        .social-float-btn img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .social-float-btn {
            position: relative;
        }

        .social-float-btn::before {
            content: attr(data-tooltip);
            position: absolute;
            right: 54px;
            top: 50%;
            transform: translateY(-50%);
            background: rgba(0, 0, 0, 0.7);
            color: #fff;
            font-size: 12px;
            padding: 4px 10px;
            border-radius: 6px;
            white-space: nowrap;
            opacity: 0;
            pointer-events: none;
            transition: opacity 0.2s ease;
        }

        .social-float-btn:hover::before {
            opacity: 1;
        }
    </style>

    <div class="social-float-container">
        <button class="social-toggle-btn" id="socialToggle" title="Liên hệ">
            <img src="https://cdn-icons-png.flaticon.com/128/134/134718.png" alt="Contact"
                 style="width:28px;height:28px;object-fit:contain;">
        </button>
        <div class="social-items" id="socialItems">
            <a class="social-float-btn" href="https://zalo.me/0394158994" target="_blank" data-tooltip="Zalo">
                <img src="https://upload.wikimedia.org/wikipedia/commons/9/91/Icon_of_Zalo.svg" alt="Zalo">
            </a>
            <a class="social-float-btn" href="https://www.facebook.com/share/1MVc1miHnd/" target="_blank" data-tooltip="Facebook">
                <img src="https://upload.wikimedia.org/wikipedia/commons/b/b9/2023_Facebook_icon.svg" alt="Facebook">
            </a>
            <a class="social-float-btn" href="https://www.instagram.com/comic.store/" target="_blank"
               data-tooltip="Instagram">
                <img src="https://upload.wikimedia.org/wikipedia/commons/e/e7/Instagram_logo_2016.svg" alt="Instagram">
            </a>
        </div>
    </div>

    <script>
        const socialToggle = document.getElementById('socialToggle');
        const socialItems = document.getElementById('socialItems');

        socialToggle.addEventListener('click', () => {
            socialToggle.classList.toggle('open');
            socialItems.classList.toggle('open');
        });
        document.addEventListener('click', (e) => {
            if (!e.target.closest('.social-float-container')) {
                socialToggle.classList.remove('open');
                socialItems.classList.remove('open');
            }
        });
    </script>
</header>
<script>
    const searchInput = document.getElementById('searchInput');
    const dropdown = document.getElementById('searchDropdown');
    const tagsContainer = document.getElementById('historyTagsContainer');
    const clearAllBtn = document.getElementById('clearAll');

    const STORAGE_KEY = 'comicstore_search_history';
    let searchHistory = JSON.parse(localStorage.getItem(STORAGE_KEY)) || [];

    function saveHistory() {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(searchHistory));
    }

    function removeHistoryItem(term) {
        searchHistory = searchHistory.filter(t => t !== term);
        saveHistory();
        renderHistory();
    }

    function renderHistory() {
        tagsContainer.innerHTML = '';

        if (!searchHistory.length) {
            tagsContainer.innerHTML = '<div style="padding:12px 16px;color:#999;font-style:italic;text-align:center">Không có lịch sử</div>';
            return;
        }

        searchHistory.slice(0, 20).forEach(term => {
            const tag = document.createElement('div');
            tag.className = 'history-tag';

            const text = document.createElement('span');
            text.textContent = term;
            text.onclick = () => {
                searchInput.value = term;
                hideDropdown();
                searchInput.form.submit();
            };

            const removeBtn = document.createElement('span');
            removeBtn.className = 'remove';
            removeBtn.innerHTML = '×';
            removeBtn.onclick = (e) => {
                e.stopPropagation();
                removeHistoryItem(term);
            };

            tag.appendChild(text);
            tag.appendChild(removeBtn);
            tagsContainer.appendChild(tag);
        });
    }

    function showDropdown() {
        renderHistory();
        dropdown.classList.add('show');
    }

    function hideDropdown() {
        dropdown.classList.remove('show');
    }

    searchInput.addEventListener('focus', showDropdown);
    searchInput.addEventListener('input', showDropdown);

    document.addEventListener('click', (e) => {
        if (!dropdown.contains(e.target) && e.target !== searchInput) {
            hideDropdown();
        }
    });

    window.addEventListener('scroll', hideDropdown);

    searchInput.form.addEventListener('submit', (e) => {
        const term = searchInput.value.trim();
        if (!term) {
            e.preventDefault();
            return;
        }

        const searchButton = document.querySelector('.search-button');
        if (searchButton) {
            searchButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
        }

        searchHistory = searchHistory.filter(t => t !== term);
        searchHistory.unshift(term);
        if (searchHistory.length > 20) searchHistory.pop();

        saveHistory();
    });

    clearAllBtn.onclick = () => {
        if (confirm('Xóa toàn bộ lịch sử?')) {
            searchHistory = [];
            saveHistory();
            renderHistory();
        }
    };
</script>

<c:if test="${not empty sessionScope.currentUser}">
    <script>
        async function loadHeaderNotifications() {
            const url = '${pageContext.request.contextPath}/NotificationServlet/recent?limit=8';

            try {
                const response = await fetch(url);

                if (!response.ok) {
                    const text = await response.text();
                    throw new Error('Network error: ' + response.status);
                }

                const data = await response.json();
                const count = data.unread_count || 0;
                const badge = document.getElementById('notification-badge');
                const badgeCount = document.getElementById('header-badge-count');
                if (badge && badgeCount) {
                    badge.textContent = count;
                    badge.style.display = count > 0 ? 'flex' : 'none';
                    badgeCount.textContent = `(${count})`;
                }
                const list = document.getElementById('header-notification-list');
                if (!list) {
                    return;
                }
                if (!data.notifications || data.notifications.length === 0) {
                    list.innerHTML = '<div class="empty-noti">Chưa có thông báo mới</div>';
                    return;
                }
                let html = '';
                data.notifications.forEach(n => {
                    const unreadClass = (n.is_read === false) ? 'unread' : '';

                    let displayMessage = '(Không có nội dung)';
                    if (n.message && typeof n.message === 'string' && n.message.trim()) {
                        const firstLine = n.message.trim().split('\n')[0];
                        displayMessage = firstLine.length > 100
                            ? firstLine.substring(0, 100) + '...'
                            : firstLine;
                    }

                    const formattedTime = n.formatted_date || '';

                    html += '<div class="header-noti-item ' + unreadClass + '" data-id="' + n.id + '" data-type="' + (n.type || '') + '">'
                        + '<div class="noti-icon">' + '</div>'
                        + '<div class="noti-content">'
                        + '<div class="noti-message">' + displayMessage + '</div>'
                        + '<div class="noti-time">' + formattedTime + '</div>'
                        + '</div>'
                        + '</div>';
                });

                list.innerHTML = html;
                document.querySelectorAll('.header-noti-item').forEach(item => {
                    item.addEventListener('click', async function () {
                        const id = this.dataset.id;
                        const type = this.dataset.type || '';

                        let tabType = 'ALL';
                        if (type.startsWith('ORDER')) tabType = 'ORDER';
                        else if (type === 'PROMOTION') tabType = 'EVENT';

                        try {
                            await fetch('${pageContext.request.contextPath}/NotificationServlet/mark-read?id=' + id, {
                                method: 'POST'
                            });
                        } catch (err) {
                            console.error('Lỗi đánh dấu đã đọc:', err);
                        }
                        window.location.href = '${pageContext.request.contextPath}/fontend/nguoiB/notifications.jsp?type=' + tabType + '#noti-' + id;
                    });
                });

            } catch (err) {
                const list = document.getElementById('header-notification-list');
                if (list) {
                    list.innerHTML = '<div class="empty-noti">Lỗi kết nối. Thử lại sau.</div>';
                }
            }
        }
        document.addEventListener('DOMContentLoaded', () => {

            const bell = document.getElementById('bell-icon');
            const panel = document.getElementById('notification-panel');

            if (!bell || !panel) {
                return;
            }


            bell.addEventListener('click', function (e) {
                e.preventDefault();
                e.stopPropagation();


                if (panel.style.display === 'block') {
                    panel.style.display = 'none';
                } else {
                    panel.style.display = 'block';
                    loadHeaderNotifications();
                }
            });


            document.addEventListener('click', function (e) {
                if (!bell.contains(e.target) && !panel.contains(e.target)) {
                    if (panel.style.display === 'block') {
                        panel.style.display = 'none';
                    }
                }
            });


            console.log("Loading initial badge count...");
            fetch('${pageContext.request.contextPath}/NotificationServlet/count')
                .then(r => {
                    console.log("Badge count response:", r.status);
                    return r.json();
                })
                .then(d => {
                    const count = d.unread_count || 0;
                    const badge = document.getElementById('notification-badge');
                    if (badge) {
                        badge.textContent = count;
                        badge.style.display = count > 0 ? 'flex' : 'none';
                    }
                })
                .catch(err => {
                    console.error('Lỗi load badge count:', err);
                });
        });

        setInterval(() => {
            fetch('${pageContext.request.contextPath}/NotificationServlet/count')
                .then(r => r.json())
                .then(d => {
                    const count = d.unread_count || 0;
                    const badge = document.getElementById('notification-badge');
                    const oldCount = parseInt(badge.textContent) || 0;

                    if (count > oldCount && count > 0) {
                        badge.classList.add('badge-pulse');
                        setTimeout(() => badge.classList.remove('badge-pulse'), 1000);
                    }

                    badge.textContent = count;
                    badge.style.display = count > 0 ? 'flex' : 'none';
                })
                .catch(err => console.error('Auto refresh badge error:', err));
        }, 60000);
    </script>

    <script>
        document.body.dataset.userId = '${sessionScope.currentUser.id}';
        document.body.dataset.loggedIn = 'true';
        document.body.dataset.contextPath = '${pageContext.request.contextPath}';
    </script>

    <script src="https://www.gstatic.com/firebasejs/10.7.1/firebase-app-compat.js"></script>
    <script src="https://www.gstatic.com/firebasejs/10.7.1/firebase-messaging-compat.js"></script>

    <script src="${pageContext.request.contextPath}/js/firebase-notification.js"></script>
</c:if>

<c:if test="${empty sessionScope.currentUser}">
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const bell = document.getElementById('bell-icon');
            if (!bell) return;

            bell.addEventListener('click', function (e) {
                e.preventDefault();
                e.stopPropagation();
                showLoginHintToast();
            });
        });

        function showLoginHintToast() {
            const existing = document.getElementById('login-hint-toast');
            if (existing) existing.remove();

            const contextPath = '${pageContext.request.contextPath}';

            const toast = document.createElement('div');
            toast.id = 'login-hint-toast';
            toast.innerHTML =
                '<i class="fa-solid fa-bell" style="margin-right:8px;opacity:0.85;"></i>' +
                'Vui lòng <a href="' + contextPath + '/login">đăng nhập</a> để xem thông báo';

            document.body.appendChild(toast);

            requestAnimationFrame(() => toast.classList.add('show'));

            setTimeout(() => {
                toast.classList.remove('show');
                setTimeout(() => toast.remove(), 300);
            }, 3000);

            document.addEventListener('click', function handler(e) {
                if (!toast.contains(e.target) && e.target !== bell) {
                    toast.classList.remove('show');
                    setTimeout(() => toast.remove(), 300);
                    document.removeEventListener('click', handler);
                }
            });
        }
    </script>
</c:if>
