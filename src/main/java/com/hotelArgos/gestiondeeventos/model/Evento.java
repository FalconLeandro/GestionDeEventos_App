package com.hotelArgos.gestiondeeventos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Evento {
    private int id;
    private String nombre;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int cantidadPersonas;
    private String observaciones;
    private Empresa empresa;
    private Salon salon;
    private FormatoSalon formatoSalon;
    private List<ServiciosAdicionales> serviciosAdicionales;
    private List<Catering> caterings;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String estado;

    // Constructor, Getters y Setters
    // Constructor con todos los atributos
    public Evento(int id, String nombre, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int cantidadPersonas, String observaciones, Empresa empresa, Salon salon, FormatoSalon formatoSalon, List<ServiciosAdicionales> serviciosAdicionales, List<Catering> caterings, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cantidadPersonas = cantidadPersonas;
        this.observaciones = observaciones;
        this.empresa = empresa;
        this.salon = salon;
        this.formatoSalon = formatoSalon;
        this.serviciosAdicionales = serviciosAdicionales;
        this.caterings = caterings;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.estado = "VIGENTE";
    }
    // Constructor sin id
    public Evento(String nombre, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int cantidadPersonas, String observaciones, Empresa empresa, Salon salon, FormatoSalon formatoSalon, List<ServiciosAdicionales> serviciosAdicionales, List<Catering> caterings, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cantidadPersonas = cantidadPersonas;
        this.observaciones = observaciones;
        this.empresa = empresa;
        this.salon = salon;
        this.formatoSalon = formatoSalon;
        this.serviciosAdicionales = serviciosAdicionales;
        this.caterings = caterings;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.estado = "VIGENTE";
    }

    // Constructor que acepta solo el ID del evento
    public Evento(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void cancelarEvento() {
        this.estado = "cancelado";
    }

    public void finalizarEvento() {
        this.estado = "finalizado";
    }

    public void setEstadoVigente() {
        this.estado = "vigente";
    }

    public List<ServiciosAdicionales> getServiciosAdicionales() {
        return serviciosAdicionales;
    }

    public void setServiciosAdicionales(List<ServiciosAdicionales> serviciosAdicionales) {
        this.serviciosAdicionales = serviciosAdicionales;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(int cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Salon getSalon() {
        return salon;
    }

    public void setSalon(Salon salon) {
        this.salon = salon;
    }

    public FormatoSalon getFormatoSalon() {
        return formatoSalon;
    }

    public void setFormatoSalon(FormatoSalon formatoSalon) {
        this.formatoSalon = formatoSalon;
    }

    public List<Catering> getCaterings() {
        return caterings;
    }

    public void setCaterings(List<Catering> caterings) {
        this.caterings = caterings;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Evento: " +
                "id: " + id +
                ", nombre: " + nombre +
                ", fecha: " + fecha +
                ", hora de inicio: " + horaInicio +
                ", hora de finalizacion: " + horaFin +
                ", cantidad de personas: " + cantidadPersonas +
                ", observaciones: " + observaciones +
                ", empresa: " + empresa +
                ", salon: " + salon +
                ", formato de salon: " + formatoSalon +
                ", servicios adicionales: " + serviciosAdicionales +
                ", caterings: " + caterings +
                ", createdAt: " + createdAt +
                ", updatedAt: " + updatedAt +
                ", estado: " + estado;
    }


}

