<%--
  Created by IntelliJ IDEA.
  User: matth
  Date: 14/03/2023
  Time: 09:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>connexion</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Connexion.css" />
</head>

<div class="title">
  <h1>Connexion</h1>
</div>
<div class="container">
  <form method="post" action="ese-oye">
    <label class="textBox">
      <input type="text" name="mail" placeholder=" " required>
      <span class="text">Mail</span>
    </label>
    <br/>
    <label class="textBox">
      <input type="password" name="password" placeholder=" " required>
      <span class="text">Mot de passe</span>



  </form>

</div>



</body>
</html>
