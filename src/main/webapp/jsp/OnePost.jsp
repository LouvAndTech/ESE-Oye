<%--
  Created by IntelliJ IDEA.
  User: elouanlerissel
  Date: 11/02/2023
  Time: 18:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="fr">
<head>
    <%@include file="theme/Head.jsp" %>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Post.css">
    <script type="module" src="${pageContext.request.contextPath}/js/request.js" defer></script>
    <title>Post</title>
</head>
<body>

<%@include file="theme/Header.jsp" %>

<section>
    <div class="centerSectionElement">
        <c:choose>
            <c:when test="${adminState}">
                <div class="alignItemsRight">
                    <a onclick="window.Request.sendPost('ese-oye?id=UserPanel&contentPage=AdminValidePost&postId=${postId}&action=delete')"
                       class="passSecu_low"><span class="material-symbols-outlined">delete</span></a>
                </div>
            </c:when>
        </c:choose>
        <div class="header">
            <h2>${post.title}</h2>
            <h2>${post.price}€</h2>
        </div>
        <div class="carrousel">
            <div class="backCarrousel">
                <div class="backgroundCarrousel"></div>
                <button class="closeBtn" onclick="closeCarrousel();"><span
                        class="material-symbols-outlined">close</span></button>
                <button class="btnRight" onclick="turnRight();"><span class="material-symbols-outlined">arrow_forward_ios</span>
                </button>
                <button class="btnLeft" onclick="turnLeft();"><span
                        class="material-symbols-outlined">arrow_back_ios</span></button>
                <div class="shortBtn"></div>
            </div>
            <c:forEach var="image" items="${post.imageList}">
                <img src="${image}" alt="">
            </c:forEach>
        </div>
        <hr>
        <h2>Caractéristiques :</h2>
        <div class="carac">
            <div class="cat">
                <p>Catégorie :</p>
                <p>${post.category.name}</p>
            </div>
            <hr class="hrVertical">
            <div class="state">
                <p>Etat :</p>
                <p>${post.state.name}</p>
            </div>
            <div class="date">
                <p>Mise en ligne :</p>
                <p>${post.date}</p>
            </div>
        </div>
        <hr>
        <h2>Description :</h2>
        <p>${post.content}</p>

        <hr>
        <div class="user">
            <img class="imgPP" src="http://eseoye.elouan-lerissel.fr/blankPP.png" alt="">
            <div class="info">
                <p>${post.author.name} ${post.author.surname}</p>
                <p>${post.author.mail}</p>
                <p>${post.author.phone}</p>
            </div>
            <a href="ese-oye?id=UserProfile&targetUser=${post.author.secureID}" class="aBtn">Voir le profil</a>
        </div>
    </div>
</section>

<%@include file="theme/Footer.jsp" %>
<script src="${pageContext.request.contextPath}/js/Carrousel_post.js"></script>
</body>
</html>
