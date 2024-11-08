package com.hotelArgos.gestiondeeventos.dao.impl;

import com.hotelArgos.gestiondeeventos.dao.GenericDAO;
import com.hotelArgos.gestiondeeventos.model.Catering;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CateringDAO implements GenericDAO<Catering> {

    private Connection connection;

    public CateringDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void crear(Catering catering) throws SQLException {
        String query = "INSERT INTO catering (tipo_catering, observaciones) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, catering.getTipoCatering());
            ps.setString(2, catering.getObservaciones());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    catering.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public Catering obtenerPorId(int id) throws SQLException {
        String query = "SELECT * FROM catering WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Catering(
                            rs.getInt("id"),
                            rs.getString("tipo_catering"),
                            rs.getString("observaciones"),
                            null // No se necesita la lista de opciones de catering aca
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Catering> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM catering";
        List<Catering> caterings = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                caterings.add(new Catering(
                        rs.getInt("id"),
                        rs.getString("tipo_catering"),
                        rs.getString("observaciones"),
                        null // No se necesita la lista de opciones de catering aca
                ));
            }
        }
        return caterings;
    }

    @Override
    public void actualizar(Catering catering) throws SQLException {
        String query = "UPDATE catering SET tipo_catering = ?, observaciones = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, catering.getTipoCatering());
            ps.setString(2, catering.getObservaciones());
            ps.setInt(3, catering.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        // Primero, eliminar el registro relacionado en the evento_catering_opciones table
        String deleteReferencesQuery = "DELETE FROM evento_catering_opciones WHERE catering_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteReferencesQuery)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }

        // Luego, eliminar el registro in the catering table
        String query = "DELETE FROM catering WHERE id = ?";
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