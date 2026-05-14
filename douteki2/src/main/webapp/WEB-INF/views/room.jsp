
<h2>DM</h2>

<div>
  <th:block th:each="m : ${messages}">
    <p>
      <span th:text="${m.senderId}"></span>：
      <span th:text="${m.text}"></span>
    </p>
  </th:block>
</div>

<form action="/room/send" method="post">
  <input type="hidden" name="roomId" th:value="${roomId}">
  <input type="text" name="text">
  <button type="submit">送信</button>
</form>