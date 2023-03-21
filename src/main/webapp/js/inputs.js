const textboxs = Array.prototype.slice.call(document.querySelectorAll('.textBox input'));
const btns = document.querySelectorAll("input[type='submit']");
const passSecu = document.getElementsByClassName("passSecu")[0];
const listText = [];

textboxs.forEach(textbox => {
    //active or disable the save button
    listText.push(textbox.value);
    textbox.addEventListener('keyup', (e) => {
        var id = textboxs.indexOf(textbox);
        nid = id > 0 ? id-1 : id;
        if(textbox.type == "password"){
            //password Security
            if(btns.length > 1)
                btns[nid].disabled = 1;
            passSecu.classList.remove("passSecu_low");
            passSecu.classList.remove("passSecu_high");
            if(textbox.value.length >= 8){
                if(textbox.value.match(/[A-Z]/g)){
                    if(textbox.value.match(/[a-z]/g)){
                        if(textbox.value.match(/[0-9]/g)){
                            passSecu.classList.add("passSecu_high");
                            passSecu.innerHTML = "Le mdp est securiser";
                            if(btns.length > 1)
                                btns[nid].disabled = 0;
                        }else{
                            passSecu.classList.add("passSecu_low");
                            passSecu.innerHTML = "Le mdp doit contenir des chiffres";
                        }
                    }else{
                        passSecu.classList.add("passSecu_low");
                        passSecu.innerHTML = "Le mdp doit contenir des minuscule";
                    }
                }else{
                    passSecu.classList.add("passSecu_low");
                    passSecu.innerHTML = "Le mdp doit contenir des majuscule";
                }
            }else{
                passSecu.classList.add("passSecu_low");
                passSecu.innerHTML = "Le mdp doit avoir au moin 8 charactere";
            }
        }else if(textbox.type == "date"){
            btns[nid].disabled = 1;
            textbox.addEventListener('keyup', (e) => {
                // get the date input value from the user
                let userInputDate = new Date(textbox.value);

                // calculate the age based on the current date
                let currentDate = new Date();
                let age = currentDate.getFullYear() - userInputDate.getFullYear();

                // if the user's birthdate hasn't occurred yet this year, subtract one from age
                if (currentDate.getMonth() < userInputDate.getMonth() ||
                    (currentDate.getMonth() === userInputDate.getMonth() && currentDate.getDate() < userInputDate.getDate())) {
                    age--;
                }

                // check if the user is under 18 years old
                if (age >= 18) {
                    btns[nid].disabled = 1;
                    // get the button element and disable it
                    let buttonElement = document.getElementById("myButton");
                    buttonElement.disabled = true;
                }
            });
        }else{
            btns[nid].disabled = listText[id] == textbox.value || !textbox.value ? 1 : 0;
        }
    });
    //add detection file
    if(textbox.type == "file"){
        textbox.addEventListener("change", (e) => {
            document.querySelector("#btnImgPP").disabled = 0;
        });
    }
    //add space to the phone number
    if(textbox.type == "tel"){
        textbox.addEventListener('keyup', (e) => {
            textbox.value = textbox.value.replace(/[^0-9]/g, '').replace(/(.{2})/g, '$1 ').trim();
        });
    }

});

//textarea
var textareas = document.querySelectorAll(".textAreaCont");

textareas.forEach(textarea => {
    textarea.addEventListener('keyup', (e) => {
        if(textarea.children[0].value.length > 2000){
            textarea.children[0].value = textarea.children[0].value.slice(0, 2000);
        }
        textarea.children[1].innerHTML = textarea.children[0].value.length+"/2000";
    })
});

//custom select
var selectElem;
var selectContainers = document.querySelectorAll('.custom_select');
selectContainers.forEach(selectContainer => {
    selectContainer.innerHTML += '<div class="cs_container"><div class="cs_select"><p>-- Select Option --</p><span class="material-symbols-outlined">expand_more</span></div><div class="cs_option"></div></div>';
    var btnSelect = selectContainer.children[1].children[0];
    var optContainer = selectContainer.children[1].children[1];
    btnSelect.addEventListener("click", function(){
        selectElem = selectContainer;
        if(optContainer.classList.contains("openSelect")){
            optContainer.classList.remove("openSelect");
            optContainer.style.height = 0;
            optContainer.style.zIndex = 0;
        }else{
            optContainer.classList.add("openSelect");
            optContainer.style.height = 2.2*optContainer.children.length+"em";
            optContainer.style.zIndex = 1;
        }
    });
    var ListOpt = selectContainer.querySelectorAll("select option");
    var allElement = selectContainer.querySelector("select").classList.contains("allElement");
    for(var i=allElement?0:1; i<ListOpt.length; i++){
        optContainer.innerHTML += '<div class="cs_option_e" value="'+ListOpt[i].value+'"><p>'+ListOpt[i].innerHTML+'</p></div>';
    }
    selectContainer.querySelectorAll(".cs_option_e").forEach(e => {
        e.addEventListener("click", function(){
            e.parentNode.parentNode.parentNode.children[0].value = e.getAttribute("value");
            e.parentNode.parentNode.children[0].children[0].innerHTML = e.children[0].innerHTML;
        });
    });
});
document.addEventListener("click", function(){
    selectContainers.forEach(selectContainer => {
        if(selectElem != selectContainer){
            selectContainer.children[1].children[1].classList.remove("openSelect");
            selectContainer.children[1].children[1].style.height = 0;
        }
    });
    selectElem = null;
});
