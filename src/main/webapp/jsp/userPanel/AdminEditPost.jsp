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

<div class="container">
  <form action="ese-oye?id=UserPanel&contentPage=AdminEditPost" method="post" enctype='multipart/form-data'>
    <div class="centerSectionElement">
      <div class="header">
        <input type="text" name="title" placeholder="Titre" value="" required>
        <input type="number" name="price" placeholder="Prix" value="" required>
      </div>
      <div class="oneLineImg">
        <div class="addImg">
          <img src="http://eseoye.elouan-lerissel.fr/blankImg.png">
          <img src="http://eseoye.elouan-lerissel.fr/blankImg.png">
          <img src="http://eseoye.elouan-lerissel.fr/blankImg.png">
          <img src="http://eseoye.elouan-lerissel.fr/blankImg.png">
          <img src="http://eseoye.elouan-lerissel.fr/blankImg.png">
        </div>
      </div>
      <hr>
      <h2>Caractéristiques :</h2>
      <div class="carac">
        <div class="cat">
          <p>Catégorie :</p>
          <div class="custom_select">
            <select name="category" id="">
              <option value="">-- Select catégorie --</option>
              <c:forEach var="c" items="${categories}">
                <option value="${c.id}">${c.name}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <hr class="hrVertical">
        <div class="state">
          <p>Etat :</p>
          <div class="custom_select">
            <select name="state" id="">
              <option value="">-- Select Etat --</option>
              <c:forEach var="s" items="${states}">
                <option value="${s.id}">${s.name}</option>
              </c:forEach>
            </select>
          </div>
        </div>
      </div>
      <hr>
      <h2>Description :</h2>
      <div class="textAreaCont">
        <textarea name="description" maxlength="2000" id="" placeholder="Description"></textarea>
        <p class="nbChar">0/2000</p>
      </div>
      <div class="centerBtn">
        <input class="btn fill" type="submit" value="Edit And Publish">
      </div>
    </div>
  </form>
</div>
