<%--
  Created by IntelliJ IDEA.
  User: matth
  Date: 08/02/2023
  Time: 16:15
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
  <title>inscription</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Inscription.css" />
  <script src="${pageContext.request.contextPath}/js/inputs.js" defer></script>
  <script type="module" src="${pageContext.request.contextPath}/js/request.js" defer></script>
</head>
<body>

<div class="title">
  <h1>ESE-OYE</h1>
</div>
<div>
  <section class="container">
    <form method="post" action="ese-oye">
      <c:if test="${error}">
        <div class="error">
          <p>Erreur d'authentification</p>
        </div>
      </c:if>
      <label class="textBox">
        <input type="text" name="name" placeholder="prénom" required>

      </label>
      <br/><br/>
      <label class="textBox">
        <input type="text" name="surname" placeholder="nom " required>
      </label>
      <br/><br/>
      <label class="textBox">
        <input type="tel" name="phone" placeholder="numéro de téléphone " required>
      </label>
      <br/><br/>
      <label class="textBox">
        <input type="email" name="mail" placeholder="mail" required>
      </label>
      <br/><br/>
      <label class="textBox">
        <input type="date" name="bday" placeholder="date de naissance " required max="08/03/2005" id="mybday" onclick="btchange()">

      </label>
      <br/><br/>
      <label class="textBox">
        <input type="password" name="password" placeholder="mot de passe" required >
      </label>
      <br/><br/>
      <input type="submit" class="btn fill" value="Valider" id="myButton">
    </form>
    <input type="submit" class="btn fill" value="déjà inscrit ?"onclick="window.Request.sendPost('ese-oye?id=Connexion')">
  </section>

</div>



</body>
</html>