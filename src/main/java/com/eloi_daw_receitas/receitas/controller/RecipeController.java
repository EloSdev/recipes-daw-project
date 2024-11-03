package com.eloi_daw_receitas.receitas.controller;

import com.eloi_daw_receitas.receitas.model.Recipe;
import com.eloi_daw_receitas.receitas.model.Usuario;
import com.eloi_daw_receitas.receitas.repository.UsuarioRepository;
import com.eloi_daw_receitas.receitas.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RecipeController {

    @Autowired
    private final RecipeService recipeService;

    @Autowired
    private UsuarioRepository usuarioRepository; // Repositorio para obtener el usuario

    // private final String UPLOAD_DIR = "uploads/"; // Directorio donde se guardarán las imágenes

    @Autowired
    private RecipeController(RecipeService recipeService) {

        this.recipeService = recipeService;

    }

    @GetMapping(value = "/recetas/{id}")
    public ResponseEntity<Recipe> obtenerRecetaPorId(@PathVariable Long id) {
        Recipe receita = recipeService.obtenerRecetaPorId(id);
        if (receita != null) {
            return ResponseEntity.ok(receita);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    /*
    @GetMapping(value = "/recetas")
    public Page<Recipe> listarRecetas(@RequestParam(defaultValue = "fechaDesc") String orden,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "6") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return recipeService.listarRecetas(orden, pageable);
    }*/
    /*@GetMapping(value = "/recetas")
    public List<Recipe> listarRecetas(
            @RequestParam(defaultValue = "fechaDesc") String orden,
            @PageableDefault(size = 6) Pageable pageable) {
        Page<Recipe> pageRecetas = recipeService.listarRecetas(orden, pageable);
        return pageRecetas.getContent(); // Devuelve solo el contenido (las recetas)
    }*/

    /*
    @GetMapping(value = "/recetas")
    public Page<Recipe> listarRecetas(
            @RequestParam(defaultValue = "fechaDesc") String orden,
            @PageableDefault(size = 6) Pageable pageable) {
        //return recipeService.listarRecetas(orden, pageable);
        Page<Recipe> pageRecetas = recipeService.listarRecetas(orden, pageable);
        return pageRecetas; // Devuelve la página completa

    }*/
    @GetMapping(value = "/recetas")
    public Page<Recipe> listarRecetas(
            @RequestParam(defaultValue = "fechaDesc") String orden,
            @RequestParam(required = false) String search, // Parámetro de búsqueda opcional
            @PageableDefault(size = 6) Pageable pageable) {

        if (search != null && !search.isEmpty()) {
            // Si hay un término de búsqueda, llama a un método de servicio que filtre por nombre
            return recipeService.buscarRecetasPorNombre(search, orden, pageable);
        } else {
            // Si no hay búsqueda, devuelve la lista completa ordenada
            return recipeService.listarRecetas(orden, pageable);
        }
    }

    @GetMapping("/recetas/all")
    public List<Recipe> listarTodasLasRecetas() {
        return recipeService.listarTodasLasRecetas(); // Devuelve todas las recetas
    }

    @GetMapping(value = "/recetas/search")
    public ResponseEntity<List<Recipe>> buscarRecetasPorNombre(@RequestParam String nombre) {
        List<Recipe> recetas = recipeService.buscarRecetasPorNombre(nombre);
        return ResponseEntity.ok(recetas); // Devuelve una lista, puede ser vacía
    }


    // Endpoint para incrementar el número de likes
    @PostMapping("recetas/{recetaId}/like")
    public ResponseEntity<Recipe> incrementarLike(@PathVariable Long recetaId) {
        Recipe recetaActualizada = recipeService.incrementarLikes(recetaId);

        // Verifica si se actualizó la receta
        if (recetaActualizada != null) {
            return ResponseEntity.ok(recetaActualizada); // Asegúrate de que esto devuelva un JSON válido
        } else {
            return ResponseEntity.notFound().build(); // Devuelve 404 si la receta no se encuentra
        }
    }

    /*@PostMapping(value = "/recetas/subir-receta", consumes = "application/json")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Recipe crearReceta(@RequestBody Recipe receita, Authentication authentication) {
        receita.setAutor(authentication.getName());

        return recipeService.crearReceta(receita);
    }*/

    //corregir problema de que si se sube unha receta con nome colhido reescribese
    //outro problema> unha vez logueado, si vou recetas.html sae login e registrar
    //nickname e logout so aparece en home.html
    //ten que aparecer en home.html,recetas,html. e about .html si login ok
    @PostMapping("recetas/subir-receta") // Asegúrate de que la URL coincida con la de tu formulario
    public ResponseEntity<Recipe> crearReceta(
            @RequestParam("nombre") String nombre,
            @RequestParam("ingredientes") String ingredientes,
            @RequestParam("elaboracion") String elaboracion,
            @RequestParam("imagen") MultipartFile imagen, // Cambiado para recibir la imagen
            @RequestParam("autor") String autor,
            @RequestParam("likes") int likes) {

        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Obtener el nombre de usuario

        // Buscar el usuario por nickname y manejar el caso opcional
        Optional<Usuario> optionalUsuario = usuarioRepository.findByNickname(username);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get(); // Obtener el usuario

            // Lógica para guardar la receta
            Recipe nuevaReceta = new Recipe();
            nuevaReceta.setNombre(nombre);
            nuevaReceta.setIngredientes(ingredientes);
            nuevaReceta.setElaboracion(elaboracion);
            nuevaReceta.setAutor(usuario.getNickname());
            nuevaReceta.setLikes(likes);
            nuevaReceta.setFecha(new Date()); // Establecer la fecha actual
            nuevaReceta.setUsuario(usuario);

            // Maneja el archivo de imagen
            if (imagen != null && !imagen.isEmpty()) {
                try {
                    // Definir la ruta donde se guardará la imagen
                    String rutaImagen = "src/main/resources/static/images/recetas/" + imagen.getOriginalFilename();
                    File file = new File(rutaImagen);

                    // Escribir la imagen en la ruta especificada
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(imagen.getBytes());
                    }

                    // Almacenar la URL relativa de la imagen en la base de datos
                    String imagenUrl = "/images/recetas/" + imagen.getOriginalFilename(); // URL que se guardará en la base de datos
                    nuevaReceta.setImagenUrl(imagenUrl); // Asigna la URL a la receta

                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null); // Manejo de errores si la imagen no se puede procesar
                }
            }

            // Guarda la receta en la base de datos usando tu servicio
            recipeService.crearReceta(nuevaReceta);



           return new ResponseEntity<>(nuevaReceta, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Usuario no encontrado
        }
    }

}