<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/14/2023
  Time: 10:37 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="module" src="${pageContext.request.contextPath}/js/request.js" defer></script>
<header>
    <div class="container">
        <h1 onclick="window.Request.sendGet('ese-oye?id=ListPosts')" style="cursor: pointer">ESE OYE</h1>
        <div class="rightBtn">
            <a href="?id=UserPanel&contentPage=AddAnnonce" class="aBtn">Ajouter une annonce</a>
            <c:choose>
                <c:when test="${!adminState}">
                    <a href="?id=UserPanel&contentPage=Account">Profil</a>
                </c:when>
                <c:otherwise>
                    <a href="?id=UserPanel&contentPage=AdminListUser">Profil</a>
                </c:otherwise>
            </c:choose>
            <div class="darkMode">
                <button onclick="dayDark();"><span class="material-symbols-outlined icon_dark_mode">dark_mode</span>
                </button>
            </div>
        </div>
    </div>
    <hr>
</header>
