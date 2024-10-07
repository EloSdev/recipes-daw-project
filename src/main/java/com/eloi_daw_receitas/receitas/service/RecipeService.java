package com.eloi_daw_receitas.receitas.service;


import com.eloi_daw_receitas.receitas.model.Recipe;
import com.eloi_daw_receitas.receitas.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Recipe crearReceta(Recipe receta) {
        return recipeRepository.save(receta);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public void eliminarReceta(Long id) {
        // Lógica para eliminar la receta
    }



}
