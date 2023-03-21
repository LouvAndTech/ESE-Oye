const image_drop = document.querySelector("#image_drop");
let uploaded_image = [];

const message = document.getElementById("errorMessageimage");

image_drop.addEventListener('dragover', (event) => {
    event.stopPropagation();
    event.preventDefault();
    event.dataTransfer.dropEffect = 'copy';
});

image_drop.addEventListener('drop', (event) =>{
    event.stopPropagation();
    event.preventDefault();
    newImage(event.dataTransfer.files);
});

image_drop.addEventListener('change',(event) =>{
    newImage(image_drop.files);
});

function newImage(files){
    let uploaded_image = [];
    document.querySelector("#image_show").innerHTML="";
    message.style.display = "none";
    let resp = heavyTest(files);
    if(!resp){
        document.getElementById('image_drop').files = files;
        for (let i=0;i<files.length;i++)
             readImage(files[i]);

    }else {
        if(resp==='noImage'){
            message.innerHTML = "Vous devez ajouter au moins une image"
        }else if(resp==='moreImage'){
            message.innerHTML = "Vous êtes limité à 5 images par annonce"
        }else if(resp==='tooBig'){
            message.innerHTML = "L'une des images fait plus de 4Mo"
        }
        document.querySelector("#image_drop").style.backgroundImage = ""
        message.style.display = "block";
    }
}

readImage = (file) => {
    const reader = new FileReader();
    reader.addEventListener('load', (event) => {
        uploaded_image.push(event.target.result);
        let img = new Image();
        img.src = `${uploaded_image[uploaded_image.length-1]}`;
        document.querySelector("#image_show").appendChild(img);
    });
    reader.readAsDataURL(file);
}

function heavyTest (fileList){
    //If there is less than an image or more than five
    if (fileList.length < 1) return 'noImage';
    else if(fileList.length > 5) return 'moreImage';

    //If one of the images is more than 8MB
    for (let i=0;i < fileList.length; i++){
        if(fileList[i].size > 4*(10**6)){
            return 'tooBig'
        }
    }
    //else return OK
    return false
}