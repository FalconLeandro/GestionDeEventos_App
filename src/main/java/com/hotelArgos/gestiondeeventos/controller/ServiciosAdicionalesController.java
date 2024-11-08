package com.hotelArgos.gestiondeeventos.controller;

import com.hotelArgos.gestiondeeventos.dao.impl.ServiciosAdicionalesDAO;
import com.hotelArgos.gestiondeeventos.model.ServiciosAdicionales;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiciosAdicionalesController {

    private ServiciosAdicionalesDAO serviciosAdicionalesDAO;

    public ServiciosAdicionalesController(Connection connection) {
        this.serviciosAdicionalesDAO = new ServiciosAdicionalesDAO(connection);
    }

    public void crearServicioAdicional(ServiciosAdicionales servicio) {
        ServiciosAdicionales servicioAdicional = new ServiciosAdicionales(servicio.getNombre(), servicio.getDescripcion());
        try {
            serviciosAdicionalesDAO.crear(servicioAdicional);
        } catch (SQLException e) {
            e.printStackTrace();  // Manejo de excepciones
        }
    }

    public ServiciosAdicionales obtenerServicioAdicionalPorId(int id) {
        try {
            return serviciosAdicionalesDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ServiciosAdicionales> obtenerTodosLosServiciosAdicionales() {
        try {
            return serviciosAdicionalesDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();  // Retorna una lista vacía en caso de error
    }

    public void actualizarServicioAdicional(ServiciosAdicionales servicio) throws SQLException {
        serviciosAdicionalesDAO.actualizar(servicio);
    }

    public void eliminarServicioAdicional(int id) {
        try {
            serviciosAdicionalesDAO.eliminar(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}