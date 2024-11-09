package com.eloi_daw_receitas.receitas.service;


import com.eloi_daw_receitas.receitas.exception.ResourceNotFoundException;
import com.eloi_daw_receitas.receitas.model.Recipe;
import com.eloi_daw_receitas.receitas.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RecipeService {

    @Autowired
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository){

        this.recipeRepository= recipeRepository;
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
    public Recipe incrementarLikes(Long recetaId) {
        Recipe receta = recipeRepository.findById(recetaId)
                .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada"));
        receta.setLikes(receta.getLikes() + 1);
        return recipeRepository.save(receta);
    }

    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Recipe crearReceta(Recipe receta) {
        return recipeRepository.save(receta);
    }

    public List<Recipe> listarTodasLasRecetas() {
        return recipeRepository.findAll();
    }


    public List<Recipe> buscarRecetasPorNombre(String nombre) {
        return recipeRepository.findByNombreContainingIgnoreCase(nombre);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public void eliminarReceta(Long id) {
        // Lógica para eliminar receita
        //non se vai implementar, as receitas eliminaránse desde a bbdd
    }



}
