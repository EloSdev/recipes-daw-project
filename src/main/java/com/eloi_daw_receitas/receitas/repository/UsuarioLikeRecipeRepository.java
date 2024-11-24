package com.eloi_daw_receitas.receitas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eloi_daw_receitas.receitas.model.Recipe;
import com.eloi_daw_receitas.receitas.model.Usuario;
import com.eloi_daw_receitas.receitas.model.UsuarioLikeRecipe;

public interface UsuarioLikeRecipeRepository extends JpaRepository<UsuarioLikeRecipe, Long> {

    Optional<UsuarioLikeRecipe> findByUsuarioAndReceta(Usuario usuario, Recipe receta);

    List<UsuarioLikeRecipe> findByUsuario(Usuario usuario);
}
