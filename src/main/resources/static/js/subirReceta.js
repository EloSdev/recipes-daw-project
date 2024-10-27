document.getElementById("form-subir-receta").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevenir la redirección predeterminada
    const formData = new FormData(this);

    fetch("/api/recetas/subir-receta", {
        method: "POST",
        body: formData
    })
    .then(response => {
        // Registrar el estado de la respuesta para ver si hay un error específico
        console.log("Estado de la respuesta:", response.status);

        if (response.ok) {
            window.location.href = "/home.html"; // Redirigir a home.html
        } else {
            // Leer el mensaje de error para más detalles
            response.text().then(text => {
                console.error("Error en la respuesta:", text);
                alert("Error al crear la receta: " + text); // Muestra el error en el mensaje
            });
        }
    })
    .catch(error => console.error("Error en el fetch:", error));
});
