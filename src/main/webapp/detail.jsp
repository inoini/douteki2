<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*,model.Post"%>
<%@ page import="model.Post"%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
<html>


<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/style.css">
</head>

<body>
<h2>投稿詳細</h2>
<%
Post p = (Post) request.getAttribute("post");

if (p == null) {
%>
<p>データがありません</p>
<%
return;
}
%>



<div class="detail">
    <div class="name"><%= p.getName() %></div>
</div>


<div class="messageBox"><%=p.getMessage().trim()%></div>




<%
if (p.getSnsUrl() != null && !p.getSnsUrl().isEmpty()) {
%>
<p>
	SNS①： <a href="<%=p.getSnsUrl()%>" target="_blank"
		rel="noopener noreferrer"> <%=p.getSnsUrl()%>
	</a>
</p>
<%
}
%>
<%
if (p.getSnsUrl2() != null && !p.getSnsUrl2().isEmpty()) {
%>
<p>
	SNS②： <a href="<%=p.getSnsUrl2()%>" target="_blank"
		rel="noopener noreferrer"> <%=p.getSnsUrl2()%>
	</a>
</p>
<%
}
%>
<p>
	Discordユーザー名： <span id="discordName">@<%=p.getDiscordName()%></span>
</p>
<button class="mainBtn"
	onclick="location.href='<%=request.getContextPath()%>/list'">

	戻る</button>
<form action="<%=request.getContextPath()%>/delete" method="post">
    <input type="hidden" name="id" value="<%=p.getId()%>">
    <button type="submit" class="deleteBtn">削除</button>
</form>
<button onclick="copyDiscord()">DISCORDコピー</button>

<script>
	function copyDiscord() {
		const text = document.getElementById("discordName").innerText;
		navigator.clipboard.writeText(text);
		alert("コピーしました: " + text);
	}
</script>
</body>
</html>