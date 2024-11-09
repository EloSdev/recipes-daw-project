document.getElementById('registro-form').addEventListener('submit', async function (event) {
            event.preventDefault();

            const data = {
                nickname: document.getElementById('nickname').value,
                password: document.getElementById('password').value,
                email: document.getElementById('email').value,
                rol: 'USER'
            };

            try {
                const response = await fetch('http://localhost:8080/api/usuarios', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                     body: JSON.stringify(data)
                });

                if (response.ok) {
                    alert('Usuario registrado exitosamente');
                    window.location.href = '/index.html';
                } else if (response.status === 400) {

                            const errorData = await response.json();
                            console.log('Error 400:', errorData.message);
                            alert('El usuario ya está registrado.');
                } else {
                    const errorData = await response.json();
                    alert(`Error al registrar: ${errorData.message}`);
                }
            } catch (error) {
                console.error('Error:', error);
            }

        });