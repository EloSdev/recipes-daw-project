package com.eloi_daw_receitas.receitas.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Autowired
        private CustomUserDetailsService customUserDetailsService;


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests((requests) -> requests
                            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                            .requestMatchers("/", "/index.html", "/public-views/**","/api/usuarios","/api/usuarios/**").permitAll()
                            .requestMatchers("/api/recetas/subir-receta", "/home.html").authenticated()  // Require autenticación para acceder o home e poder crear receita e
                            .requestMatchers("/api/recetas", "/api/recetas/search", "/api/recetas/**", "/api/usuarios/autenticado").permitAll()

                            .anyRequest().authenticated()
                    )
                    .formLogin((form) -> form
                            .loginPage("/public-views/login.html")
                            .loginProcessingUrl("/login")
                            .defaultSuccessUrl("/home.html", true)
                            .failureHandler((request, response, exception) -> {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //configurar para que devolva 401 e asi manexar o erro de credenciais incorrectas; doutra forma pr defecto SpringSecurity devolve erro na url
                                response.getWriter().write("Invalid credentials");
                            })
                            .permitAll()
                    )
                    .logout(logout ->
                            logout
                                    .logoutUrl("/logout") // Configura a URL para logout
                                    .logoutSuccessUrl("/index.html")
                                    .permitAll()
                    )

                    .csrf(csrf->csrf.disable());


            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authManager(HttpSecurity http) throws Exception {
            AuthenticationManagerBuilder authenticationManagerBuilder =
                    http.getSharedObject(AuthenticationManagerBuilder.class);
            authenticationManagerBuilder.userDetailsService(customUserDetailsService)
                    .passwordEncoder(passwordEncoder());
            return authenticationManagerBuilder.build();
        }

    }
