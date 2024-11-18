package com.hotelArgos.gestiondeeventos.model;

public class FormatoSalon {
    private int id;
    private String nombre;
    private String descripcion;
    private String imagenFormatoSalon;

    // Constructor, Getters y Setters


    public FormatoSalon(int id, String nombre, String descripcion, String imagenFormatoSalon) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenFormatoSalon = imagenFormatoSalon;
    }

    public FormatoSalon(String nombre, String descripcion, String imagenFormatoSalon) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenFormatoSalon = imagenFormatoSalon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenFormatoSalon() {
        return imagenFormatoSalon;
    }

    public void setImagenFormatoSalon(String imagenFormatoSalon) {
        this.imagenFormatoSalon = imagenFormatoSalon;
    }

    @Override
    public String toString() {
        return "Id:" + id +
                ", Nombre:" + nombre +
                ", Descripción:" + descripcion +
                ", Imagen:" + imagenFormatoSalon;
    }
}


