<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/14/2023
  Time: 10:34 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="fr">
<head>
    <%@include file="theme/Head.jsp" %>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Post.css">
    <title>Post</title>
</head>
<body>

<%@include file="theme/Header.jsp" %>

<section>
    <div class="centerSectionElement">
        <div class="header">
            <h2>Appareil à raclette</h2>
            <h2>123.99€</h2>
        </div>
        <div class="carrousel">
            <div class="backCarrousel">
                <div class="backgroundCarrousel"></div>
                <button class="closeBtn" onclick="closeCarrousel();"><span class="material-symbols-outlined">close</span></button>
                <button class="btnRight" onclick="turnRight();"><span class="material-symbols-outlined">arrow_forward_ios</span></button>
                <button class="btnLeft" onclick="turnLeft();"><span class="material-symbols-outlined">arrow_back_ios</span></button>
                <div class="shortBtn"></div>
            </div>
            <img src="${pageContext.request.contextPath}/img/blankImg.png" alt="">
            <img src="${pageContext.request.contextPath}/img/blankImg.png" alt="">
            <img src="${pageContext.request.contextPath}/img/blankImg.png" alt="">
            <img src="${pageContext.request.contextPath}/img/blankImg.png" alt="">
            <img src="${pageContext.request.contextPath}/img/blankImg.png" alt="">
        </div>
        <hr>
        <h2>Caractéristiques :</h2>
        <div class="carac">
            <div class="cat">
                <p>Catégorie :</p>
                <p>Cuisine</p>
            </div>
            <hr class="hrVertical">
            <div class="state">
                <p>Etat :</p>
                <p>A servie</p>
            </div>
            <div class="date">
                <p>Mise en ligne :</p>
                <p>01/03/2023</p>
            </div>
        </div>
        <hr>
        <h2>Description :</h2>
        <p>Un appareil à raclette est un petit appareil de cuisine conçu pour faire fondre du fromage et le servir avec des accompagnements tels que des pommes de terre, des cornichons, de la charcuterie, etc. Il se compose généralement d'une plaque chauffante qui permet de faire fondre le fromage et de le faire couler dans des poêlons individuels qui sont placés sous la plaque chauffante.</p>
        <br>
        <p>Les poêlons sont généralement disposés autour de la plaque chauffante et peuvent être retirés pour que les convives puissent servir le fromage fondu sur leurs aliments. La plaque chauffante est souvent dotée d'un contrôle de température pour régler la chaleur en fonction des préférences individuelles.</p>
        <br>
        <p>Les appareils à raclette peuvent être électriques ou fonctionner avec des bougies chauffe-plat. Ils peuvent varier en taille et en capacité, avec des modèles allant de deux à douze poêlons.</p>
        <hr>
        <div class="user">
            <img class="imgPP" src="${pageContext.request.contextPath}/img/blankPP.png" alt="">
            <div class="info">
                <p>Julien Guitter</p>
                <p>5 annonces</p>
            </div>
            <a href="" class="aBtn">Voir le profile</a>
        </div>
    </div>
</section>

<%@include file="theme/Footer.jsp" %>
<script src="../js/Carrousel_post.js"></script>
</body>
</html>
