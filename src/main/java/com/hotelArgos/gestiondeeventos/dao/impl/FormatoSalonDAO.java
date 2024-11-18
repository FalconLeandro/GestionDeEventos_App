package com.hotelArgos.gestiondeeventos.dao.impl;

import com.hotelArgos.gestiondeeventos.dao.GenericDAO;
import com.hotelArgos.gestiondeeventos.model.FormatoSalon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormatoSalonDAO implements GenericDAO<FormatoSalon> {

    private Connection connection;

    public FormatoSalonDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void crear(FormatoSalon formatoSalon) throws SQLException {
        String query = "INSERT INTO formato_salon (nombre, descripcion, imagen_formato_salon) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, formatoSalon.getNombre());
            ps.setString(2, formatoSalon.getDescripcion());
            ps.setString(3, formatoSalon.getImagenFormatoSalon());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    formatoSalon.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public FormatoSalon obtenerPorId(int id) throws SQLException {
        String query = "SELECT * FROM formato_salon WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new FormatoSalon(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getString("imagen_formato_salon")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<FormatoSalon> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM formato_salon";
        List<FormatoSalon> formatos = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                formatos.add(new FormatoSalon(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("imagen_formato_salon")
                ));
            }
        }
        return formatos;
    }

    @Override
    public void actualizar(FormatoSalon formatoSalon) throws SQLException {
        String query = "UPDATE formato_salon SET nombre = ?, descripcion = ?, imagen_formato_salon = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, formatoSalon.getNombre());
            ps.setString(2, formatoSalon.getDescripcion());
            ps.setString(3, formatoSalon.getImagenFormatoSalon());
            ps.setInt(4, formatoSalon.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM formato_salon WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminarPorIds(int id1, int id2) throws Exception {
        // Implementación del método eliminarPorIds
    }
}