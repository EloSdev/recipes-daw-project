//función para obter o usuario logueado e actualizar barra de navegación 
        async function mostrarUsuario() {
            try {
                const response = await fetch('http://localhost:8080/api/usuarios');
                if (response.ok) {
                    const data = await response.json();
                    if (data.username) {
                        document.getElementById('user-nickname').textContent = data.username;
                }
            }
             }catch (error) {
                console.error('Error al obtener el usuario:', error);
            }
        }
        document.addEventListener("DOMContentLoaded", mostrarUsuario);