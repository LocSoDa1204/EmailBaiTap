<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>MailList - Đăng ký nhận tin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
<div class="split">

    <aside class="side">
        <div class="logo">Mail<span>List</span></div>

        <div>
            <h2>Nhận tin mới nhất, ngay trong hộp thư của bạn.</h2>
            <p>Đăng ký một lần, chúng tôi sẽ gửi thông báo về sản phẩm và ưu đãi mới.</p>
            <ul class="perks">
                <li>Xác nhận qua email ngay sau khi đăng ký</li>
                <li>Không gửi trùng cho cùng một địa chỉ</li>
                <li>Thông tin được lưu an toàn trong cơ sở dữ liệu</li>
            </ul>
        </div>

        <small>Bài thực hành Java Servlet/JSP &ndash; JPA &amp; JavaMail</small>
    </aside>

    <section class="panel">
        <div class="form-box">
            <h1>Tạo đăng ký</h1>
            <p class="hint">Điền thông tin của bạn bên dưới.</p>

            <% Object msg = request.getAttribute("message");
               if (msg != null && !"".equals(msg.toString())) { %>
                <div class="alert">${message}</div>
            <% } %>

            <form action="${pageContext.request.contextPath}/emailList" method="post" accept-charset="UTF-8">
                <input type="hidden" name="action" value="add">

                <label for="email">Địa chỉ email</label>
                <input type="email" id="email" name="email" value="${user.email}" required>

                <label for="firstName">Tên</label>
                <input type="text" id="firstName" name="firstName" value="${user.firstName}" required>

                <label for="lastName">Họ</label>
                <input type="text" id="lastName" name="lastName" value="${user.lastName}" required>

                <input type="submit" value="Đăng ký ngay" class="btn">
            </form>
        </div>
    </section>

</div>
</body>
</html>