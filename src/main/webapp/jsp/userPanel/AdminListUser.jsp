<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/20/2023
  Time: 4:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2>User List</h2>
<hr>
<div class="post ConfigUser">
    <div>
        <p>Ajouter un utilisateur</p>
    </div>
    <div class="end">
        <a href="ese-oye?id=UserPanel&contentPage=AdminAddUser"><span class="material-symbols-outlined">add_circle</span></a>
    </div>
</div>
<c:forEach var="user" items="${listUser}">
    <hr>
    <div class="post ConfigUser">
        <div>
            <p>${user.name} ${user.surname}</p>
        </div>
        <form class="end" action="ese-oye?id=UserPanel&contentPage=AdminListUser" method="post">
            <input type="hidden" name="secureID" value="${user.secureID}">
            <button type="submit" name="admin"><span class="material-symbols-outlined desactiver">admin_panel_settings</span></button>
            <button type="submit" name="lock"><span class="material-symbols-outlined desactiver">lock</span></button>
            <button type="submit" name="delete"><span class="material-symbols-outlined passSecu_low">delete</span></button>
        </form>
    </div>
</c:forEach>
