package com.eloi_daw_receitas.receitas.controller;

import com.eloi_daw_receitas.receitas.model.Usuario;
import com.eloi_daw_receitas.receitas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api") //check a ruta!!!!!
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioController(UsuarioService usuarioService){

        this.usuarioService = usuarioService;
    }

    // Endpoint para registrar un nuevo usuario
    @PostMapping(value ="/usuarios" , consumes = "application/json")//ver si e necesario este valor
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar usuario: " + e.getMessage());
        }
    }



    //metodo para valida introducir login--> o login debe validarse a traves de Security Config (Spring Security)
    /*
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario user) {
        boolean isAuthenticated = usuarioService.login(user.getNickname(), user.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }*/

    //crear método para ver todolos usuarios? -> iso melhor na bbdd non??



}
