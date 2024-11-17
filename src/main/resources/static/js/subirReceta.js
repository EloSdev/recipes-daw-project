document.getElementById("form-subir-receta").addEventListener("submit", function (event) {
    event.preventDefault();
    clearErrorMessages();

    const nombre = document.getElementById("nombre").value.trim();
    const ingredientes = document.getElementById("ingredientes").value.trim();
    const elaboracion = document.getElementById("elaboracion").value.trim();
    const imagen = document.getElementById("imagen").files[0]; // Obtenemos el archivo de imagen

    let isValid = true;

    // validacións
    if (nombre === "") {
        showError("nombre", "El nombre de la receta es obligatorio.");
        isValid = false;
    }

    if (ingredientes === "") {
            showError("ingredientes", "Debes incluir los ingredientes.");
            isValid = false;
        }

    if (elaboracion === "") {
        showError("elaboracion", "La elaboración es obligatoria.");
        isValid = false;
    }


    if (!imagen) {
        showError("imagen", "Debe subir una imagen para la receta.");
        isValid = false;
    } else {

        const allowedImageTypes = ["image/jpeg", "image/png", "image/gif"];
        if (!allowedImageTypes.includes(imagen.type)) {
            showError("imagen", "Solo se permiten imágenes en formato JPG, PNG o GIF.");
            isValid = false;
        }
    }

    if (!isValid) {
        return;
    }

    // si pasan tódalas validacións:
    const formData = new FormData(this);

    fetch("/api/recetas/subir-receta", {
        method: "POST",
        body: formData
    })
    .then(response => {
        console.log("Estado de la respuesta:", response.status);

        if (response.ok) {
            window.location.href = "/home.html";
        } else {
            response.text().then(text => {
                console.error("Error en la respuesta:", text);
                showError("form", "Error al crear la receta: " + text);
            });
        }
    })
    .catch(error => {
        console.error("Error en el fetch:", error);
        showError("form", "Ocurrió un error inesperado al crear la receta.");
    });
});

// función para amosar mensaxe de erro
function showError(fieldId, message) {
    const field = document.getElementById(fieldId);
    const errorMessage = document.createElement("span");
    errorMessage.classList.add("error-message");
    errorMessage.textContent = message;

    field.insertAdjacentElement("afterend", errorMessage);
}

// función para limpar as mensaxes de erro previas
function clearErrorMessages() {
    const errorMessages = document.querySelectorAll(".error-message");
    errorMessages.forEach(message => message.remove());
}
