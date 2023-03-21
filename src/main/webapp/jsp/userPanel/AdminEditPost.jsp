<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/21/2023
  Time: 1:21 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<link rel="stylesheet" href="${pageContext.request.contextPath}/css/Post.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/AddAnnonce.css">

<section>
  <div class="container">
    <form action="ese-oye?id=UserPanel&contentPage=AdminEditPost" method="post">
      <div class="centerSectionElement">
        <c:if test="${!empty error}">
          <p class="warning" style="display: block">${error}</p>
        </c:if>
        <div class="header">
          <input type="text" name="title" placeholder="Titre" value="${post.title}" required>
          <h2>${post.price}€</h2>
        </div>
        <div class="oneLineImg">
          <div class="addImg">
            <c:forEach var="image" items="${post.imageList}">
              <img src="${image}">
            </c:forEach>
          </div>
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
        <div class="textAreaCont">
          <textarea name="description" maxlength="2000" id="" placeholder="Description" >${post.content}</textarea>
          <p class="nbChar">0/2000</p>
        </div>
        <div class="centerBtn">
          <input type="hidden" name="postId" value="${post.secureId}">
          <input class="btn fill" type="submit" value="Edit And Publish">
        </div>
      </div>
    </form>
  </div>
</section>
