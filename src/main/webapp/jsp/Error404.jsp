<%--
  Created by IntelliJ IDEA.
  User: matth
  Date: 18/03/2023
  Time: 17:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error404</title>
</head>
<body>

<c:choose>
    <c:when test="${a boolean expr}">
        <input type="submit" class="btn fill" value="retourner au site"onclick="window.Request.sendPost('ese-oye?id=Inscription')">
    </c:when>
    <c:otherwise>
        <input type="submit" class="btn fill" value="retourner au site"onclick="window.Request.sendPost('ese-oye?')">
    </c:otherwise>
</c:choose>








</body>
</html>
