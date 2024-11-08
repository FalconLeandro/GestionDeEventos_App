package com.hotelArgos.gestiondeeventos.model;

import java.util.ArrayList;
import java.util.List;

public class Catering {
    private int id;
    private String tipoCatering;
    private String observaciones;
    private List<OpcionesCatering> opcionesCatering;

    // Constructor, Getters y Setters

    public Catering(int id, String tipoCatering, String observaciones, List<OpcionesCatering> opcionesCatering) {
        this.id = id;
        this.tipoCatering = tipoCatering;
        this.observaciones = observaciones;
        this.opcionesCatering = opcionesCatering != null ? opcionesCatering : new ArrayList<>();
    }

    public Catering(String tipoCatering, String observaciones) {
        this.tipoCatering = tipoCatering;
        this.observaciones = observaciones;
        this.opcionesCatering = new ArrayList<>();
    }

    public Catering(String tipoCatering, String observaciones, List<OpcionesCatering> opcionesCatering) {
        this.tipoCatering = tipoCatering;
        this.observaciones = observaciones;
        this.opcionesCatering = opcionesCatering != null ? opcionesCatering : new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoCatering() {
        return tipoCatering;
    }

    public void setTipoCatering(String tipoCatering) {
        this.tipoCatering = tipoCatering;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<OpcionesCatering> getOpcionesCatering() {
        return opcionesCatering;
    }

    public void setOpcionesCatering(List<OpcionesCatering> opcionesCatering) {
        this.opcionesCatering = opcionesCatering;
    }

    @Override
    public String toString() {
        return "Catering: " +
                "id: " + id +
                ", tipoCatering: " + tipoCatering +
                ", observaciones: " + observaciones +
                ", opcionesCatering: " + opcionesCatering;
    }
}