<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/7/2023
  Time: 8:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/AddImage.css">

<div class="container">
    <h1>Information Personnel</h1>
    <form action="" method="post">
        <div>
            <label class="textBox">
                <input type="text" name="name" placeholder="Prénom" value="${user.name}" required>
                <input type="text" name="surname" placeholder="Nom" value="${user.surname}" required>
            </label>
        </div>
        <input type="submit" name="nameSurnameBtn" class="btn fill" value="Enregistrer" disabled>
    </form>
    <hr>
    <h1>Image de Profil</h1>
    <form>
        <div class="addImg" style="max-height: 23em">
            <label class="textBox">
                <input type="file" id="image_drop" name="image_drop"/>
            </label>
            <div id="image_show" class="imgList">
            </div>
            <p class="warning" id="errorMessageimage">loading</p>
            <script src="${pageContext.request.contextPath}/js/draganddrop.js" type="text/javascript"></script>
            <input id="btnImgPP" type="submit" class="btn fill" name="imageBtn" value="Enregistrer" disabled>
        </div>
    </form>
    <hr>
    <h1>Email</h1>
    <form action="" method="post">
        <div>
            <label class="textBox">
                <input type="email" name="mail" placeholder="Email" value="${user.mail}" required>
            </label>
        </div>
        <input type="submit" class="btn fill" name="mailBtn" value="Enregistrer" disabled>
    </form>
    <hr>
    <h1>Numéro de téléphone</h1>
    <form action="" method="post">
        <div>
            <label class="textBox">
                <input type="tel" name="phone" maxlength="14" placeholder="Téléphone" value="${user.phone}" required>
            </label>
        </div>
        <input type="submit" class="btn fill" name="phoneBtn" value="Enregistrer" disabled>
    </form>
    <hr>
    <h1>Date de naissance</h1>
    <form action="" method="post">
        <div>
            <label class="textBox">
                <input type="date" name="birth" placeholder="" value="${user.birth}" required>
            </label>
        </div>
        <input type="submit" class="btn fill" name="birthBtn" value="Enregistrer" disabled>
    </form>
    <hr>
    <h1>Nouveau mot de passe</h1>
    <form action="" method="post">
        <div>
            <label class="textBox">
                <input type="password" name="password" placeholder="Mot de passe" value="" required>
                <p class="passSecu">Votre mot de passe est trop faible</p>
            </label>
        </div>
        <input type="submit" class="btn fill" name="passwordBtn" value="Enregistrer" disabled>
    </form>
    <hr>
    <h1>Supression</h1>
    <form action="" method="post">
        <input type="submit" class="btn fill" name="deleteBtn" value="Suprimer le compte">
    </form>
</div>
