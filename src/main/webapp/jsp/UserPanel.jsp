<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 2/21/2023
  Time: 7:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Panel de control</title>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/userPanel.css">
</head>
<body>
<div id="mainContent">
  <nav>

    <h1>Panneau de control</h1>
    <hr>
    <a href="?id=UserPanel&contentPage=Account" ${contentPage == "Account" ? 'class="active"' : ''}><span class="material-symbols-outlined">person</span> Mon compte</a>
    <a href="?id=UserPanel&contentPage=Annonce" ${contentPage == "Annonce" ? 'class="active"' : ''}><span class="material-symbols-outlined">description</span> Mes annonces</a>
    <a href="?id=UserPanel&contentPage=AddAnnonce" ${contentPage == "AddAnnonce" ? 'class="active"' : ''}><span class="material-symbols-outlined">note_add</span> Ajouter une annonce</a>
    <a href="?id=UserPanel&contentPage=Message" ${contentPage == "Message" ? 'class="active"' : ''}><span class="material-symbols-outlined">chat</span> Mes messages</a>
    <a href="?id=UserPanel&contentPage=Favoris" ${contentPage == "Favoris" ? 'class="active"' : ''}><span class="material-symbols-outlined">favorite</span> Mes favoris</a>
    <hr>
    <a href="" class="deco_btn"><span class="material-symbols-outlined">logout</span> Deconnexion</a>

    <!--
                    <h1>Panneau Admin</h1>
                    <hr>
                    <a href="" class="active"><span class="material-symbols-outlined">manage_accounts</span> Gestion des utilisateurs</a>
                    <a href=""><span class="material-symbols-outlined">description</span> Gestion des annonces</a>
                    <a href=""><span class="material-symbols-outlined">forum</span> Gestion des messages</a>
                    <a href=""><span class="material-symbols-outlined">monitoring</span> Statistique</a>
                    <hr>
                    <a href="" class="deco_btn"><span class="material-symbols-outlined">logout</span> Deconnexion</a>
-->
  </nav>
  <section>
    <jsp:include page="userPanel/${contentPage}.jsp">
      <jsp:param name="year" value="2010"/>
    </jsp:include>
  </section>
</div>
</body>
<script src="${pageContext.request.contextPath}/js/input.js"></script>
</html>
