<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/7/2023
  Time: 8:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container">
    <h1>Information Personnel</h1>
    <form action="" method="post">
        <div>
            <label class="textBox">
                <input type="text" name="name" placeholder="" value="Jean" required>
                <span class="textBox_placeholder">Nom</span>
            </label>
            <label class="textBox">
                <input type="text" name="surname" placeholder="" value="Eude" required>
                <span class="textBox_placeholder">Prenom</span>
            </label>
        </div>
        <input type="submit" class="btn fill" value="Enregistrer" disabled>
    </form>
    <hr>
    <h1>Email</h1>
    <form action="" method="post">
        <div>
            <label class="textBox">
                <input type="email" name="mail" placeholder="" value="" required>
                <span class="textBox_placeholder">Email</span>
            </label>
        </div>
        <input type="submit" class="btn fill" value="Enregistrer" disabled>
    </form>
    <hr>
    <h1>Numéro de téléphone</h1>
    <form action="" method="post">
        <div>
            <label class="textBox">
                <input type="tel" name="phone" maxlength="14" placeholder="" value="1" required>
                <span class="textBox_placeholder">## ## ## ## ##</span>
            </label>
        </div>
        <input type="submit" class="btn fill" value="Enregistrer" disabled>
    </form>
    <hr>
    <h1>Date de naissance</h1>
    <form action="" method="post">
        <div>
            <label class="textBox">
                <input type="date" name="birth" placeholder="" value="" required>
                <span class="textBox_placeholder">Date de naissance</span>
            </label>
        </div>
        <input type="submit" class="btn fill" value="Enregistrer" disabled>
    </form>
    <hr>
    <h1>Mot de passe</h1>
    <form action="" method="post">
        <div>
            <label class="textBox">
                <input type="password" name="password" placeholder="" value="" required>
                <span class="textBox_placeholder">Password</span>
            </label>
            <p class="passSecu">Votre mot de passe est trop faible</p>
        </div>
        <input type="submit" class="btn fill" value="Enregistrer" disabled>
    </form>
</div>
