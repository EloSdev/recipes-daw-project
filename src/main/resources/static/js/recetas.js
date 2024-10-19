 async function cargarRecetas() {
            try {
                const response = await fetch('/api/recetas'); // Ajusta la URL según tu API
                if (!response.ok) {
                    throw new Error('Error al cargar las recetas');
                }
                const recetas = await response.json();
                console.log('Recetas:', recetas); // Imprimir la respuesta

                // Verificar el tipo de datos que se recibe
                if (Array.isArray(recetas)) {
                    mostrarRecetas(recetas);
                } else {
                    console.error('La respuesta no es un array:', recetas);
                    document.getElementById('recetas-list').innerHTML = '<p>Error: los datos no son válidos.</p>';
                }
            } catch (error) {
                console.error('Error:', error);
                document.getElementById('recetas-list').innerHTML = '<p>Error al cargar las recetas.</p>';
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
            // Seleccionamos todos los botones de like
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
                // Aquí actualizamos el contador de likes en el DOM
                const likesCountSpan = likeButton.querySelector('.like-count');
                likesCountSpan.textContent = data.likes; // Actualizamos con el nuevo número de likes
            })
            .catch(error => {
                console.error('Error:', error);
            });
        }




        window.onload = cargarRecetas; // Cargar recetas al cargar la página