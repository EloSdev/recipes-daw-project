package com.eloi_daw_receitas.receitas.controller;

import com.eloi_daw_receitas.receitas.model.Recipe;
import com.eloi_daw_receitas.receitas.model.Usuario;
import com.eloi_daw_receitas.receitas.repository.UsuarioRepository;
import com.eloi_daw_receitas.receitas.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestController
@RequestMapping("/api")
public class RecipeController {

    @Autowired
    private final RecipeService recipeService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private RecipeController(RecipeService recipeService) {

        this.recipeService = recipeService;

    }

    //Endpoint para obter a receta polo seu id
    @SuppressWarnings("unused")
    @GetMapping(value = "/recetas/{id}")
    public ResponseEntity<Recipe> obtenerRecetaPorId(@PathVariable Long id) {
        Recipe receita = recipeService.obtenerRecetaPorId(id);
        String nome = receita.getNombre();
        if (receita != null) {
            log.info("receta - {}", nome);
            return ResponseEntity.ok(receita);
        } else {
            log.error("Receta con id {} no encontrada" , id);
            return ResponseEntity.notFound().build();
        }
    }

    //Endpoint que devolve tódalas receitas con valor por defecto as máis recentes 
    //Devólvense paxinadas de 6 e cun párametro search opcional a módo de buscador
    @GetMapping(value = "/recetas")
    public Page<Recipe> listarRecetas(
            @RequestParam(defaultValue = "fechaDesc") String orden,
            @RequestParam(required = false) String search, 
            @PageableDefault(size = 6) Pageable pageable) {

        if (search != null && !search.isEmpty()) {
            log.info("Recetas filtradas");
            return recipeService.buscarRecetasPorNombre(search, orden, pageable);
        } else {
            log.info("Recetas listadas y paginadas");
            return recipeService.listarRecetas(orden, pageable);
        }
    }

    //Endpoint para listar tódalas receitas (sen paxinación)
    @GetMapping("/recetas/all")
    public List<Recipe> listarTodasLasRecetas() {
        log.info("Obteniendo tódas las recetas");
        return recipeService.listarTodasLasRecetas();
    }

    //Endpoint para buscar receitas no search
    @GetMapping(value = "/recetas/search")
    public ResponseEntity<List<Recipe>> buscarRecetasPorNombre(@RequestParam String nombre) {
        List<Recipe> recetas = recipeService.buscarRecetasPorNombre(nombre);
        log.info("Filtrando recetas");
        return ResponseEntity.ok(recetas); 
    }

    // Endpoint para incrementar o número de likes
    @PostMapping("recetas/{recetaId}/like")
    public ResponseEntity<Recipe> incrementarLike(@PathVariable Long recetaId, Authentication authentication) {
        String username = authentication.getName(); 
        try {
            Recipe recetaActualizada = recipeService.incrementarLikes(recetaId, username);
            log.info("El usuario {} ha dado like a la receta {}", recetaId, username);
            return ResponseEntity.ok(recetaActualizada);
        } catch (IllegalStateException e) {
            log.error("Error mientras el usuario {} daba like a la receta {}", username, recetaId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); 
        }
    }

    // Enpoint que devolve as receitas xa votadas por usuario
    @GetMapping("/votadas/{username}")
    public ResponseEntity<List<Long>> obtenerRecetasVotadas(@PathVariable String username) {
        List<Long> recetaIds = recipeService.obtenerRecetasVotadasPorUsuario(username);
        log.info("Receta ya votadas por el usuario {}", username);
        return ResponseEntity.ok(recetaIds);
    }

    //Endpoint para crear receitas
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
                    String rutaImagen = "src/main/resources/static/images/recipes/" + nuevoNombreArchivo;
                    File file = new File(rutaImagen);

                    // Escribir a imaxe na ruta especificada
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(imagen.getBytes());
                    }
                    // Almacenar a URL relativa da imaxe na base de datos
                    String imagenUrl = "/images/recipes/" + nuevoNombreArchivo;
                    nuevaReceta.setImagenUrl(imagenUrl);

                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null);
                }
            }
            recipeService.crearReceta(nuevaReceta);
            log.info("El usuario {} ha creado la receta con nombre {}", autor, nombre);
            return new ResponseEntity<>(nuevaReceta, HttpStatus.CREATED);

        } else {
            log.error("Error mientras el usuario {} creaba una receta de nombre {}", autor, username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}