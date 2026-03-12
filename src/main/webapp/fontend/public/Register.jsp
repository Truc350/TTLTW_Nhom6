<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/fontend/css/publicCss/signIn.css">
</head>
<body>

<div class="container">
    <div class="left">
        <div class="box">
            <h2>Đăng ký</h2>

            <p id="clientError"
               style="color:red; font-size:14px; font-weight:bold;
          text-align:center; display:none; margin-bottom:5px;">
            </p>
            <c:if test="${not empty error}">
                <p class=""
                   style="color : red; font-size: 14px; display: flex;font-weight: bold; justify-content: center;margin-bottom: 5px">${error}</p>
            </c:if>
            <form action="${pageContext.request.contextPath}/RegisterServlet" method="post"
                  onsubmit="return validateRegister()">
                <label>Nhập tên đăng nhập:</label>
                <input id="username" type="text" placeholder="" name="username">

                <%--                <label>Nhập email:<span style="color:#999; font-size:12px;">(hoặc số điện thoại)</span></label>--%>
                <%--                <input id="email" type="email" placeholder="" name="email">--%>

                <%--                <label>Số điện thoại: <span style="color:#999; font-size:12px;">(hoặc email)</span></label>--%>
                <%--                <input id="phone" type="tel" placeholder="Nhập số điện thoại" name="phone">--%>
                <div class="email-phone-group">
                    <div class="field">
                        <label>Email</label>
                        <input id="email" type="email" placeholder="Nhập email" name="email">
                    </div>
                    <div class="divider">hoặc</div>
                    <div class="field">
                        <label>Số điện thoại</label>
                        <input id="phone" type="tel" placeholder="Nhập số điện thoại" name="phone">
                    </div>
                </div>
                <label>Nhập mật khẩu:</label>
                <div class="password-box">
                    <input id="password" type="password" placeholder="" name="password">
                    <img src="${pageContext.request.contextPath}/img/eyePassword.png" class="eye toggle1">
                </div>

                <label>Xác nhận lại mật khẩu:</label>
                <div class="password-box">
                    <input id="confirmPassword" type="password" placeholder="" name="confirmPassword">
                    <img src="${pageContext.request.contextPath}/img/eyePassword.png" class="eye toggle2">
                </div>

                <button type="submit" value="Đăng ký" id="registerBtn">Đăng ký</button>
                <p style="text-align:center; margin-top:15px; font-size:14px; color:#333;">
                    Bạn đã có tài khoản?
                    <a href="${pageContext.request.contextPath}/login"
                       style="color:#0276DA; font-weight:bold; text-decoration:none;">
                        Đăng nhập
                    </a>
                </p>
            </form>

        </div>

    </div>
    <div class="right">
        <img src="${pageContext.request.contextPath}/img/anhLogin.png" alt="Books">
    </div>

</div>
<div class="popup-success" id="successPopup">
    <div class="success-box">
        <h2>Đăng ký thành công</h2>
        <div class="check-icon"></div>
    </div>
</div>

<script>
    function togglePassword(eyeClass) {
        const eye = document.querySelector("." + eyeClass);
        const input = eye.previousElementSibling;

        eye.addEventListener("click", () => {
            if (input.type === "password") {
                input.type = "text";
                eye.src = "${pageContext.request.contextPath}/img/eyePasswordHide.png";
            } else {
                input.type = "password";
                eye.src = "${pageContext.request.contextPath}/img/eyePassword.png";
            }
        });
    }

    togglePassword("toggle1");
    togglePassword("toggle2");
</script>

<c:if test="${not empty success}">
    <script>
        const successPopup = document.getElementById("successPopup");
        successPopup.style.display = "flex";
        setTimeout(() => {
            window.location.href = "${pageContext.request.contextPath}/login";
        }, 2000);
    </script>
</c:if>


</body>

<script>
    function validateRegister() {
        const username = document.getElementById("username").value.trim();
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value.trim();
        const confirmPassword = document.getElementById("confirmPassword").value.trim();
        const phone = document.getElementById("phone").value.trim();
        const clientError = document.getElementById("clientError");
        const serverError = document.getElementById("serverError");
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[\W_]).{8,}$/;
        const phoneRegex = /^(0[35789])[0-9]{8}$/;

        clientError.style.display = "none";
        clientError.innerText = "";
        if (serverError) serverError.style.display = "none";

        if (!username || !password || !confirmPassword) {
            showRegisterError("Vui lòng nhập đầy đủ thông tin");
            return false;
        }
        if (!email && !phone) {
            showRegisterError("Vui lòng nhập ít nhất email hoặc số điện thoại");
            return false;
        }

        if (email && !emailRegex.test(email)) {
            showRegisterError("Email không đúng định dạng");
            return false;
        }
        if (phone && !phoneRegex.test(phone)) {
            showRegisterError("Số điện thoại không đúng định dạng (VD: 0912345678)");
            return false;
        }

        if (!passwordRegex.test(password)) {
            showRegisterError(
                "Mật khẩu phải hơn 8 ký tự, gồm chữ hoa, chữ thường và ký tự đặc biệt"
            );
            return false;
        }

        if (password !== confirmPassword) {
            showRegisterError("Mật khẩu xác nhận không khớp");
            return false;
        }

        return true;
    }

    function showRegisterError(msg) {
        const clientError = document.getElementById("clientError");
        const serverError = document.getElementById("serverError");

        if (serverError) serverError.style.display = "none";

        clientError.innerText = msg;
        clientError.style.display = "block";
    }

</script>
<script>
    ["username", "email","phone", "password", "confirmPassword"].forEach(id => {
        document.getElementById(id).addEventListener("input", clearRegisterErrors);
    });

    function clearRegisterErrors() {
        const clientError = document.getElementById("clientError");
        const serverError = document.getElementById("serverError");

        if (clientError) clientError.style.display = "none";
        if (serverError) serverError.style.display = "none";
    }
</script>


</html>