package com.eloi_daw_receitas.receitas.service;

import com.eloi_daw_receitas.receitas.model.Usuario;
import com.eloi_daw_receitas.receitas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {


    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository){

        this.usuarioRepository = usuarioRepository;
    }

    //Método para rexistrar un usuario coa contrasinal encriptada
    public Usuario registrarUsuario(Usuario usuario) {

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    //@Secured("ROLE_ADMIN")
    public void eliminarUsuario(String nickname) {
        // Lógica para eliminar un usuario solo para admins
        //non se implemente, fárase desde a bbdd
    }

}
