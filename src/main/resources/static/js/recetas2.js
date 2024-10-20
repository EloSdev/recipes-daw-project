let currentPage = 0;
const pageSize = 6;

async function cargarRecetas(page = 0) {
    mostrarMensajeCargando(); // Mostrar mensaje de carga
    try {
        const response = await fetch(`/api/recetas?page=${page}&size=${pageSize}`);
        if (!response.ok) {
            throw new Error(`Error al cargar las recetas: ${response.status} ${response.statusText}`);
        }

        const data = await response.json();

        // Verifica que la respuesta sea un objeto con la estructura esperada
        if (data && data.content) {
            const recetas = data.content; // Contenido de la página actual
            const totalPages = data.totalPages; // Total de páginas

            console.log('Recetas:', recetas);
            mostrarRecetas(recetas);
            actualizarPaginacion(totalPages, page);
        } else {
            console.error('La respuesta no contiene las recetas esperadas:', data);
            mostrarError('Error: los datos no son válidos.');
        }
    } catch (error) {
        console.error('Error:', error);
        mostrarError(error.message);
    } finally {
        ocultarMensajeCargando(); // Ocultar mensaje de carga
    }
}

// Función para mostrar mensaje de carga
function mostrarMensajeCargando() {
    document.getElementById('recetas-list').innerHTML = '<p>Cargando recetas...</p>';
}

// Función para ocultar mensaje de carga
function ocultarMensajeCargando() {
    // Aquí podrías implementar lógica si es necesario
}

function mostrarError(message) {
    document.getElementById('recetas-list').innerHTML = `<p>${message}</p>`;
}

// Llamar a cargarRecetas para la página inicial
//cargarRecetas(currentPage);



function actualizarPaginacion(totalPages, currentPage) {
    const paginationContainer = document.querySelector('.pagination-container');
    paginationContainer.innerHTML = ''; // Limpiamos la paginación anterior

    // Crear botón "Anterior"
    if (currentPage > 0) {
        const prevButton = document.createElement('button');
        prevButton.textContent = 'Anterior';
        prevButton.addEventListener('click', () => {
            cargarRecetas(currentPage - 1); // Ir a la página anterior
        });
        paginationContainer.appendChild(prevButton);
    }

    // Crear botones de páginas numeradas
    for (let i = 0; i < totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i + 1;
        if (i === currentPage) {
            pageButton.disabled = true; // Deshabilitar el botón de la página actual
        }
        pageButton.addEventListener('click', () => {
            cargarRecetas(i); // Cargar la página seleccionada
        });
        paginationContainer.appendChild(pageButton);
    }

    // Crear botón "Siguiente"
    if (currentPage < totalPages - 1) {
        const nextButton = document.createElement('button');
        nextButton.textContent = 'Siguiente';
        nextButton.addEventListener('click', () => {
            cargarRecetas(currentPage + 1); // Ir a la página siguiente
        });
        paginationContainer.appendChild(nextButton);
    }
}



function mostrarRecetas(recetas) {
    const recetasList = document.getElementById('recetas-list');
    recetasList.innerHTML = ''; // Limpiar el contenido previo

    recetas.forEach(receta => {
        const recetaDiv = document.createElement('div');
        recetaDiv.classList.add('recipe');
        recetaDiv.innerHTML = `
                    <article>
                      <div class="recipe-container">
                        <img src=${receta.imagenUrl} class="recipe-img" alt=${receta.nombre} />
                      </div>
                      <footer>
                        <p class="recipe-name">${receta.nombre}</p>
                        <h4 class="recipe-autor">${receta.autor}</h4>
                        <div class="like-section">
                              <button class="like-btn" data-id=${receta.id}>
                                      <i class="fas fa-thumbs-up"></i>
                                      <span class="like-count">${receta.likes}</span>
                              </button>
                        </div>
                      </footer>
                    </article>
        `;
        recetasList.appendChild(recetaDiv);
    });

    // Aquí es donde ahora agregamos el evento de like
    agregarLikeEventListeners();
}

function agregarLikeEventListeners() {
    const likeButtons = document.querySelectorAll('.like-btn');
    console.log(likeButtons); // Deberías ver una lista de botones en la consola

    likeButtons.forEach(button => {
        button.addEventListener('click', function() {
            const recetaId = this.getAttribute('data-id');
            likeReceta(recetaId, this); // Pasamos el botón mismo para actualizar el número de likes
        });
    });
}

function likeReceta(recetaId, likeButton) {
    console.log(recetaId);
    console.log(`URL: /api/recetas/${recetaId}/like`);
    fetch(`/api/recetas/${recetaId}/like`, {
        method: 'POST', // Usamos POST para hacer la actualización del like
        headers: {
            'Content-Type': 'application/json',  // Enviar y recibir JSON
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al hacer like');
        }
        return response.json(); // Devolvemos la respuesta como JSON
    })
    .then(data => {
        const likesCountSpan = likeButton.querySelector('.like-count');
        likesCountSpan.textContent = data.likes; // Actualizamos con el nuevo número de likes
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

window.onload = cargarRecetas(currentPage);
;
