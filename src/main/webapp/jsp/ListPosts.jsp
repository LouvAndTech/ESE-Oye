<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/14/2023
  Time: 10:34 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="fr">
<head>
    <%@include file="theme/Head.jsp" %>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Annonces.css">
    <title>List Posts</title>
</head>
<body>
<%@include file="theme/Header.jsp" %>

<section>
    <div class="tri">
        <h2>Annonce << Toutes >> : Angers</h2>
        <select name="tri" id="">
            <option value="plusRecent">Tri: Plus récent</option>
            <option value="moinRecent">Tri: Moins récent</option>
            <option value="plusCher">Tri: Plus chère</option>
            <option value="moinsCher">Tri: Moins chère</option>
        </select>

    </div>
    <div class="posts centerSectionElement">
        <hr>
        <div class="post" onclick="">
            <img src="${pageContext.request.contextPath}/img/blankImg.png" alt="">
            <div class="content">
                <div class="header">
                    <h2>Hummour</h2>
                    <h2>1.00€</h2>
                </div>
                <div class="footer">
                    <div class="cat">
                        <p>Catégorie :</p>
                        <p>Forme d'esprit</p>
                    </div>
                    <div class="subtitle">
                        <p class="author">Elouan</p>
                        <p class="date">10/03/2023</p>
                    </div>
                </div>
            </div>
        </div>
        <hr>
        <div class="post" onclick="">
            <img src="${pageContext.request.contextPath}/img/blankImg.png" alt="">
            <div class="content">
                <div class="header">
                    <h2>Tapis</h2>
                    <h2>256.00€</h2>
                </div>
                <div class="footer">
                    <div class="cat">
                        <p>Catégorie :</p>
                        <p>Ameublement</p>
                    </div>
                    <div class="subtitle">
                        <p class="author">Bernard</p>
                        <p class="date">01/02/2022</p>
                    </div>
                </div>
            </div>
        </div>
        <hr>
        <div class="post" onclick="">
            <img src="${pageContext.request.contextPath}/img/blankImg.png" alt="">
            <div class="content">
                <div class="header">
                    <h2>Foular</h2>
                    <h2>1672.00€</h2>
                </div>
                <div class="footer">
                    <div class="cat">
                        <p>Catégorie :</p>
                        <p>Vetement</p>
                    </div>
                    <div class="subtitle">
                        <p class="author">Adolphe</p>
                        <p class="date">15/06/1940</p>
                    </div>
                </div>
            </div>
        </div>
        <hr>
    </div>

    <div class="buttonContainer">
        <button class="btn"><span class="material-symbols-outlined">navigate_before</span></button>
        <button class="actual">1</button>
        <button>2</button>
        <button>3</button>
        <button>4</button>
        <button>5</button>
        <button>6</button>
        <button class="btn"><span class="material-symbols-outlined">navigate_next</span></button>
    </div>
</section>

<%@include file="theme/Footer.jsp" %>

</body>
</html>
