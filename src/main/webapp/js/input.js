const textboxs = Array.prototype.slice.call(document.querySelectorAll('.textBox input'));
const btns = document.querySelectorAll("input[type='submit']");
const passSecu = document.getElementsByClassName("passSecu")[0];
const listText = [];
textboxs.forEach(textbox => {
    //active or disable the save button
    listText.push(textbox.value);
    textbox.addEventListener('keyup', (e) => {
        var id = textboxs.indexOf(textbox);
        if(textbox.type != "password"){
            nid = id > 0 ? id-1 : id;
            btns[nid].disabled = listText[id] == textbox.value || !textbox.value ? 1 : 0;
        }else{
            //password Security
            passSecu.classList.remove("passSecu_low");
            passSecu.classList.remove("passSecu_high");
            if(textbox.value.length >= 8){
                if(textbox.value.match(/[A-Z]/g)){
                    if(textbox.value.match(/[a-z]/g)){
                        if(textbox.value.match(/[0-9]/g)){
                            passSecu.classList.add("passSecu_high");
                            passSecu.innerHTML = "Le mdp est securiser";
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
        }
    });

    //add space to the phone number
    if(textbox.type == "tel"){
        textbox.addEventListener('keyup', (e) => {
            textbox.value = textbox.value.replace(/[^0-9]/g, '').replace(/(.{2})/g, '$1 ').trim();
        });
    }
});

