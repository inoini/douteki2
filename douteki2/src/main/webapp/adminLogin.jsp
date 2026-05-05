<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta charset="UTF-8">
<title>管理者ログイン</title>

<style>
body {
	font-family: Arial;
	padding: 20px;
}

input {
	padding: 8px;
	margin: 5px;
	width: 250px;
}

button {
	padding: 8px 15px;
	margin: 5px;
}
</style>

<script src="https://www.gstatic.com/firebasejs/8.10.1/firebase-app.js"></script>
<script src="https://www.gstatic.com/firebasejs/8.10.1/firebase-auth.js"></script>

</head>

<body>

<h2>管理者ログイン</h2>

<form action="${pageContext.request.contextPath}/adminLogin"
      method="post"
      autocomplete="off">

    <input type="text" name="id"
           placeholder="ID"
           autocomplete="off">

    <input type="password" name="pass"
           placeholder="パスワード"
           autocomplete="new-password">

    <button type="submit">ログイン</button>
</form>



</body>
</html>