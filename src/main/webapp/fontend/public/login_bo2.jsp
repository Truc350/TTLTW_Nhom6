<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="https://fonts.googleapis.com/css?family=Roboto:300,400&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/fontend/css/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/fontend/css/css/style.css">

    <link rel="stylesheet" href="fonts/icomoon/style.css">
    <link rel="stylesheet" href="css/owl.carousel.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <title>Login</title>
</head>
<body>

<div class="d-lg-flex half">

    <div class="bg order-1 order-md-2"
         style="background-image: url(${pageContext.request.contextPath}/img/anhLogin.png);"></div>

    <div class="contents order-2 order-md-1">

        <div class="container">
            <div class="row align-items-center justify-content-center">
                <div class="col-md-7">
                    <h3><strong>Đăng nhập Comic Store</strong></h3>
                    <p class="mb-4">Hãy nhập thông tin để bắt đầu sử dụng dịch vụ mua sắm.</p>

                    <c:if test="${not empty error}">
                        <p class="text-danger">
                                ${error}
                        </p>
                    </c:if>



                    <!-- Form login -->
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="form-group first">
                            <label for="username">Tên đăng nhập hoặc Email</label>
                            <input type="text" class="form-control" id="username" name="username"
                                   placeholder="Tên đăng nhập hoặc gmail" required>
                        </div>
                        <div class="form-group last mb-3">
                            <label for="password">Mật khẩu</label>
                            <div class="input-group">
                                <input type="password" class="form-control" id="password" name="password"
                                       placeholder="Mật khẩu" required>
                                <div class="input-group-append">
                                    <button class="btn btn-outline-secondary" type="button" id="togglePassword">
                                        <i class="fa fa-eye"></i>
                                    </button>
                                </div>
                            </div>
                        </div>

                        <div class="d-flex mb-5 align-items-center">
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" id="remember" name="remember" checked>
                                <label class="form-check-label" for="remember">Nhớ mật khẩu</label>
                            </div>
                            <span class="ml-auto"><a
                                    href="${pageContext.request.contextPath}/fontend/public/ForgotPass.jsp"
                                    class="forgot-pass">Quên mật khẩu</a></span>
                        </div>

                        <input type="submit" value="Đăng nhập" class="btn btn-block btn-primary">
                    </form>

                    <div>
                        <span class="signup-tran"><a
                                href="${pageContext.request.contextPath}/fontend/public/Register.jsp"
                                class="forgot-pass">Đăng kí tài khoản</a></span>
                    </div>


                    <!-- Social Login -->
                    <div class="text-center mt-4" id="social-login">
                        <p>Hoặc đăng nhập bằng</p>
                        <div class="d-flex justify-content-center">
                            <a href="#" class="btn btn-danger mr-2 social-btn">
                                <i class="fab fa-google mr-1"></i> Google
                            </a>
                            <a href="https://www.facebook.com/v20.0/dialog/oauth?
                                 client_id=<%= System.getenv("FACEBOOK_APP_ID") %>
                              &redirect_uri=${pageContext.request.contextPath}/login-facebook-callback
                             &scope=email
                             &response_type=code
                                &state=xyz123"
                               class="btn btn-primary social-btn">
                                <i class="fab fa-facebook-f mr-1"></i> Facebook
                            </a>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<%--<script src="js/jquery-3.3.1.min.js"></script>--%>
<%--<script src="js/popper.min.js"></script>--%>
<%--<script src="js/bootstrap.min.js"></script>--%>
<%--<script src="js/main.js"></script>--%>

<script>
    // document.addEventListener("DOMContentLoaded", function () {
    //     const form = document.querySelector("form");
    //     const passwordInput = document.getElementById("password");
    //
    //     form.addEventListener("submit", function (event) {
    //         const password = passwordInput.value;
    //
    //         // Regex kiểm tra: ít nhất 8 ký tự, có chữ hoa, chữ thường, số và ký tự đặc biệt
    //         const regex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    //
    //         if (!regex.test(password)) {
    //             event.preventDefault(); // chặn submit
    //             alert("Mật khẩu phải có ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt!");
    //         }
    //     });
    // });


    document.addEventListener("DOMContentLoaded", function () {
        const togglePassword = document.getElementById("togglePassword");
        const passwordInput = document.getElementById("password");

        togglePassword.addEventListener("click", function () {
            const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
            passwordInput.setAttribute("type", type);

            // Đổi icon khi toggle
            this.querySelector("i").classList.toggle("fa-eye");
            this.querySelector("i").classList.toggle("fa-eye-slash");
        });
    });


</script>

</body>
</html>