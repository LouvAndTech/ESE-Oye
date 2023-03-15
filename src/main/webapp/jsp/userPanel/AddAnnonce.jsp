
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/Post.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/AddAnnonce.css">

<div class="container">
    <form action="ese-oye?id=UserPanel&contentPage=AddAnnonce" method="post" enctype='multipart/form-data'>
        <div class="centerSectionElement">
            <c:if test="${!empty error}">
                <p class="warning" style="display: block">${error}</p>
            </c:if>
            <div class="header">
                <input type="text" name="title" placeholder="Titre" value="" required>
                <input type="number" name="price" placeholder="Prix" value="" required>
            </div>
            <div class="carrousel">
                <div class="addImg">
                    <input type="file" multiple="multiple" id="image_drop" name="image_drop"/>
                    <div id="image_show" class="imgList">
                    </div>
                    <p class="warning" id="errorMessageimage">loading</p>
                    <script src="${pageContext.request.contextPath}/js/draganddrop.js" type="text/javascript"></script>
                </div>
            </div>
            <hr>
            <h2>Caractéristiques :</h2>
            <div class="carac">
                <div class="cat">
                    <p>Catégorie :</p>
                    <select name="categorie" id="">
                        <option value="">-- Select catégorie --</option>
                        <c:forEach var="c" items="${categories}">
                            <option value="${c}">${c}</option>
                        </c:forEach>
                    </select>
                </div>
                <hr class="hrVertical">
                <div class="state">
                    <p>Etat :</p>
                    <select name="categorie" id="">
                        <option value="">-- Select Etat --</option>
                        <c:forEach var="c" items="${states}">
                            <option value="${c}">${c}</option>
                        </c:forEach>
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