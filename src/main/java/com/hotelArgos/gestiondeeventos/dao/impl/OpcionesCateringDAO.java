package com.hotelArgos.gestiondeeventos.dao.impl;

import com.hotelArgos.gestiondeeventos.dao.GenericDAO;
import com.hotelArgos.gestiondeeventos.model.OpcionesCatering;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OpcionesCateringDAO implements GenericDAO<OpcionesCatering> {

    private Connection connection;

    public OpcionesCateringDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void crear(OpcionesCatering opcionesCatering) throws SQLException {
        String query = "INSERT INTO opciones_catering (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, opcionesCatering.getNombre());
            ps.setString(2, opcionesCatering.getDescripcion());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    opcionesCatering.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public OpcionesCatering obtenerPorId(int id) throws SQLException {
        String query = "SELECT * FROM opciones_catering WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new OpcionesCatering(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<OpcionesCatering> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM opciones_catering";
        List<OpcionesCatering> opcionesCatering = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                opcionesCatering.add(new OpcionesCatering(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }
        }
        return opcionesCatering;
    }

    @Override
    public void actualizar(OpcionesCatering opcionesCatering) throws SQLException {
        String query = "UPDATE opciones_catering SET nombre = ?, descripcion = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, opcionesCatering.getNombre());
            ps.setString(2, opcionesCatering.getDescripcion());
            ps.setInt(3, opcionesCatering.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        // Eliminar referencias en la tabla evento_catering_opciones
        String deleteReferencesQuery = "DELETE FROM evento_catering_opciones WHERE opcion_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteReferencesQuery)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }

        // Eliminar la opción de catering
        String query = "DELETE FROM opciones_catering WHERE id = ?";
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