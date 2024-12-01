package com.eloi_daw_receitas.receitas.repository;

import com.eloi_daw_receitas.receitas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNickname(String nickname);
}
