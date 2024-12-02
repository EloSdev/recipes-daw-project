//comprobación de si o usuario esta autenticado para actualizar o contido html da esquina superior dereita da barra de navegación

document.addEventListener("DOMContentLoaded", function () {
  fetch("/api/usuarios/autenticado")
    .then((response) => {
      console.log(response);
      if (response.ok) {
        return response.text();
      } else {
        throw new Error("No autenticado");
      }
    })
    .then((username) => {
      localStorage.setItem("username", username);
      
      //si o usuario esta autenticado, pinta o seu nome donde antes estaba o botón de login e pinta logout onde antes estaba o botón de rexistro
      document.getElementById("user-nickname").innerText = username;
      document.getElementById("login").style.display = "none";
      document.getElementById("registro").style.display = "none";
      document.getElementById("user-nickname").style.display = "block";
      document.getElementById("logout").style.display = "block";
      document.getElementById("subir-receta").classList.remove("hidden");
    })
    .catch((error) => {
      document.getElementById("login").style.display = "block";
      document.getElementById("registro").style.display = "block";
      document.getElementById("user-nickname").style.display = "none";
      document.getElementById("logout").style.display = "none";
      
    });
});

//exportar función para usala en recetas.js
export async function isAuthenticated() {
  try {
    const response = await fetch("/api/usuarios/autenticado");
    return response.ok;
  } catch {
    return false;
  }
}
