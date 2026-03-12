<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/fontend/css/publicCss/login1.css">

</head>
<body>

<div class="container">
    <div class="login-section">
        <div class="login-box">
            <form action="${pageContext.request.contextPath}/login" method="post" onsubmit="return validateLogin()">
                <h2>Đăng nhập</h2>
                <p id="clientError"
                   style="color:red; font-size:14px; font-weight: bold; text-align:center; display:none;">
                </p>

                <c:if test="${not empty error}">
                    <h3 id="serverError"
                        style="color:red; font-size:14px; text-align:center;">
                            ${error}
                    </h3>
                </c:if>

                <label>Nhập Email hoặc Tên đăng nhập:</label>
                <input id="username" placeholder="Email hoặc tên đăng nhập" name="username">

                <label>Nhập mật khẩu:</label>
                <div class="password-box">
                    <input id="password" type="password" placeholder="Mật khẩu" name="password">
                    <span class="eye">
                    <img src="${pageContext.request.contextPath}/img/eyePassword.png" id="toggleEye" alt="eye">
                </span>
                </div>

                <div class="links">
                    <a href="${pageContext.request.contextPath}/RegisterServlet">Đăng ký tài khoản</a>
                    <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu</a>
                </div>

                <button type="submit" value="Đăng nhập">Đăng nhập</button>

            </form>
            <p class="or">Hoặc đăng nhập bằng</p>

            <div class="social">
                <img class="google" id="googleLogin"
                     src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ13Zhu7gCjSl_MTncNr7aEbl9HkMWaidA4wNFbPO0oxQm49i-RPgTEME3oN_nSroHl1KqwY2haK-IFEYfBc1BkwP9PBNEv9ApC9tvzcVM&s"
                     alt="Google"
                     style="cursor: pointer;">

                <img class="facebook" id="facebookLogin"
                     src="https://upload.wikimedia.org/wikipedia/commons/b/b9/2023_Facebook_icon.svg"
                     alt="Facebook"
                     style="cursor: pointer; width: 40px; height: 40px;">
            </div>

        </div>
    </div>
    <div class="image-section">
        <img src="${pageContext.request.contextPath}/img/anhLogin.png" alt="Books">
    </div>
</div>

<script>
    const eye = document.getElementById("toggleEye");
    const passwordInput = document.querySelector(".password-box input");

    if (eye && passwordInput) {
        eye.addEventListener("click", () => {
            if (passwordInput.type === "password") {
                passwordInput.type = "text";
                eye.src = "${pageContext.request.contextPath}/img/eyePasswordHide.png";
            } else {
                passwordInput.type = "password";
                eye.src = "${pageContext.request.contextPath}/img/eyePassword.png";
            }
        });
    }
    const googleBtn = document.getElementById("googleLogin");
    if (googleBtn) {
        googleBtn.addEventListener("click", () => {
            window.location.href = "${pageContext.request.contextPath}/login-google";
        });
    }
    const facebookBtn = document.getElementById("facebookLogin");
    if (facebookBtn) {
        facebookBtn.addEventListener("click", () => {
            window.location.href = "https://www.facebook.com/v18.0/dialog/oauth?client_id=4112997315630876&redirect_uri=http://localhost:8080/LTW_Nhom5/login-facebook-callback&scope=email,public_profile";
        });
    }
</script>
<script>
    function validateLogin() {
        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();
        const errorBox = document.getElementById("clientError");

        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[\W_]).{8,}$/;
        errorBox.style.display = "none";
        errorBox.innerText = "";

        if (username === "" || password === "") {
            showError("Vui lòng nhập đầy đủ tài khoản và mật khẩu");
            return false;
        }

        if (!passwordRegex.test(password)) {
            showError("Mật khẩu phải hơn 8 ký tự, gồm chữ hoa, chữ thường và ký tự đặc biệt");
            return false;
        }

        return true;
    }

    function showError(msg) {
        const errorBox = document.getElementById("clientError");
        const serverError = document.getElementById("serverError");
        if (serverError) {
            serverError.style.display = "none";
        }

        errorBox.innerText = msg;
        errorBox.style.display = "block";
    }

</script>
<script>
    document.getElementById("username").addEventListener("input", clearErrors);
    document.getElementById("password").addEventListener("input", clearErrors);

    function clearErrors() {
        const clientError = document.getElementById("clientError");
        const serverError = document.getElementById("serverError");

        if (clientError) clientError.style.display = "none";
        if (serverError) serverError.style.display = "none";
    }
</script>


</body>
</html>