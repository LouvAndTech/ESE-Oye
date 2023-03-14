const listImg = document.querySelectorAll(".carrousel img");
var btnShort;
var idImage = 0;
var imgWidth = listImg[0].offsetWidth;
var imgHeight = listImg[0].offsetHeight;
var startPoint = 0;
var isOpen = false;

//set random image for the test

listImg.forEach(img => {
    img.src = "https://picsum.photos/640/480?random="+Math.floor(Math.random() * 100);
});

//fin code pour le test

for(var i=0; i<listImg.length; i++){
    document.querySelector(".shortBtn").innerHTML += '<button class="btnShort" onclick="selectSpecImg('+i+');"><span class="material-symbols-outlined">radio_button_unchecked</span></button>';
    listImg[i].addEventListener("click", function(){
        openCarrousel();
    });
    btnShort = document.querySelectorAll(".btnShort span");
    btnShort[0].innerHTML = "radio_button_checked";
}

window.addEventListener("resize",e => {
    startPoint = startPoint = (window.innerWidth-imgWidth)/2;
    if(isOpen){
        moveImg();
    }
});

function openCarrousel(){
    disableScrolling();
    isOpen = true;
    listImg[0].parentElement.style.height = listImg[0].parentElement.offsetHeight;
    listImg[0].parentElement.classList.add("opened");
    startPoint = (window.innerWidth-imgWidth)/2;
    moveImg();
}

function moveImg(){
    for(var i=0; i<listImg.length; i++){
        if(idImage == i){
            listImg[i].style.height = imgHeight;
            listImg[i].style.opacity = 1;
        }else{
            listImg[i].style.height = imgHeight/2
            listImg[i].style.opacity = 0;
        }
        //listImg[i].style.opacity = idImage
        listImg[i].style.left = startPoint+(i-idImage)*imgWidth;
        btnShort[i].innerHTML = "radio_button_unchecked";
        //listImg[i].style.height = idImage == i ? imgHeight : imgHeight/2;
    }
    btnShort[idImage].innerHTML = "radio_button_checked";
}

function turnLeft(){
    idImage -= idImage ? 1 : 0;
    moveImg(0);
}

function turnRight(){
    idImage += idImage < listImg.length-1 ? 1 : 0;
    moveImg(0);
}

function closeCarrousel(){
    enableScrolling();
    isOpen = false;
    listImg[0].parentElement.classList.remove("opened");
    listImg[0].style.height = imgHeight;
    listImg[1].style.height = "14.5em";
    listImg[2].style.height = "14.5em";
    listImg.forEach(e =>{
        e.style.opacity = 1;
    })
}

function selectSpecImg(i){
    idImage = i;
    moveImg();
}

function disableScrolling(){
    var x=window.scrollX;
    var y=window.scrollY;
    window.onscroll=function(){window.scrollTo(x, y);};
}

function enableScrolling(){
    window.onscroll=function(){};
}