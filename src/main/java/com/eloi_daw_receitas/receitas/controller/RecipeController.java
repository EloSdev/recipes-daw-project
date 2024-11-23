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
    private UsuarioRepository usuarioRepository;

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

    @GetMapping(value = "/recetas")
    public Page<Recipe> listarRecetas(
            @RequestParam(defaultValue = "fechaDesc") String orden,
            @RequestParam(required = false) String search, // Parámetro de búsqueda opcional
            @PageableDefault(size = 6) Pageable pageable) {

        if (search != null && !search.isEmpty()) {

            return recipeService.buscarRecetasPorNombre(search, orden, pageable);
        } else {

            return recipeService.listarRecetas(orden, pageable);
        }
    }

    @GetMapping("/recetas/all")
    public List<Recipe> listarTodasLasRecetas() {
        return recipeService.listarTodasLasRecetas();
    }

    @GetMapping(value = "/recetas/search")
    public ResponseEntity<List<Recipe>> buscarRecetasPorNombre(@RequestParam String nombre) {
        List<Recipe> recetas = recipeService.buscarRecetasPorNombre(nombre);
        return ResponseEntity.ok(recetas); // Devolve unha lista, pode estar vacía
    }

    // Endpoint para incrementar o número de likes
    /*
     * @PostMapping("recetas/{recetaId}/like")
     * public ResponseEntity<Recipe> incrementarLike(@PathVariable Long recetaId) {
     * Recipe recetaActualizada = recipeService.incrementarLikes(recetaId);
     * 
     * if (recetaActualizada != null) {
     * return ResponseEntity.ok(recetaActualizada);
     * } else {
     * return ResponseEntity.notFound().build();
     * }
     * }
     */
    // Endpoint para incrementar o número de likes
    @PostMapping("recetas/{recetaId}/like")
    public ResponseEntity<Recipe> incrementarLike(@PathVariable Long recetaId, Authentication authentication) {
        String username = authentication.getName(); 
        try {
            Recipe recetaActualizada = recipeService.incrementarLikes(recetaId, username);
            return ResponseEntity.ok(recetaActualizada);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); 
        }
    }

    @PostMapping("recetas/subir-receta")
    public ResponseEntity<Recipe> crearReceta(
            @RequestParam("nombre") String nombre,
            @RequestParam("ingredientes") String ingredientes,
            @RequestParam("elaboracion") String elaboracion,
            @RequestParam("imagen") MultipartFile imagen,
            @RequestParam("autor") String autor,
            @RequestParam("likes") int likes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Usuario> optionalUsuario = usuarioRepository.findByNickname(username);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            // Lóxica para gardar a receita
            Recipe nuevaReceta = new Recipe();
            nuevaReceta.setNombre(nombre);
            nuevaReceta.setIngredientes(ingredientes);
            nuevaReceta.setElaboracion(elaboracion);
            nuevaReceta.setAutor(usuario.getNickname());
            nuevaReceta.setLikes(likes);
            nuevaReceta.setFecha(new Date());
            nuevaReceta.setUsuario(usuario);

            if (imagen != null && !imagen.isEmpty()) {
                try {
                    // Xenerar un nome único para o arquivo utilizando a marca de tempo actual
                    String extension = imagen.getOriginalFilename()
                            .substring(imagen.getOriginalFilename().lastIndexOf('.'));
                    String nuevoNombreArchivo = username + "_" + System.currentTimeMillis() + extension;

                    // Definir a ruta onde se gardará a imaxe
                    String rutaImagen = "src/main/resources/static/images/recetas/" + nuevoNombreArchivo;
                    File file = new File(rutaImagen);

                    // Escribir a imaxe na ruta especificada
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(imagen.getBytes());
                    }
                    // Almacenar a URL relativa da imaxe na base de datos
                    String imagenUrl = "/images/recetas/" + nuevoNombreArchivo;
                    nuevaReceta.setImagenUrl(imagenUrl);

                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null);
                }
            }
            recipeService.crearReceta(nuevaReceta);
            return new ResponseEntity<>(nuevaReceta, HttpStatus.CREATED);

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}