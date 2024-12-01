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
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private UsuarioController(UsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    // Endpoint para rexistrar un novo usuario
    @PostMapping(value = "/usuarios", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            usuarioService.registrarUsuario(usuario);
            log.info("Usuario {} registrado con éxito", usuario.getNickname());
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (Exception e) {
            log.info("Error al registrar un usuario");
            return ResponseEntity.badRequest().body("Error al registrar usuario: " + e.getMessage());
        }
    }

    //Endpoint que devolve o usuario logueado
    @GetMapping("/usuarios")
    public Map<String, String> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();
        log.info("Usuario {} logueado", nickname);
        return Map.of("username", nickname); 
    }

    //Endpoint  para comprobar si o usuario esta logueado e poder actualizar o html cos botóns correspondentes
    @GetMapping("/usuarios/autenticado")
    public ResponseEntity<?> obtenerUsuarioAutenticado(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("Usuario {} autenticado", authentication.getName());
            return ResponseEntity.ok(authentication.getName());
        }
        log.info("Usuario no antenticado");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
    }
}
