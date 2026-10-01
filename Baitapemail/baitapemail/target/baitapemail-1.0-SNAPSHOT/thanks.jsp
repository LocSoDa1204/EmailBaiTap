<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>MailList - Đăng ký thành công</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
<div class="center">
    <div class="receipt">
        <div class="check">&#10003;</div>
        <h1>Đăng ký thành công</h1>
        <p class="hint">Thông tin của bạn đã được ghi nhận.</p>

        <dl>
            <dt>Email</dt>
            <dd>${user.email}</dd>
            <dt>Tên</dt>
            <dd>${user.firstName}</dd>
            <dt>Họ</dt>
            <dd>${user.lastName}</dd>
        </dl>

        <% if (request.getAttribute("errorMessage") == null) { %>
            <span class="status">Đã gửi email xác nhận</span>
        <% } else { %>
            <span class="status fail">Chưa gửi được email xác nhận</span>
            <div class="alert" style="margin-top:16px;text-align:left">${errorMessage}</div>
        <% } %>

        <a class="btn" href="${pageContext.request.contextPath}/emailList?action=join">Đăng ký thêm</a>
    </div>
</div>
</body>
</html>