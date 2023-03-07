<%--
  Created by IntelliJ IDEA.
  User: elouanlerissel
  Date: 10/02/2023
  Time: 14:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>ESE-Oye</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ListPosts.css"/>
    <script type="module" src="${pageContext.request.contextPath}/js/request.js"></script>
</head>
    <body>
        <div class="posts" >
            <c:forEach var="post" items="${posts}">
                    <div class="post" onclick="window.Request.sendGet('ese-oye?id=OnePost&postId=${post.id}')" >
                        <div class="header">
                            <h2>${post.title}</h2>
                            <h3>${post.price} €</h3>
                        </div>
                        <div class="subtitle">
                            <p class="author">${post.author}</p>
                            <p class="date">${post.date.day} / ${post.date.month} / ${post.date.year}</p>
                        </div>
                    </div>
            </c:forEach>
        </div>
        <div class="buttonContainer">
            <button onclick="window.Request.sendPost('ese-oye?id=ListPosts&postPage=${postPage-1}')">previous</button>
            <button onclick="window.Request.sendPost('ese-oye?id=ListPosts&postPage=${postPage+1}')">next</button>
        </div>
    </body>
</html>

<!--onclick="location.href='ese-oye?id=ListPosts&postId=${post.id}'"-->