package com.eloi_daw_receitas.receitas.repository;

import com.eloi_daw_receitas.receitas.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // Métodos para ordenar recetas por fechas y  likes
    Page<Recipe> findAllByOrderByFechaAsc(Pageable pageable);
    Page<Recipe> findAllByOrderByFechaDesc(Pageable pageable);
    Page<Recipe> findAllByOrderByLikesAsc(Pageable pageable);
    Page<Recipe> findAllByOrderByLikesDesc(Pageable pageable);


}
