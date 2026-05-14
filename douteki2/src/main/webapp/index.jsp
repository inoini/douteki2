<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*,com.example.app.Post"%>

<h2>掲示板</h2>

<head>
<title>投稿一覧</title>

<meta name="viewport"
	content="width=device-width, initial-scale=1.0">

<link rel="stylesheet" href="css/style.css">

</head>

<a class="mainBtn"
	href="<%=request.getContextPath()%>/AdminServlet">

	管理者画面

</a>


<!-- 投稿ボタン -->
<button type="button"
	class="mainBtn"
	onclick="openModal()">

	投稿

</button>

<hr>

<h2>投稿一覧</h2>

<%
Integer currentPage =
(Integer) request.getAttribute("currentPage");

if (currentPage == null) {
	currentPage = 1;
}
%>

<hr>

<div class="container">

<%
List<Post> posts =
(List<Post>) request.getAttribute("posts");

if (posts != null && posts.size() > 0) {

	for (Post p : posts) {

		String msg = p.getMessage();

		msg = msg.replaceAll(
		"(https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+)",
		"<a href='$1' target='_blank'>$1</a>");
%>

	<div class="card">

		<!-- 名前 -->
		<div class="name">

			<%= p.getName() %>

			<% if(p.isAdmin()){ %>

				👑

			<% } %>

		</div>

		<!-- メッセージ -->
		<div class="message">

			<%= msg %>

		</div>

		<!-- 詳細 -->
		<a class="mainBtn"
		   href="<%=request.getContextPath()%>/detail?id=<%=p.getUserId()%>">

			詳細

		</a>

		<!-- 時間 -->
		<div class="time">

			<%= p.getCreatedAt() %>

		</div>

	</div>

<%
	}
}
%>

</div>

<!-- ページ -->
<div style="margin-top:20px; text-align:center;">

<%
if(currentPage > 1){
%>

	<a class="pageBtn"
	   href="<%=request.getContextPath()%>/list?page=<%=currentPage - 1%>">

		←

	</a>

<%
}
%>

<%
for(int i = 1; i <= 20; i++){
%>

	<a class="pageBtn"
	   href="<%=request.getContextPath()%>/list?page=<%=i%>">

		<%=i%>

	</a>

<%
}
%>

<%
if(currentPage < 20){
%>

	<a class="pageBtn"
	   href="<%=request.getContextPath()%>/list?page=<%=currentPage + 1%>">

		→

	</a>

<%
}
%>

</div>

<!-- 投稿ポップアップ -->
<div id="modal"
	style="
	display:none;
	position:fixed;
	top:50%;
	left:50%;
	transform:translate(-50%, -50%);
	width:450px;
	background:#fff;
	border:1px solid #aaa;
	padding:25px;
	border-radius:10px;
	box-shadow:0 4px 12px rgba(0,0,0,0.2);
">

<form action="<%=request.getContextPath()%>/post"
	  method="post">

	<div class="form-group">

		<label>名前</label>

		<input type="text"
			   name="name"
			   placeholder="名前">

	</div>

	<div class="form-group">

		<label>メッセージ</label>

		<textarea name="message"
				  placeholder="メッセージ"></textarea>

	</div>

	<div class="form-group">

		<label>SNSリンク</label>

		<input type="text"
			   name="snsUrl"
			   placeholder="@Twitterusername"
			   onblur="convertTwitterId(this)">

	</div>

	<div class="form-group">

		<label>SNSリンク②</label>

		<input type="text"
			   name="snsUrl2"
			   placeholder="@instagramユーザー名"
			   onblur="convertInstagramId(this)">

	</div>

	<div class="form-group">

		<label>Discordユーザー名</label>

		<input type="text"
			   name="discordName"
			   placeholder="@DiscordID名">

	</div>

	<br>

	<div class="buttonArea">

		<input type="submit"
			   value="投稿"
			   class="mainBtn">

		<button type="button"
				class="subBtn"
				onclick="closeModal()">

			閉じる

		</button>

	</div>

</form>

</div>

<script>

function convertTwitterId(input){

	let value = input.value.trim();

	if(value === "") return;

	if(value.startsWith("@")){
		value = value.substring(1);
	}

	if(!value.startsWith("http://") &&
	   !value.startsWith("https://")){

		input.value =
			"https://twitter.com/" + value;
	}
}

function convertInstagramId(input){

	let value = input.value.trim();

	if(value === "") return;

	if(value.startsWith("@")){
		value = value.substring(1);
	}

	if(!value.startsWith("http://") &&
	   !value.startsWith("https://")){

		input.value =
			"https://instagram.com/" + value;
	}
}

function openModal(){
	document.getElementById("modal").style.display = "block";
}

function closeModal(){
	document.getElementById("modal").style.display = "none";
}

</script>