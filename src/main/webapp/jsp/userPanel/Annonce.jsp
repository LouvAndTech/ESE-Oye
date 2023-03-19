<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/7/2023
  Time: 8:22 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/Annonces.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ListPosts.css">

<script type="module" src="${pageContext.request.contextPath}/js/request.js" defer></script>

<section>
    <div class="list">
        <br>
        <h2>Toute vos annonces publiées.</h2>
        <div class="posts centerSectionElement">
            <c:forEach var="post" items="${posts}">
                <hr>
                <div class="post" onclick="window.Request.sendGet('ese-oye?id=OnePost&postId=${post.id}')">
                    <img src="${post.firstImage}" alt="">
                    <div class="content">
                        <div class="header">
                            <h2>${post.title}</h2>
                            <h2>${post.price}€</h2>
                        </div>
                        <div class="footer">
                            <div class="cat">
                                <p>Catégorie :</p>
                                <p>${post.category.name}</p>
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
            <button class="btn" onclick="window.Request.sendPost('ese-oye?id=UserPanel&contentPage=Annonce&postPage=${postPage-1}')"><span class="material-symbols-outlined">navigate_before</span></button>
            <c:forEach var="page" items="${nbPage}">
                <button <c:choose>
                    <c:when test="${postPage == page}">class="actual" style="cursor: default" </c:when>
                    <c:otherwise>onclick="window.Request.sendPost('ese-oye?id=UserPanel&contentPage=Annonce&postPage=${page}')"</c:otherwise>
                </c:choose>
                >${page}</button>
            </c:forEach>
            <button class="btn" onclick="window.Request.sendPost('ese-oye?id=UserPanel&contentPage=Annonce&postPage=${postPage+1}')"><span class="material-symbols-outlined">navigate_next</span></button>
        </div>
    </div>
</section>