//Envío dos datos do login ao back para a autenticación do login

document.getElementById('login-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    try {

        const response = await fetch("/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded", //Tipo de contido esperado por Spring Security.
            },
            body: new URLSearchParams({ username, password }), //Formatea os datos como formulario
        });

        if (response.redirected) {
            window.location.href = response.url;
        } else if (response.status === 401) {
            alert("Credenciales incorrectas. Por favor, inténtalo de nuevo.");
        } else {
            const errorText = await response.text();
            console.error("Respuesta inesperada:", errorText);
            alert("Ocurrió un error inesperado. Por favor, inténtalo más tarde.");
        }
    } catch (error) {
        console.error("Error al intentar autenticar:", error);
        alert("Ocurrió un error de conexión. Por favor, inténtalo más tarde.");
    }
});
