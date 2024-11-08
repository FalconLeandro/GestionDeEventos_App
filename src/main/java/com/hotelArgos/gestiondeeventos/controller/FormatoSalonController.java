package com.hotelArgos.gestiondeeventos.controller;

import com.hotelArgos.gestiondeeventos.dao.impl.FormatoSalonDAO;
import com.hotelArgos.gestiondeeventos.model.FormatoSalon;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FormatoSalonController {

    private FormatoSalonDAO formatoSalonDAO;

    public FormatoSalonController(Connection connection) {
        this.formatoSalonDAO = new FormatoSalonDAO(connection);
    }

    public void crearFormatoSalon(FormatoSalon formatoSalon) {
        try {
            formatoSalonDAO.crear(formatoSalon);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public FormatoSalon obtenerFormatoSalonPorId(int id) {
        try {
            return formatoSalonDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FormatoSalon> obtenerTodosLosFormatosSalon() {
        try {
            return formatoSalonDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();  // Retorna una lista vacía en caso de error
    }

    public void actualizarFormatoSalon(FormatoSalon formatoSalon) {
        try {
            formatoSalonDAO.actualizar(formatoSalon);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarFormatoSalon(int id) {
        try {
            formatoSalonDAO.eliminar(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}