document.getElementById('registro-form').addEventListener('submit', async function (event) {
            event.preventDefault();

            const data = {
                nickname: document.getElementById('nickname').value,
                password: document.getElementById('password').value,
                email: document.getElementById('email').value,
                rol: 'USER' // O el rol que deseas enviar
            };

            try {
                const response = await fetch('http://localhost:8080/api/usuarios', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                     body: JSON.stringify(data) // Envío de los datos del formulario al backend
                });

                if (response.ok) {
                    alert('Usuario registrado exitosamente');
                    window.location.href = '/index.html'; // Redirige al index
                } else if (response.status === 400) {
                            // Usuario ya registrado
                            const errorData = await response.json();
                            console.log('Error 400:', errorData.message);
                            alert('El usuario ya está registrado.'); // Muestra un mensaje al usuario
                } else {
                    const errorData = await response.json();
                    alert(`Error al registrar: ${errorData.message}`);
                }
            } catch (error) {
                console.error('Error:', error);
            }

        });