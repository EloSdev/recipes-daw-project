// Función para obtener el usuario logueado y actualizar la interfaz
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