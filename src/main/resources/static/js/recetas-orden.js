let currentPage = 0;
const pageSize = 6;
let currentOrder = 'fechaDesc';
let currentSearch = '';

// Función principal para cargar as receitas
async function cargarRecetas(page = 0, orden = 'fechaDesc', search = '') {
    try {

        let url = `/api/recetas?page=${page}&size=${pageSize}&orden=${orden}`;
        if (search) {
            url += `&search=${encodeURIComponent(search)}`;
        }

        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Error al cargar las recetas: ${response.status} ${response.statusText}`);
        }

        const data = await response.json();

        if (data && data.content) {
            const recetas = data.content;
            const totalPages = data.totalPages;
            mostrarRecetas(recetas);
            actualizarPaginacion(totalPages, page, orden, search);

             // Quitar a clase 'hidden' para amosar o contido unha vez cargado
             document.getElementById('recetas-section').classList.remove('hidden');
             document.getElementById('footer').classList.remove('hidden');

        } else {
            document.getElementById('recetas-list').innerHTML = '<p>No se encontraron recetas.</p>';
        }
    } catch (error) {
        document.getElementById('recetas-list').innerHTML = `<p>${error.message}</p>`;
    }
}

// Función para actualizar a paxinación
function actualizarPaginacion(totalPages, currentPage, orden, search) {
    const paginationContainer = document.querySelector('.pagination-container');
    paginationContainer.innerHTML = '';

    if (currentPage > 0) {
        const prevButton = document.createElement('button');
        prevButton.textContent = 'Anterior';
        prevButton.addEventListener('click', () => cargarRecetas(currentPage - 1, orden, search));
        paginationContainer.appendChild(prevButton);
    }

    for (let i = 0; i < totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i + 1;
        if (i === currentPage) {
            pageButton.disabled = true;
        }
        pageButton.addEventListener('click', () => cargarRecetas(i, orden, search));
        paginationContainer.appendChild(pageButton);
    }

    if (currentPage < totalPages - 1) {
        const nextButton = document.createElement('button');
        nextButton.textContent = 'Siguiente';
        nextButton.addEventListener('click', () => cargarRecetas(currentPage + 1, orden, search));
        paginationContainer.appendChild(nextButton);
    }
}

// Función para amosar as receitas na páxina
function mostrarRecetas(recetas) {
    const recetasList = document.getElementById('recetas-list');
    recetasList.innerHTML = '';

    recetas.forEach(receta => {
        const recetaDiv = document.createElement('div');
        recetaDiv.classList.add('recipe');
        recetaDiv.innerHTML = `
            <article>
                <div class="recipe-container">
                    <img src=${receta.imagenUrl} class="recipe-img" alt="${receta.nombre}" />
                </div>
                <footer>
                    <p class="recipe-name">${receta.nombre}</p>
                    <h4 class="recipe-autor">${receta.autor}</h4>
                    <div class="like-section">
                        <button class="like-btn" data-id="${receta.id}">
                            <i class="fas fa-thumbs-up"></i>
                            <span class="like-count">${receta.likes}</span>
                        </button>
                    </div>
                </footer>
            </article>
        `;
        recetasList.appendChild(recetaDiv);
    });

    agregarLikeEventListeners();
}

// Función para engadir eventos de "like"
function agregarLikeEventListeners() {
    const likeButtons = document.querySelectorAll('.like-btn');
    likeButtons.forEach(button => {
        button.addEventListener('click', function() {
            const recetaId = this.getAttribute('data-id');
            likeReceta(recetaId, this);
        });
    });
}

// Función para manexar "like" en recetas
function likeReceta(recetaId, likeButton) {
    fetch(`/api/recetas/${recetaId}/like`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
    })
    .then(response => {
        if (!response.ok) throw new Error('Error al hacer like');
        return response.json();
    })
    .then(data => {
        const likesCountSpan = likeButton.querySelector('.like-count');
        likesCountSpan.textContent = data.likes;
    })
    .catch(error => console.error('Error:', error));
}

// Escoita o cambio no menú desplegable de orden
document.getElementById("orden-recetas").addEventListener("change", function(event) {
    currentOrder = event.target.value;
    cargarRecetas(0, currentOrder, currentSearch);
});

// Evento para buscar en tempo real o campo de búsqueda
document.querySelector('.search-input').addEventListener('input', function (event) {
    currentSearch = event.target.value;
    cargarRecetas(0, currentOrder, currentSearch);
});

// Cargar receitas inicialmente
window.onload = () => cargarRecetas(currentPage, currentOrder, currentSearch);
