<%--
  Created by IntelliJ IDEA.
  User: matth
  Date: 08/02/2023
  Time: 16:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>inscription</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Inscription.css" />
</head>
<body>

<div class="title">
  <h1>ESE-OYE</h1>
</div>
<section class="container">
    <form method="post" action="ese-oye">
      <label class="textBox">
        <input type="text" name="name" placeholder=" " required>
        <span class="textBox_placeholder">prénom</span>
      </label>
      <br/>
      <label class="textBox">
        <input type="text" name="surname" placeholder=" " required>
        <span class="textBox_placeholder">nom</span>
      </label>
      <br/>
      <label class="textBox">
        <input type="tel" name="phone" placeholder=" " required>
        <span class="textBox_placeholder">numéro de téléphone</span>
      </label>
      <br/>
      <label class="textBox">
        <input type="email" name="mail" placeholder=" " required>
        <span class="textBox_placeholder">mail</span>
        <br/>
        <input name="error" placeholder=" ">
      </label>
      <br/>

      <label class="textBox">
        <input type="date" name="bday" placeholder=" " required max="08/03/2005" id="mybday" onclick="btchange()">
        <span class="textBox_placeholder">date de naissance</span>
      </label>
      <br/>
      <label class="textBox">
        <input type="password" name="password" placeholder=" " required >
        <span class="textBox_placeholder">mot de passe</span>
      </label>
      <br/>
      <input type="submit" class="btn fill" value="Valider" id="myButton">
    </form>
    <a href="webapp/jsp/Connexion.jsp" class="btn fill">déjà inscrit ?</a>
</section>
<script src="${pageContext.request.contextPath}/js/Check_Button.js"></script>

</body>
</html>
