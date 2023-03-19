<%--
  Created by IntelliJ IDEA.
  User: julie
  Date: 3/7/2023
  Time: 8:22 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/Post.css">

<div class="container">
    <form action="">
        <div class="centerSectionElement">
            <div class="header">
                <input type="text" name="title" placeholder="Titre" value="" required>
                <input type="number" name="price" placeholder="Prix" value="" required>
            </div>
            <div class="carrousel">
                <div class="addImg">
                    <input type="file" name="" id="">
                </div>
            </div>
            <hr>
            <h2>Caractéristiques :</h2>
            <div class="carac">
                <div class="cat">
                    <p>Catégorie :</p>
                    <div class="custom_select">
                        <select name="" id="">
                            <option value="0">-- Select Option --</option>
                            <option value="1">Option 1</option>
                            <option value="2">Option 2</option>
                            <option value="3">Option 3</option>
                            <option value="4">Option 4</option>
                            <option value="5">Option 5</option>
                            <option value="6">Option 6</option>
                        </select>
                    </div>
                </div>
                <hr class="hrVertical">
                <div class="state">
                    <p>Etat :</p>
                    <div class="custom_select">
                        <select name="" id="">
                            <option value="0">-- Select Option --</option>
                            <option value="1">Option 1</option>
                            <option value="2">Option 2</option>
                            <option value="3">Option 3</option>
                            <option value="4">Option 4</option>
                            <option value="5">Option 5</option>
                            <option value="6">Option 6</option>
                        </select>
                    </div>
                </div>
            </div>
            <hr>
            <h2>Description :</h2>
            <div class="textAreaCont">
                <textarea name="description" maxlength="2000" id="" placeholder="Description"></textarea>
                <p class="nbChar">0/2000</p>
            </div>
            <div class="centerBtn">
                <input class="btn fill" type="submit" value="Enregistrer">
            </div>
        </div>
    </form>
</div>