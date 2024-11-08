package com.hotelArgos.gestiondeeventos.controller;

import com.hotelArgos.gestiondeeventos.dao.impl.SalonDAO;
import com.hotelArgos.gestiondeeventos.model.Salon;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalonController {

    private SalonDAO salonDAO;

    public SalonController(Connection connection) {
        this.salonDAO = new SalonDAO(connection);
    }

    public void crearSalon(Salon salon) {
        try {
            salonDAO.crear(salon);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Salon obtenerSalonPorId(int id) {
        try {
            return salonDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Salon> obtenerTodosLosSalones() {
        try {
            return salonDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();  // Retorna una lista vacía en caso de error
    }

    public void actualizarSalon(Salon salon) {
        try {
            salonDAO.actualizar(salon);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarSalon(int id) {
        try {
            salonDAO.eliminar(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
