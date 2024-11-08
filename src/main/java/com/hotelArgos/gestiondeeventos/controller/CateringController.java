package com.hotelArgos.gestiondeeventos.controller;

import com.hotelArgos.gestiondeeventos.dao.impl.CateringDAO;
import com.hotelArgos.gestiondeeventos.model.Catering;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CateringController {

    private CateringDAO cateringDAO;

    public CateringController(Connection connection) {
        this.cateringDAO = new CateringDAO(connection);
    }

    public void crearCatering(Catering catering) {
        try {
            cateringDAO.crear(catering);
            System.out.println("Catering creado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear el catering: " + e.getMessage());
        }
    }

    public Catering obtenerCateringPorId(int id) {
        try {
            return cateringDAO.obtenerPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al obtener el catering: " + e.getMessage());
        }
        return null;
    }

    public List<Catering> obtenerTodosLosCaterings() {
        try {
            return cateringDAO.obtenerTodos();
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de catering: " + e.getMessage());
        }
        return new ArrayList<>();  // Retorna una lista vacía en caso de error
    }

    public void actualizarCatering(Catering catering) {
        try {
            cateringDAO.actualizar(catering);
            System.out.println("Catering actualizado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar el catering: " + e.getMessage());
        }
    }

    public void eliminarCatering(int id) {
        try {
            cateringDAO.eliminar(id);
            System.out.println("Catering eliminado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar el catering: " + e.getMessage());
        }
    }
}