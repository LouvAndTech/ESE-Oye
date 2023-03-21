<%--
  Created by IntelliJ IDEA.
  User: elouanlerissel
  Date: 10/02/2023
  Time: 14:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="fr">
<head>
    <%@include file="theme/Head.jsp" %>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script type="module" src="${pageContext.request.contextPath}/js/request.js" defer></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Annonces.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ListPosts.css">
    <script src="${pageContext.request.contextPath}/js/inputs.js" defer></script>
    <title>List Posts</title>
</head>
<body>
<%@include file="theme/Header.jsp" %>

<section>
    <nav>
        <form action="ese-oye?id=ListPosts" method="post">
            <h1>Filtres :</h1>
            <hr>
            <p>Order :</p>
            <div class="custom_select">
                <select class="allElement" name="tri" id="">
                    <c:forEach var="ord" items="${orders}">
                        <option value="${ord.value}">${ord.displayName}</option>
                    </c:forEach>
                </select>
            </div>
            <hr>
            <p>Catégorie :</p>
            <div class="custom_select">
                <select class="allElement" name="cat" id="cat">
                    <option value="-1">Aucune</option>
                    <c:forEach items="${categories}" var="cat">
                        <option value="${cat.id}">${cat.name}</option>
                    </c:forEach>
                </select>
            </div>
            <hr>
            <p>État :</p>
            <div class="custom_select">
                <select class="allElement" name="state" id="state">
                    <option value="-1">Aucun</option>
                    <c:forEach items="${states}" var="state">
                        <option value="${state.id}">${state.name}</option>
                    </c:forEach>
                </select>
            </div>
            <hr>
            <p>Prix Max :</p>
            <input type="number" name="price" placeholder="Prix max" value="">
            <hr>
            <input type="hidden" name="postPage" value="${postPage}">
            <button class="btn" type="submit">Appliquer</button>
        </form>
    </nav>
    <hr class="hrVertical">
    <div class="list">
        <br>
        <h2>Totalité des annonces :</h2>
        <div class="posts centerSectionElement">
            <c:forEach var="post" items="${posts}">
                <hr>
                <div class="post" onclick="window.Request.sendGet('ese-oye?id=OnePost&postId=${post.secureId}')">
                    <img src="${post.firstImage}" alt="">
                    <div class="content">
                        <div class="header">
                            <h2>${post.title}</h2>
                            <h2>${post.price}€</h2>
                        </div>
                        <div class="footer">
                            <div class="detail">
                                <p>Catégorie :</p>
                                <p>${post.category.name}</p>
                            </div>
                            <div class="detail">
                                <p>État :</p>
                                <p>${post.state.name}</p>
                            </div>
                            <div class="subtitle">
                                <p class="author">${post.author.name} ${post.author.surname}</p>
                                <p class="date">${post.date}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="buttonContainer">
            <button class="btn" onclick="window.Request.sendPost('ese-oye?id=ListPosts&postPage=${postPage-1}&cat=${cat}&state=${state}&price=${price}&order=${order}&newPage=1')"><span class="material-symbols-outlined">navigate_before</span></button>
            <c:forEach var="page" items="${nbPage}">
                <button <c:choose>
                    <c:when test="${postPage+1 == page}">class="actual" style="cursor: default" </c:when>
                    <c:otherwise>onclick="window.Request.sendPost('ese-oye?id=ListPosts&postPage=${page-1}&cat=${cat}&state=${state}&price=${price}&order=${order}&newPage=1')"</c:otherwise>
                </c:choose>
                >${page}</button>
            </c:forEach>
            <button class="btn" onclick="window.Request.sendPost('ese-oye?id=ListPosts&postPage=${postPage+1}&cat=${cat}&state=${state}&price=${price}&order=${order}&newPage=1')"><span class="material-symbols-outlined">navigate_next</span></button>
        </div>
    </div>
</section>

<%@include file="theme/Footer.jsp" %>

</body>
</html>