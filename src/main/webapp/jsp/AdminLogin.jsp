<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/20/2023
  Time: 2:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="theme/Head.jsp" %>
    <title>connexion</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Login.css"/>
</head>

<section>
    <div class="container">
        <form method="post" action="ese-oye?id=AdminLogin">
            <h1>Admin Connection</h1>
            <input type="text" name="pseudo" placeholder="Name.Surname" required>
            <br/><br/>
            <input type="password" name="password" placeholder="Mot de passe" required>
            <br/><br/>
            <input class="btn fill" type="submit" value="Se connecter">
        </form>
    </div>
</section>


</body>
</html>
