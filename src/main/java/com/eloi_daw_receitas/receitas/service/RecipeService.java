package com.eloi_daw_receitas.receitas.service;


import com.eloi_daw_receitas.receitas.exception.ResourceNotFoundException;
import com.eloi_daw_receitas.receitas.model.Recipe;
import com.eloi_daw_receitas.receitas.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    // Método para listar recetas con orden y paginación
    /*public Page<Recipe> listarRecetas(String orden, Pageable pageable) {
        // Lógica para ordenar las recetas según el valor del parámetro 'orden'
        return recipeRepository.findAll(pageable); // Reemplaza con tu lógica de orden si es necesario
    }*/

    // Método para buscar recetas por nombre con paginación y orden
    public Page<Recipe> buscarRecetasPorNombre(String nombre, String orden, Pageable pageable) {
        // Busca recetas cuyo nombre contenga el término de búsqueda (ignora mayúsculas)
        return recipeRepository.findByNombreContainingIgnoreCase(nombre, pageable); // Reemplaza con tu lógica de orden si es necesario
    }

    // Método para incrementar los likes de una receta
    public Recipe incrementarLikes(Long recetaId) {
        Recipe receta = recipeRepository.findById(recetaId)
                .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada"));
        receta.setLikes(receta.getLikes() + 1);
        return recipeRepository.save(receta); // Asegúrate de que se está guardando correctamente
    }


    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Recipe crearReceta(Recipe receta) {
        return recipeRepository.save(receta);
    }

    // Nuevo método para listar todas las recetas
    public List<Recipe> listarTodasLasRecetas() {
        return recipeRepository.findAll(); // Asumiendo que este método está disponible en tu repositorio
    }


    // Método para buscar recetas por nombre
    public List<Recipe> buscarRecetasPorNombre(String nombre) {
        return recipeRepository.findByNombreContainingIgnoreCase(nombre);
    }



    //@PreAuthorize("hasRole('ADMIN')")
    public void eliminarReceta(Long id) {
        // Lógica para eliminar la receta
    }



}
