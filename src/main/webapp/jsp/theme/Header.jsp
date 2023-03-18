<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/14/2023
  Time: 10:37 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header>
  <div class="container">
    <h1>ESE OYE</h1>
    <div class="searchBar">
      <form action="" method="post">
        <input name="search" type="text" placeholder="Rechercher ...">
        <button type="submit"><span class="material-symbols-outlined">search</span></button>
      </form>
    </div>
    <div class="rightBtn">
      <a href="?id=UserPanel&contentPage=AddAnnonce" class="aBtn">Ajouter une annonce</a>
      <a href="?id=UserPanel&contentPage=Account">Profil</a>
      <div class="darkMode">
        <button onclick="dayDark();"><span class="material-symbols-outlined icon_dark_mode">dark_mode</span></button>
      </div>
    </div>
  </div>
  <hr>
</header>
