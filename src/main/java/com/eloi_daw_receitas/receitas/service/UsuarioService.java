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

    public UsuarioService(UsuarioRepository usuarioRepository){

        this.usuarioRepository = usuarioRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;  // Inyecta el encoder de contraseñas

    public Usuario registrarUsuario(Usuario usuario) {
        // Cifrar la contraseña antes de almacenarla
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    //de momento non habilito esta seguirdade por metodos
    //@Secured("ROLE_ADMIN")
    public void eliminarUsuario(String nickname) {
        // Lógica para eliminar un usuario solo para admins
    }

    /*
    //metodo para login
    //creo que de mmto innecesario pq o login vaino xestionar SecurityConfig....imos vendo!!
    public boolean login(String nickname, String password) {
        Usuario usuario = usuarioRepository.findByNickname(nickname);
        if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
            return true;
        }
        return false;
    }*/


}
