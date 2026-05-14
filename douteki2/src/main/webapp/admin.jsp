<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*,com.example.app.Post"%>
<%@ page import="jakarta.servlet.http.*" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>
<%
String role = (String)session.getAttribute("role");

if(role == null || !role.equals("admin")){
    response.sendRedirect("adminLogin.jsp");
    return;
}
%>
<h2>掲示板</h2>
<head>
<title>投稿一覧</title>

<link rel="stylesheet" href="css/style.css">
</head>
<a class="mainBtn"
href="<%=request.getContextPath()%>/list">
利用者画面へ戻る
</a>

<hr>

<h2>投稿一覧</h2>

<!-- 投稿画面へ -->
<%
Integer currentPage = (Integer) request.getAttribute("currentPage");

if (currentPage == null) {
	currentPage = 1;
}
%>
<hr>
<div class="container">
	<%
	List<Post> posts = (List<Post>) request.getAttribute("posts");

	if (posts != null && posts.size() > 0) {
		for (Post p : posts) {
			String msg = StringEscapeUtils.escapeHtml4(p.getMessage());

		    msg = msg.replaceAll(
		        "(https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+)",
		        "<a href='$1' target='_blank' rel='noopener noreferrer'>$1</a>"
		    );
			
	%>

	<div class="card">

		<div class="name"><%=p.getName()%></div>

		<div class="message"><%=msg%></div>
		<!-- ★SNSリンク① -->

		<!-- 詳細 -->
	<div class="buttons">
		<a class="mainBtn"
			href="<%=request.getContextPath()%>/detail?id=<%=p.getUserId()%>"> 詳細

		</a>

		<!-- 削除 -->
	<form action="<%=request.getContextPath()%>/delete" method="post" style="display:inline;">
    <input type="hidden" name="id" value="<%=p.getUserId()%>">
    <button type="submit" class="deleteBtn"
        onclick="return confirm('削除しますか？');">
        削除
    </button>
</form>
</div>
	</div>

	<%
	}
	}
	%>
</div>
<div style="margin-top: 20px; text-align: center;">

	<%
	if (currentPage > 1) {
	%>
	<a class="pageBtn" href="?page=<%=currentPage - 1%>"> ← </a>
	<%
	}
	%>

	<%
	for (int i = 1; i <= 20; i++) {
	%>

	<a class="pageBtn" href="?page=<%=i%>"> <%=i%>

	</a>

	<%
	}
	%>

	<%
	if (currentPage < 20) {
	%>
	<a class="pageBtn" href="?page=<%=currentPage + 1%>"> → </a>
	<%
	}
	%>

</div>
<!-- ポップアップ -->
<div id="modal"
	style="display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); width: 450px; background: #fff; border: 1px solid #aaa; padding: 25px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);">
	

	<form action="<%=request.getContextPath()%>/adminLogin" method="post">

		<div class="form-group">
			<label>名前</label> <input type="text" name="name">
		</div>

		<div class="form-group">
			<label>メッセージ</label>
			<textarea name="message"></textarea>
		</div>

		<div class="form-group">
			<label>SNSリンク</label> <input type="text" name="snsUrl"
				placeholder="@username" onblur="convertTwitterId(this)">
		</div>

		<div class="form-group">
			<label>SNSリンク②</label><input type="text" name="snsUrl2"
				placeholder="@instagram" onblur="convertInstagramId(this)">
		</div>

		<div class="form-group">
			<label>Discordユーザー名</label> <input type="text" name="discordName"
				placeholder="@username">
		</div>

		<br>
		<div class="buttonArea">

			<input type="submit" value="投稿" class="mainBtn">

			<button type="button" class="subBtn" onclick="closeModal()">

				閉じる</button>

		</div>
	</form>
</div>
<script>
	function convertTwitterId(input) {

		let value = input.value.trim();

		if (value === "")
			return;

		// @ が先頭なら削除
		if (value.startsWith("@")) {
			value = value.substring(1);
		}

		// twitter URL化
		if (!value.startsWith("http://") && !value.startsWith("https://")) {
			input.value = "https://twitter.com/" + value;
		}
	}

	function convertInstagramId(input) {

		let value = input.value.trim();

		if (value === "")
			return;

		// @ を削除
		if (value.startsWith("@")) {
			value = value.substring(1);
		}

		// URLでなければInstagram URL化
		if (!value.startsWith("http://") && !value.startsWith("https://")) {
			input.value = "https://instagram.com/" + value;
		}
	}

	function openModal() {
		document.getElementById("modal").style.display = "block";
	}
	function closeModal() {
		document.getElementById("modal").style.display = "none";
	}
</script>