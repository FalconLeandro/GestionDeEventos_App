package com.hotelArgos.gestiondeeventos.dao.impl;

import com.hotelArgos.gestiondeeventos.dao.GenericDAO;
import com.hotelArgos.gestiondeeventos.model.EventoServiciosAdicionales;
import com.hotelArgos.gestiondeeventos.model.ServiciosAdicionales;
import com.hotelArgos.gestiondeeventos.model.Evento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoServiciosAdicionalesDAO implements GenericDAO<EventoServiciosAdicionales> {

    private Connection connection;

    public EventoServiciosAdicionalesDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void crear(EventoServiciosAdicionales eventoServiciosAdicionales) throws SQLException {
        String query = "INSERT INTO evento_servicios_adicionales (evento_id, servicio_adicional_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventoServiciosAdicionales.getEvento().getId());
            ps.setInt(2, eventoServiciosAdicionales.getServicioAdicional().getId());
            ps.executeUpdate();
        }
    }

    @Override
    public EventoServiciosAdicionales obtenerPorId(int id) {
        // No aplicable para tablas intermedias sin una clave primaria.
        return null;
    }

    @Override
    public List<EventoServiciosAdicionales> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM evento_servicios_adicionales";
        List<EventoServiciosAdicionales> eventoServiciosAdicionalesList = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Evento evento = new Evento(rs.getInt("evento_id"));
                ServiciosAdicionales servicioAdicional = new ServiciosAdicionales(rs.getInt("servicio_adicional_id"), "", "");

                eventoServiciosAdicionalesList.add(new EventoServiciosAdicionales(evento, servicioAdicional));
            }
        }
        return eventoServiciosAdicionalesList;
    }

    @Override
    public void actualizar(EventoServiciosAdicionales eventoServiciosAdicionales) {
        // No aplicable para tablas intermedias
    }

    @Override
    public void eliminarPorIds(int eventoId, int servicioAdicionalId) throws SQLException {
        String query = "DELETE FROM evento_servicios_adicionales WHERE evento_id = ? AND servicio_adicional_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventoId);
            ps.setInt(2, servicioAdicionalId);
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) {
        // No aplicable para tablas intermedias
    }
}