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
    <title>List Posts</title>
</head>
<body>
<%@include file="theme/Header.jsp" %>

<section>
    <div class="tri">
        <h2>Annonce << Toutes >> : Angers</h2>
        <!---select name="tri" id="">
            <option value="plusRecent">Tri: Plus récent</option>
            <option value="moinRecent">Tri: Moins récent</option>
            <option value="plusCher">Tri: Plus chère</option>
            <option value="moinsCher">Tri: Moins chère</option>
        </select-->

    </div>
    <div class="posts centerSectionElement">
        <c:forEach var="post" items="${posts}">
            <hr>
            <div class="post" onclick="window.Request.sendGet('ese-oye?id=OnePost&postId=${post.id}')">
                <img src="${pageContext.request.contextPath}/img/blankImg.png" alt="">
                <div class="content">
                    <div class="header">
                        <h2>${post.title}</h2>
                        <h2>${post.price}</h2>
                    </div>
                    <div class="footer">
                        <div class="cat">
                            <p>Catégorie :</p>
                            <p>[TODO]</p>
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
        <button class="btn" onclick="window.Request.sendPost('ese-oye?id=ListPosts&postPage=${postPage-1}')"><span class="material-symbols-outlined">navigate_before</span></button>
        <c:forEach var="page" items="${nbPage}">
            <button <c:if test="${postPage == page}">class="actual"</c:if>>${page}</button>
        </c:forEach>
        <button class="btn" onclick="window.Request.sendPost('ese-oye?id=ListPosts&postPage=${postPage+1}')"><span class="material-symbols-outlined">navigate_next</span></button>
    </div>
</section>

<%@include file="theme/Footer.jsp" %>

</body>
</html>

<!--onclick="location.href='ese-oye?id=ListPosts&postId=${post.id}'"-->