<%--
  Created by IntelliJ IDEA.
  User: elouanlerissel
  Date: 11/02/2023
  Time: 18:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ESE-Oye</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/OnePost.css"/>
</head>
    <body>
        <div class="container">
            <div class="header">
                <div class="title">
                    <h1>${post.title}</h1>
                    <h2>${post.price} €</h2>
                </div>
                <div class="subtitle">
                    <p>${post.author}</p>
                    <p>${post.date}</p>
                </div>
            </div>
            <p class="content">${post.content}</p>
        </div>
    </body>
</html>
