<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/20/2023
  Time: 7:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/Login.css">
<h2>Ajouter un nouvelle utilisateur</h2>
<br/>
<form method="post" action="ese-oye?id=UserPanel&contentPage=AdminAddUser">
  <label class="textBox">
    <input type="text" name="name" placeholder="prénom" required>
  </label>
  <br/><br/>
  <label class="textBox">
    <input type="text" name="surname" placeholder="nom " required>
  </label>
  <br/><br/>
  <label class="textBox">
    <input type="tel" name="phone" maxlength="14" placeholder="numéro de téléphone " required>
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
    <p class="passSecu">Votre mot de passe est trop faible</p>
  </label>
  <br/><br/>
  <input type="submit" class="btn fill" value="Valider" id="myButton">
</form>
