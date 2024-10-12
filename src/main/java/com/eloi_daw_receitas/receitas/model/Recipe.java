package com.eloi_daw_receitas.receitas.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(length = 1000, nullable = false)
    private String ingredientes;
    @Column(length = 1000, nullable = false)
    private String elaboracion;
    @Column(nullable = false)
    private String autor;
    @Column
    private int likes;
    @Column(name = "imagen_url")
    private String imagenUrl;  // Nuevo campo
    @Column(name = "fecha", nullable = false) //, columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp // Esto asegura que Hibernate ponga la fecha actual automáticamente si no se especifica.
    private Date fecha;


    //Al incluir la dependencia jpa no seria necesario mapear esta relacion pero la incluyo igual a modo educativo
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false) //usuario_id referencia a la columna en la bbdd
    private Usuario usuario; // Relación muchos a uno



    //constructor
    public Recipe(){}

    //Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getElaboracion() {
        return elaboracion;
    }

    public void setElaboracion(String elaboracion) {
        this.elaboracion = elaboracion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}


