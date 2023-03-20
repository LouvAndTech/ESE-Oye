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
  <%@include file="theme/Head.jsp" %>
  <script src="${pageContext.request.contextPath}/js/main.js"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/UserPanel.css">
  <title>Panel User</title>
</head>
<body>

<%@include file="theme/Header.jsp" %>

<div id="mainContent">
  <nav>
    <c:choose>
      <c:when test="${!adminState}">
        <h1>Panneau de control</h1>
        <hr>
        <a href="?id=UserPanel&contentPage=Account" ${contentPage == "Account" ? 'class="active"' : ''}><span class="material-symbols-outlined">person</span> Mon compte</a>
        <a href="?id=UserPanel&contentPage=Annonce" ${contentPage == "Annonce" ? 'class="active"' : ''}><span class="material-symbols-outlined">description</span> Mes annonces</a>
        <a href="?id=UserPanel&contentPage=AddAnnonce" ${contentPage == "AddAnnonce" ? 'class="active"' : ''}><span class="material-symbols-outlined">note_add</span> Ajouter une annonce</a>
        <a href="?id=UserPanel&contentPage=Message" ${contentPage == "Message" ? 'class="active"' : ''}><span class="material-symbols-outlined">chat</span> Mes messages</a>
        <a href="?id=UserPanel&contentPage=Favoris" ${contentPage == "Favoris" ? 'class="active"' : ''}><span class="material-symbols-outlined">favorite</span> Mes favoris</a>
      </c:when>
      <c:otherwise>
        <h1>Panneau Admin</h1>
        <hr>
        <a href=""><span class="material-symbols-outlined">manage_accounts</span> Gestion des utilisateurs</a>
        <a href=""><span class="material-symbols-outlined">description</span> Gestion des annonces</a>
        <a href=""><span class="material-symbols-outlined">forum</span> Gestion des messages</a>
        <a href=""><span class="material-symbols-outlined">monitoring</span> Statistique</a>
      </c:otherwise>
    </c:choose>
    <hr>
    <a href="" class="deco_btn"><span class="material-symbols-outlined">logout</span> Deconnexion</a>


  </nav>
  <hr class="hrVertical">
  <section>
    <c:choose>
      <c:when test="${!adminState}">
        <jsp:include page="userPanel/${contentPage}.jsp">
          <jsp:param name="year" value="2010"/>
        </jsp:include>
      </c:when>
      <c:otherwise>
        <jsp:include page="adminPanel/${contentPage}.jsp">
          <jsp:param name="year" value="2010"/>
        </jsp:include>
      </c:otherwise>
    </c:choose>
  </section>
</div>

<%@include file="theme/Footer.jsp" %>

<script src="${pageContext.request.contextPath}/js/inputs.js"></script>
</body>
</html>