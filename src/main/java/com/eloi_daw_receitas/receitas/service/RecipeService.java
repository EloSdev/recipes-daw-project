package com.eloi_daw_receitas.receitas.service;

import com.eloi_daw_receitas.receitas.exception.ResourceNotFoundException;
import com.eloi_daw_receitas.receitas.model.Recipe;
import com.eloi_daw_receitas.receitas.model.Usuario;
import com.eloi_daw_receitas.receitas.model.UsuarioLikeRecipe;
import com.eloi_daw_receitas.receitas.repository.RecipeRepository;
import com.eloi_daw_receitas.receitas.repository.UsuarioLikeRecipeRepository;
import com.eloi_daw_receitas.receitas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private final RecipeRepository recipeRepository;

    @Autowired
    private UsuarioLikeRecipeRepository userLikeRecipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {

        this.recipeRepository = recipeRepository;
    }

    public Recipe obtenerRecetaPorId(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public Page<Recipe> listarRecetas(String orden, Pageable pageable) {
        switch (orden) {
            case "fechaAsc":
                return recipeRepository.findAllByOrderByFechaAsc(pageable);
            case "fechaDesc":
                return recipeRepository.findAllByOrderByFechaDesc(pageable);
            case "likeAsc":
                return recipeRepository.findAllByOrderByLikesAsc(pageable);
            case "likeDesc":
                return recipeRepository.findAllByOrderByLikesDesc(pageable);
            default:
                return recipeRepository.findAll(pageable);
        }
    }

    // Método para buscar receitas por nome con paxinación e orden
    public Page<Recipe> buscarRecetasPorNombre(String nombre, String orden, Pageable pageable) {

        return recipeRepository.findByNombreContainingIgnoreCase(nombre, pageable);
    }

    // Método para incrementar os likes dunha receita
    public Recipe incrementarLikes(Long recetaId, String username) {
        Recipe receta = recipeRepository.findById(recetaId)
                .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada"));

        Usuario usuario = usuarioRepository.findByNickname(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Optional<UsuarioLikeRecipe> likeExistente = userLikeRecipeRepository.findByUsuarioAndReceta(usuario, receta);
        if (likeExistente.isPresent()) {
            throw new IllegalStateException("Ya has dado like a esta receta");
        }

        receta.setLikes(receta.getLikes() + 1);
        recipeRepository.save(receta);

        UsuarioLikeRecipe userLikeRecipe = new UsuarioLikeRecipe();
        userLikeRecipe.setUsuario(usuario);
        userLikeRecipe.setReceta(receta);
        userLikeRecipeRepository.save(userLikeRecipe);

        return receta;
    }

    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Recipe crearReceta(Recipe receta) {
        return recipeRepository.save(receta);
    }

    public List<Recipe> listarTodasLasRecetas() {
        return recipeRepository.findAll();
    }

    public List<Recipe> buscarRecetasPorNombre(String nombre) {
        return recipeRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Método para obter as receitas que o usuario xa dou like
    public List<Long> obtenerRecetasVotadasPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByNickname(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<UsuarioLikeRecipe> likes = userLikeRecipeRepository.findByUsuario(usuario);
        List<Long> recetaIds = likes.stream().map(like -> like.getReceta().getId()).collect(Collectors.toList());
        
        return recetaIds;
    }

    // @PreAuthorize("hasRole('ADMIN')")
    public void eliminarReceta(Long id) {
        // Lógica para eliminar receita
        // non se vai implementar, as receitas eliminaránse desde a bbdd
    }

}
