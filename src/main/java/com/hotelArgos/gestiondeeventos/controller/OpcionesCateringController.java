package com.hotelArgos.gestiondeeventos.controller;

import com.hotelArgos.gestiondeeventos.dao.impl.OpcionesCateringDAO;
import com.hotelArgos.gestiondeeventos.model.OpcionesCatering;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OpcionesCateringController {

    private OpcionesCateringDAO opcionesCateringDAO;

    public OpcionesCateringController(Connection connection) {
        this.opcionesCateringDAO = new OpcionesCateringDAO(connection);
    }

    public void crearOpcionCatering(OpcionesCatering opcionCatering) {
        try {
            opcionesCateringDAO.crear(opcionCatering);
            System.out.println("Opción de Catering creada exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear la opción de catering: " + e.getMessage());
        }
    }

    public OpcionesCatering obtenerOpcionCateringPorId(int id) {
        try {
            return opcionesCateringDAO.obtenerPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al obtener la opción de catering: " + e.getMessage());
        }
        return null;
    }

    public List<OpcionesCatering> obtenerTodasLasOpcionesCatering() {
        try {
            return opcionesCateringDAO.obtenerTodos();
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de opciones de catering: " + e.getMessage());
        }
        return new ArrayList<>();  // Retorna una lista vacía en caso de error
    }

    public void actualizarOpcionCatering(OpcionesCatering opcionCatering) {
        try {
            opcionesCateringDAO.actualizar(opcionCatering);
            System.out.println("Opción de Catering actualizada exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar la opción de catering: " + e.getMessage());
        }
    }

    public void eliminarOpcionCatering(int id) {
        try {
            opcionesCateringDAO.eliminar(id);
            System.out.println("Opción de Catering eliminada exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar la opción de catering: " + e.getMessage());
        }
    }
}