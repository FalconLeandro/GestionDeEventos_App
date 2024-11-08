package com.hotelArgos.gestiondeeventos.model;

public class EventoServiciosAdicionales {
    private Evento evento;
    private ServiciosAdicionales servicioAdicional;

    // Constructor, Getters y Setters

    public EventoServiciosAdicionales(Evento evento, ServiciosAdicionales servicioAdicional) {
        this.evento = evento;
        this.servicioAdicional = servicioAdicional;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public ServiciosAdicionales getServicioAdicional() {
        return servicioAdicional;
    }

    public void setServicioAdicional(ServiciosAdicionales servicioAdicional) {
        this.servicioAdicional = servicioAdicional;
    }

    @Override
    public String toString() {
        return "EventoServiciosAdicionales{" +
                "evento=" + evento +
                ", servicioAdicional=" + servicioAdicional +
                '}';
    }
}
