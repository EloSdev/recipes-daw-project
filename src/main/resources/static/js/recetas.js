//Ficheiro para cargar as receitas e amosalas según diversos criterios establecidos nas funcións que se detallan
//Ao facer click en cada receita, ábrese un modal con datos sobre a mesma
//Cada receita ten a posibilidade de darlle a like sempre e cando o usuario esté autenticado

import { isAuthenticated } from './auth.js';

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

            const userAuthenticated = await isAuthenticated();
            if (userAuthenticated) {
                const username = localStorage.getItem("username");
                const recetasVotadas = await obtenerRecetasVotadas(username); 

                mostrarRecetas(recetas, recetasVotadas);  

            } else {
                mostrarRecetas(recetas);  
            }

            actualizarPaginacion(totalPages, page, orden, search);

             //Quitar a clase 'hidden' para amosar o contido unha vez cargado
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
async function mostrarRecetas(recetas, recetasVotadas = new Set()) {
    const recetasList = document.getElementById('recetas-list');
    recetasList.innerHTML = '';

    const userAuthenticated = await isAuthenticated();

    recetas.forEach(receta => {
        const recetaDiv = document.createElement('div');
        recetaDiv.classList.add('recipe');

        //si as receitas xa foron votadas, actualízase o icono do pulgar cara arriba a un candado
        const likeButtonClass = recetasVotadas.has(receta.id) ? 'like-btn liked' : 'like-btn'; 
        const likeButtonClassI = recetasVotadas.has(receta.id) ? 'fa fa-lock' : 'fas fa-thumbs-up'; 

        const likeButtonHTML = userAuthenticated
            ? `<div class="like-section">
                    <button class="${likeButtonClass}" data-id="${receta.id}">
                        <i class="${likeButtonClassI}"></i>
                        <span class="like-count">${receta.likes} votos</span>
                    </button>
               </div>`
            : `<div class="like-section">
                    <span class="like-count">${receta.likes} votos</span>
               </div>`; 

        recetaDiv.innerHTML = `
            <article>
                <div class="recipe-container">
                    <img src=${receta.imagenUrl} class="recipe-img" alt="${receta.nombre}" />
                    <div class="recipe-icons">
                          <button class="recipe-icon modal-btn" data-id="${receta.id}">
                          <i class="fas fa-search"></i>
                           </button>
                    </div>
                </div>
                <footer>
                    <p class="recipe-name">${receta.nombre}</p>
                    <h4 class="recipe-autor">${receta.autor}</h4>
                    ${likeButtonHTML} 
                </footer>
            </article>
        `;
        recetasList.appendChild(recetaDiv);
    });

    if (userAuthenticated) {
        agregarLikeEventListeners(); 
    }

    agregarModalEventListeners();
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

// Función para manexar "like" en receitas
function likeReceta(recetaId, likeButton) {
    fetch(`/api/recetas/${recetaId}/like`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
    })
    .then(response => {
        if (response.status === 409) {
            return null;
        }
        if (!response.ok) throw new Error('Error al hacer like');
        return response.json();
    })
    .then(data => {
        if (data) {
            
            //actualizar número de likes e cambio de iconos
            const likesCounter = likeButton.querySelector('.like-count');
            likesCounter.textContent = data.likes;
            likeButton.classList.add('liked'); 
            const icon = likeButton.querySelector('i');
            icon.classList.replace('fa-thumbs-up', 'fa-lock');
        }
    })
    .catch(error => console.error('Error:', error));
}

// Función para obter as receitas que o usuario xa votou
async function obtenerRecetasVotadas(username) {
    try {
        const response = await fetch(`/api/votadas/${username}`);
        if (!response.ok) {
            throw new Error('Error al obtener las recetas votadas');
        }
        const data = await response.json();
        return new Set(data); 
    } catch (error) {
        console.error('Error:', error);
        return new Set(); 
    }
}

// Escoitar o cambio no menú desplegable de orden
document.getElementById("orden-recetas").addEventListener("change", function(event) {
    currentOrder = event.target.value;
    cargarRecetas(0, currentOrder, currentSearch);
});

// Evento para buscar en tempo real no campo de búsqueda
document.querySelector('.search-input').addEventListener('input', function (event) {
    currentSearch = event.target.value;
    cargarRecetas(0, currentOrder, currentSearch);
});

//MODAL
// función para agregar eventos aos botóns do modal
function agregarModalEventListeners() {
    const modalButtons = document.querySelectorAll('.modal-btn');
    modalButtons.forEach(button => {
        button.addEventListener('click', async function () {
            const recetaId = this.getAttribute('data-id');
            await cargarDetallesReceta(recetaId);
        });
    });
}

//función para cargar os detalles da receta no modal
async function cargarDetallesReceta(recetaId) {
    try {
        const response = await fetch(`/api/recetas/${recetaId}`);
        if (!response.ok) {
            throw new Error(`Error al cargar los detalles de la receta: ${response.statusText}`);
        }

        const receta = await response.json();
        mostrarDetallesEnModal(receta);

    } catch (error) {
        console.error(error.message);
    }
}

// función para amosar os detalles no modal
function mostrarDetallesEnModal(receta) {
    const modal = document.querySelector('.modal-overlay');
    const modalContent = document.querySelector('.modal-container');

    modalContent.innerHTML = `
        <button class="close-btn"><i class="fas fa-times"></i></button>
        <h3>${receta.nombre}</h3>
        <p><strong>Autor/a:</strong> ${receta.autor}</p>
        <p><strong>Ingredientes:</strong> ${receta.ingredientes}</p>
        <p><strong>Preparación:</strong> ${receta.elaboracion}</p>
    `;

    modal.classList.add('open-modal');

    document.querySelector('.close-btn').addEventListener('click', () => {
        modal.classList.remove('open-modal');
    });
}

// Cargar receitas inicialmente
window.onload = async () => {
    cargarRecetas(currentPage, currentOrder, currentSearch);
}
