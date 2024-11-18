package com.hotelArgos.gestiondeeventos.model;

public class Salon {
    private int id;
    private String nombre;
    private int capacidad;
    private String descripcion;

    // Constructor, Getters y Setters


    public Salon(int id, String nombre, int capacidad, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.descripcion = descripcion;
    }

    public Salon(String nombre, int capacidad, String descripcion) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.descripcion = descripcion;
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

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Id: " + id +
                ", Nombre: " + nombre +
                ", Capacidad: " + capacidad +
                ", Descripción: " + descripcion;
    }
}


