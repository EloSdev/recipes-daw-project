package com.eloi_daw_receitas.receitas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Autowired
        private CustomUserDetailsService customUserDetailsService; // Inyectamos el servicio


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests((requests) -> requests
                            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                            .requestMatchers("/", "/index.html", "/public-views/**","/api/usuarios","/api/usuarios/**").permitAll()
                            .requestMatchers("/api/recetas/crear-receta", "/home.html").authenticated()  // Requiere autenticación para crear receta
                            .requestMatchers("/api/recetas", "/api/recetas/**").permitAll()  // Permitir acceso a /api/recetas y /api/recetas/{id}

                            .anyRequest().authenticated()
                    )
                    .formLogin((form) -> form
                            .loginPage("/public-views/login.html")
                            .loginProcessingUrl("/login")
                            .defaultSuccessUrl("/home.html", true) // URL a la que redirigir tras el login exitoso
                            .permitAll()
                    )
                    .logout(logout ->
                            logout
                                    .logoutUrl("/logout") // Configura la URL para logout
                                    .logoutSuccessUrl("/index.html") // Redirige a index.html tras cerrar sesión
                                    .permitAll()
                    )




            //  .httpBasic(Customizer.withDefaults())
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

        /*
        @Bean
        public UserDetailsService userDetailsService() {

            return new CustomUserDetailsService();
        }*/

        /*
        @Bean
        public UserDetailsService userDetailsService() {
            UserDetails user =
                    (User.withDefaultPasswordEncoder()
                            .username("user")
                            .password("password")
                            .roles("USER")
                            .build());


            return new InMemoryUserDetailsManager(user);
        }*/

    }
