package com.eloi_daw_receitas.receitas.controller;

import com.eloi_daw_receitas.receitas.model.Recipe;
import com.eloi_daw_receitas.receitas.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecipeController {

    @Autowired
    private final RecipeService recipeService;

    // private final String UPLOAD_DIR = "uploads/"; // Directorio donde se guardarán las imágenes

    @Autowired
    private RecipeController(RecipeService recipeService) {

        this.recipeService = recipeService;

    }

    @GetMapping( value = "/recetas/{id}")
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
    @GetMapping(value = "/recetas")
    public List<Recipe> listarRecetas(
            @RequestParam(defaultValue = "fechaDesc") String orden,
            @PageableDefault(size = 6) Pageable pageable) {
        Page<Recipe> pageRecetas = recipeService.listarRecetas(orden, pageable);
        return pageRecetas.getContent(); // Devuelve solo el contenido (las recetas)
    }

    @PostMapping(value = "/recetas/crear-receta", consumes = "application/json") // COMPROBAR BEN ESTE ENDPOINT
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Recipe crearReceta(@RequestBody Recipe receita, Authentication authentication) {
        receita.setAutor(authentication.getName()); //si no post meto autor User1, cambiao a User2, BEN!!
        //Deberai valiar o id do usario, agora mesmo o User2 pode facer post referenciando o User1(id usuario =1)
        return recipeService.crearReceta(receita);
    }

    // cambios a realziar para permitir a subida local de imaxes-> VOLVER A MIRAR
    /*
     * @PostMapping
     *
     * @PreAuthorize("hasRole('USER')")
     * public ResponseEntity<Receta> crearReceta(
     *
     * @RequestParam("nombre") String nombre,
     *
     * @RequestParam("ingredientes") String ingredientes,
     *
     * @RequestParam("elaboracion") String elaboracion,
     *
     * @RequestParam("imagen") MultipartFile imagen) throws IOException {
     *
     * // Guardar la imagen en el servidor
     * String imagenUrl = guardarImagen(imagen);
     *
     * Receta receta = new Receta();
     * receta.setNombre(nombre);
     * receta.setIngredientes(ingredientes);
     * receta.setElaboracion(elaboracion);
     * receta.setImagenUrl(imagenUrl);
     * Receta nuevaReceta = recetaService.guardarReceta(receta);
     *
     * return new ResponseEntity<>(nuevaReceta, HttpStatus.CREATED);
     * }
     *
     * private String guardarImagen(MultipartFile imagen) throws IOException {
     * if (imagen.isEmpty()) {
     * throw new IOException("No se seleccionó ninguna imagen");
     * }
     *
     * String nombreArchivo = imagen.getOriginalFilename();
     * String rutaCompleta = UPLOAD_DIR + nombreArchivo;
     *
     * File directorio = new File(UPLOAD_DIR);
     * if (!directorio.exists()) {
     * directorio.mkdirs(); // Crear el directorio si no existe
     * }
     *
     * File archivo = new File(rutaCompleta);
     * imagen.transferTo(archivo); // Guardar la imagen en el directorio
     *
     * return rutaCompleta; // Devolver la ruta de la imagen
     * }
     */

    // Otros métodos de la API (e.g., valorar recetas, eliminar recetas, actualizar
    // recetas)








}
