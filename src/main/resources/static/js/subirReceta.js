document.getElementById("form-subir-receta").addEventListener("submit", function (event) {
    event.preventDefault();
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
                alert("Error al crear la receta: " + text);
            });
        }
    })
    .catch(error => console.error("Error en el fetch:", error));
});
