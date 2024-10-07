package com.eloi_daw_receitas.receitas.model;


import jakarta.persistence.*;
import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Rol rol; // Enum con valores USER, ADMIN

    //Al incluir la dependencia jpa no seria necesario mapear esta relacion pero la incluyo igual a modo educativo
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true) //usuario referencia a la tabla en la bbdd
    private List<Recipe> recipes; // Relación uno a muchos

    //constructor
    public Usuario(){};


    //Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}


