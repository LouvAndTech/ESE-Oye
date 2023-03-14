
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
                    <select name="categorie" id="">
                        <option value="">-- Select catégorie --</option>
                        <option value="1">Ameublement</option>
                        <option value="2">Voiture</option>
                        <option value="3">Maison</option>
                    </select>
                </div>
                <hr class="hrVertical">
                <div class="state">
                    <p>Etat :</p>
                    <select name="categorie" id="">
                        <option value="">-- Select Etat --</option>
                        <option value="1">Neuf</option>
                        <option value="2">Ouvert mais pas utiliser</option>
                        <option value="3">Vieux</option>
                    </select>
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