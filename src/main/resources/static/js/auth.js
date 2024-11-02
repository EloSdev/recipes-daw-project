// archivo js, por ejemplo auth.js, incluido en todas las páginas
document.addEventListener("DOMContentLoaded", function() {
    fetch("/api/usuarios/autenticado")
        .then(response => {
         console.log(response); // Verifica la respuesta
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("No autenticado");
            }
        })
        .then(username => {
            document.getElementById("user-nickname").innerText = username;
            document.getElementById("login").style.display = "none";
            document.getElementById("registro").style.display = "none";
            document.getElementById("user-nickname").style.display = "block";
            document.getElementById("logout").style.display = "block";
        })
        .catch(error => {
            document.getElementById("login").style.display = "block";
            document.getElementById("registro").style.display = "block";
            document.getElementById("user-nickname").style.display = "none";
            document.getElementById("logout").style.display = "none";
        });
});
