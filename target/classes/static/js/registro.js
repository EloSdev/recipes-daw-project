//Envío do usuario rexistrodo a bbdd
//Validación no formulario de rexistro

document.getElementById('registro-form').addEventListener('submit', async function (event) {
    event.preventDefault();

    // Obter os valores dos campos do formulario
    const nickname = document.getElementById('nickname').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const repeatPassword = document.querySelector('input[placeholder="Repite tu password"]').value;
    const termsAccepted = document.getElementById('terms').checked;

    //Limpar mensaxes de erro previos
    clearErrors();

    //Validar campos
    const isNicknameValid = validateNickname(nickname);
    const isEmailValid = validateEmail(email);
    const isPasswordValid = validatePassword(password);
    const doPasswordsMatch = validatePasswordsMatch(password, repeatPassword);
    const areTermsAccepted = validateTerms(termsAccepted);

    //erro en ao menos unha validación
    if (!isNicknameValid || !isEmailValid || !isPasswordValid || !doPasswordsMatch || !areTermsAccepted) {

        return;
    }


    const data = {
        nickname,
        email,
        password,
        rol: 'USER'
    };

    try {
        const response = await fetch('http://localhost:8080/api/usuarios', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            alert('Usuario registrado exitosamente');
            window.location.href = '/index.html';
        } else if (response.status === 400) {
            const errorText = await response.text(); 
            console.log('Error 400:', errorText);

            alert('El nickname elegido no está disponible. Pruebe con otro.'); 
        } else {
            const errorText = await response.text(); 
            alert(`Error al registrar: ${errorText}`);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Ocurrió un error de conexión. Por favor, inténtalo más tarde.');
    }
});

//Función para limpar mensaxes de erro previos
function clearErrors() {
    const errorMessages = document.querySelectorAll('.error-message');
    errorMessages.forEach(error => error.remove());
}

// Función para amosar mensaxes de erro
function showError(inputElement, message) {
    const errorElement = document.createElement('p');
    errorElement.classList.add('error-message');
    errorElement.style.color = 'red';
    errorElement.style.fontSize = '0.9rem';
    errorElement.textContent = message;
    inputElement.parentElement.appendChild(errorElement);
}

//Validar nickname
function validateNickname(nickname) {
    if (nickname.trim().length < 3 || nickname.trim().length > 15) {
        showError(document.getElementById('nickname'), 'El nickname debe tener entre 3 y 15 caracteres.');
        return false;
    }
    if (!/^[a-zA-Z0-9_]+$/.test(nickname)) {
        showError(document.getElementById('nickname'), 'El nickname solo puede contener letras, números y guiones bajos.');
        return false;
    }
    return true;
}

//Validar email
function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        showError(document.getElementById('email'), 'Por favor, introduce un correo electrónico válido.');
        return false;
    }
    return true;
}

//Validar contrasinal
function validatePassword(password) {
    if (password.length < 8) {
        showError(document.getElementById('password'), 'La contraseña debe tener al menos 8 caracteres.');
        return false;
    }
    if (!/[A-Z]/.test(password) || !/[a-z]/.test(password) || !/[0-9]/.test(password)) {
        showError(document.getElementById('password'), 'La contraseña debe incluir al menos una letra mayúscula, una minúscula y un número.');
        return false;
    }
    return true;
}

//Validar que as contrasinais coincidan
function validatePasswordsMatch(password, repeatPassword) {
    if (password !== repeatPassword) {
        showError(document.querySelector('input[placeholder="Repite tu password"]'), 'Las contraseñas no coinciden.');
        return false;
    }
    return true;
}

//Validar términos e condicións
function validateTerms(accepted) {
    if (!accepted) {
        showError(document.getElementById('terms').parentElement, 'Debes aceptar los términos y condiciones.');
        return false;
    }
    return true;
}
