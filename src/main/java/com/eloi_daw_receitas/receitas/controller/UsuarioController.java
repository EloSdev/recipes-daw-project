package com.eloi_daw_receitas.receitas.controller;

import com.eloi_daw_receitas.receitas.model.Usuario;
import com.eloi_daw_receitas.receitas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioController(UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    // Endpoint para rexistrar un novo usuario
    @PostMapping(value = "/usuarios", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar usuario: " + e.getMessage());
        }
    }

    //devolucion do usuario logueado
    @GetMapping("/usuarios")
    public Map<String, String> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();
        return Map.of("username", nickname); // Devolvemos el nombre en JSON
    }


    //METODO PARA SABER SI USUARIO ESTA AUTENTICADO PARA POR NICKNAME E LOGOUT EN HOME,ABOUT E RECETAS
    @GetMapping("/usuarios/autenticado")
    public ResponseEntity<?> obtenerUsuarioAutenticado(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(authentication.getName());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
    }
}
