<%--
  Created by IntelliJ IDEA.
  User: elouanlerissel
  Date: 08/03/2023
  Time: 15:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/AddAnnonce.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
    <div class="container">
        <form action="ese-oye?id=AddAnnonce" method="post" enctype='multipart/form-data' class="form">
            <h1>Édition d'annonce</h1>
            <div class="row horz">
                <label class="textBox">
                    <input type="text" name="title" placeholder="" value="" required>
                    <span class="textBox_placeholder">Titre</span>
                </label>
                <label class="textBox">
                    <input type="text" name="price" placeholder="" value="" required>
                    <span class="textBox_placeholder">Prix</span>
                </label>
            </div>
            <div class="row">
                <label class="textBox">
                    <textarea name="description" id="" cols="52" rows="10" placeholder=" " maxlength="510" required></textarea>
                    <span class="textBox_placeholder">Description</span>
                </label>
            </div>
            <div class="row horz">
                <label class="textBox">
                    <input type="text" name="state" placeholder="" value="" required>
                    <span class="textBox_placeholder">État</span>
                </label>
                <select name="cat" id="cat">
                    <option value="">--Please choose an option--</option>
                    <c:forEach var="c" items="${categories}">
                        <option value="${c}">${c}</option>
                    </c:forEach>
                </select>

            </div>
            <div>
                <p>Déposez vos images</p>
                <input type="file" multiple="multiple" id="image_drop" name="image_drop"/>
                <div id="image_show">
                </div>
                <p id="errorMessageimage" style="display: none">loading</p>
                <script src="${pageContext.request.contextPath}/js/draganddrop.js" type="text/javascript"></script>
            </div>
            <input type="submit" class="btn fill" value="Enregistrer">
        </form>
    </div>
</body>
</html>
