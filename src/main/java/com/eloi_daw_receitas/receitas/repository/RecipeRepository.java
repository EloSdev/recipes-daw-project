package com.eloi_daw_receitas.receitas.repository;

import com.eloi_daw_receitas.receitas.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByNombreContainingIgnoreCase(String nombre);

    Page<Recipe> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    
    Page<Recipe> findAllByOrderByFechaAsc(Pageable pageable);
    Page<Recipe> findAllByOrderByFechaDesc(Pageable pageable);
    Page<Recipe> findAllByOrderByLikesAsc(Pageable pageable);
    Page<Recipe> findAllByOrderByLikesDesc(Pageable pageable);


}
