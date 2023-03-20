<%--
  Created by IntelliJ IDEA.
  User: matth
  Date: 14/03/2023
  Time: 09:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta charset="">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>connexion</title>
  <script type="module" src="${pageContext.request.contextPath}/js/request.js" defer></script>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Connexion.css" />
</head>

<section>
  <div class="container">
    <form method="post" action="ese-oye">
      <c:if test="${error}">
        <div class="error">
          <p>Erreur d'authentification</p
        </div>
      </c:if>
      <h1>Connection</h1>
      <input type="text" name="mail" placeholder="Mail" required>
      <br/><br/>
      <input type="password" name="password" placeholder="Mot de passe" required>
      <br/><br/>
      <input class="btn fill" type="submit" value="Se connecter">
    </form>

  </div>

    <input class="btn fill" type="submit" value="pas encore inscrit ?" onclick="window.Request.sendPost('ese-oye?id=Inscription')">
  <div>

  </div>
</section>



</body>
</html>
