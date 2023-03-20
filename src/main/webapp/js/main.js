var dark = true;
const root = document.querySelector(":root");
var loaded = false;


var listColor = {"--maincolor":"blue", "--backcolor":"#fff", "--textcolor":"#000", "--border":"#444", "--iluminated":"#2661bf", "--lightback":"#eee"}


if(document.cookie.split("; ").find((row) => row.startsWith("darkTheme"))?.split("=")[1] == "false"){
    changeThemeColor();
}

function dayDark(){
    changeThemeColor();
    changeIconTheme();
    document.cookie = 'darkTheme='+dark+'; path=/; max-age=2592000';
}

function changeThemeColor(){
    if(loaded){
        changeIconTheme();
    }
    dark=!dark;
    Object.keys(listColor).forEach(key => {
        const tempColor = listColor[key];
        listColor[key] = getComputedStyle(document.documentElement).getPropertyValue(key);
        root.style.setProperty(key, tempColor);
    });
}

function changeIconTheme(){
    var icon = document.querySelector(".icon_dark_mode");
    icon.innerHTML = dark ? "dark_mode" : "light_mode";
}

window.onload = function(){
    loaded = true;
    console.log(dark);
    changeIconTheme();
}

