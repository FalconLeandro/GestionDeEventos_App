package com.hotelArgos.gestiondeeventos.dao.impl;

import com.hotelArgos.gestiondeeventos.dao.GenericDAO;
import com.hotelArgos.gestiondeeventos.model.ServiciosAdicionales;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiciosAdicionalesDAO implements GenericDAO<ServiciosAdicionales> {

    private Connection connection;

    public ServiciosAdicionalesDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void crear(ServiciosAdicionales servicioAdicional) throws SQLException {
        String query = "INSERT INTO servicios_adicionales (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, servicioAdicional.getNombre());
            ps.setString(2, servicioAdicional.getDescripcion());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    servicioAdicional.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public ServiciosAdicionales obtenerPorId(int id) throws SQLException {
        String query = "SELECT * FROM servicios_adicionales WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ServiciosAdicionales(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    );
                }
            }
        }
        return null;  // Si no se encuentra el servicio adicional
    }

    @Override
    public List<ServiciosAdicionales> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM servicios_adicionales";
        List<ServiciosAdicionales> serviciosAdicionalesList = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                serviciosAdicionalesList.add(new ServiciosAdicionales(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }
        }
        return serviciosAdicionalesList;
    }

    @Override
    public void actualizar(ServiciosAdicionales servicioAdicional) throws SQLException {
        String query = "UPDATE servicios_adicionales SET nombre = ?, descripcion = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, servicioAdicional.getNombre());
            ps.setString(2, servicioAdicional.getDescripcion());
            ps.setInt(3, servicioAdicional.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM servicios_adicionales WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminarPorIds(int id1, int id2) throws Exception {
        // Implementar si es necesario
    }

    // Método para asociar un servicio adicional a un evento
    public void asociarServicioAEvento(int eventoId, int servicioId) throws SQLException {
        String query = "INSERT INTO evento_servicios_adicionales (evento_id, servicio_adicional_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventoId);
            ps.setInt(2, servicioId);
            ps.executeUpdate();
        }
    }

    // Método para desasociar todos los servicios adicionales de un evento
    public void desasociarServiciosDeEvento(int eventoId) throws SQLException {
        String query = "DELETE FROM evento_servicios_adicionales WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventoId);
            ps.executeUpdate();
        }
    }

    // Método para obtener los servicios adicionales asociados a un evento
    public List<ServiciosAdicionales> obtenerPorEventoId(int eventoId) throws SQLException {
        List<ServiciosAdicionales> serviciosAdicionalesList = new ArrayList<>();
        String query = "SELECT sa.* FROM servicios_adicionales sa " +
                "JOIN evento_servicios_adicionales esa ON sa.id = esa.servicio_adicional_id " +
                "WHERE esa.evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    serviciosAdicionalesList.add(new ServiciosAdicionales(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    ));
                }
            }
        }
        return serviciosAdicionalesList;
    }

    // Método para actualizar los servicios adicionales asociados a un evento
    public void actualizarServiciosDeEvento(int eventoId, List<ServiciosAdicionales> serviciosAdicionales) throws SQLException {
        // Primero, desasociamos todos los servicios actuales del evento
        desasociarServiciosDeEvento(eventoId);

        // Luego, asociamos los nuevos servicios adicionales
        for (ServiciosAdicionales servicio : serviciosAdicionales) {
            asociarServicioAEvento(eventoId, servicio.getId());
        }
    }
}




