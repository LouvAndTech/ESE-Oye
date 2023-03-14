function btchange(){
    // get the date input value from the user
    let userInputDate = new Date(document.getElementById("mybday").value);

// calculate the age based on the current date
    let currentDate = new Date();
    let age = currentDate.getFullYear() - userInputDate.getFullYear();

// if the user's birthdate hasn't occurred yet this year, subtract one from age
    if (currentDate.getMonth() < userInputDate.getMonth() ||
        (currentDate.getMonth() === userInputDate.getMonth() && currentDate.getDate() < userInputDate.getDate())) {
        age--;
    }

// check if the user is under 18 years old
    if (age < 18) {
        // get the button element and disable it
        let buttonElement = document.getElementById("myButton");
        buttonElement.disabled = true;
    }
}

function isNumericInput(input) {
    // Check if input consists only of digits
    return /^[0-9]+$/.test(input);
}
