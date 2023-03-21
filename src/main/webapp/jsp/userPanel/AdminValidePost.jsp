<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/20/2023
  Time: 4:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/Post.css">
<c:choose>
    <c:when test="${postID == 0}">
        <h2>Aucune annonce a valider</h2>
    </c:when>
    <c:otherwise>
        <div class="btnAdminControl">
            <a href="ese-oye?id=UserPanel&contentPage=AdminValidePost&idPost=${postID}&action=delete"><span class="material-symbols-outlined passSecu_low">cancel</span></a>
            <a href="ese-oye?id=UserPanel&contentPage=AdminEditPost&idPost=${postID}"><span class="material-symbols-outlined">edit</span></a>
            <a href="ese-oye?id=UserPanel&contentPage=AdminValidePost&idPost=${postID}&action=valid"><span class="material-symbols-outlined passSecu_high">check_circle</span></a>
            <p>Validation d'annonce</p>
        </div>
        <div class="header">
            <h2>${post.title}</h2>
            <h2>${post.price}€</h2>
        </div>
        <div class="imgInLine">
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
                <p>A servie [TODO]</p>
            </div>
            <div class="date">
                <p>Mise en ligne :</p>
                <p>${post.date}</p>
            </div>
        </div>
        <hr>
        <h2>Description :</h2>
        <p>${post.content}</p>
        </div>
    </c:otherwise>
</c:choose>
