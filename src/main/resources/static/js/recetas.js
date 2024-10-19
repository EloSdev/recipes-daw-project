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
                                <img src=${receta.imagenUrl} class="recipe-img img" alt=${receta.nombre} />
                              </div>
                              <footer>
                                <p class="recipe-name">${receta.nombre}</p>
                                <h4 class="recipe-autor">${receta.autor}</h4>
                              </footer>
                            </article>
                `;
                recetasList.appendChild(recetaDiv);
            });
        }

        window.onload = cargarRecetas; // Cargar recetas al cargar la página