package com.hotelArgos.gestiondeeventos.controller;

import com.hotelArgos.gestiondeeventos.dao.impl.EventoServiciosAdicionalesDAO;
import com.hotelArgos.gestiondeeventos.model.EventoServiciosAdicionales;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventoServiciosAdicionalesController {

    private EventoServiciosAdicionalesDAO eventoServiciosAdicionalesDAO;

    public EventoServiciosAdicionalesController(Connection connection) {
        this.eventoServiciosAdicionalesDAO = new EventoServiciosAdicionalesDAO(connection);
    }

    public void crearEventoServiciosAdicionales(EventoServiciosAdicionales eventoServiciosAdicionales) {
        try {
            eventoServiciosAdicionalesDAO.crear(eventoServiciosAdicionales);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public EventoServiciosAdicionales obtenerEventoServiciosAdicionalesPorId(int id) {
        return eventoServiciosAdicionalesDAO.obtenerPorId(id);
    }

    public List<EventoServiciosAdicionales> obtenerTodosLosEventoServiciosAdicionales() {
        try {
            return eventoServiciosAdicionalesDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();  // Retorna una lista vacía en caso de error
    }

    public void actualizarEventoServiciosAdicionales(EventoServiciosAdicionales eventoServiciosAdicionales) {
        eventoServiciosAdicionalesDAO.actualizar(eventoServiciosAdicionales);
    }

    public void eliminarEventoServiciosAdicionales(int eventoId, int servicioAdicionalId) {
        try {
            eventoServiciosAdicionalesDAO.eliminarPorIds(eventoId, servicioAdicionalId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}