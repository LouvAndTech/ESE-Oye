<%--
  Created by IntelliJ IDEA.
  User: matth
  Date: 18/03/2023
  Time: 17:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Error404</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Error404.css" />
    <script type="module" src="${pageContext.request.contextPath}/js/Request.js" defer></script>

</head>
    <body>


        <h1>Error 404 : impossible de trouver ce que vous cherchez</h1>
        <c:choose>
            <c:when test = "${sessionScope.username == null}">
                <input type="submit" class="btn fill" value="retourner au site"onclick="window.Request.sendGet('ese-oye?id=Inscription')">
            </c:when>
            <c:otherwise>
                <input type="submit" class="btn fill" value="retourner au site"onclick="window.Request.sendGet('ese-oye')">
            </c:otherwise>
        </c:choose>
        <img src="http://eseoye.elouan-lerissel.fr/404.jpg"" alt="404" class="img404">

    </body>
</html>
