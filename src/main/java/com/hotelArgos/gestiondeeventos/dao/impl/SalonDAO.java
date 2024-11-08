package com.hotelArgos.gestiondeeventos.dao.impl;

import com.hotelArgos.gestiondeeventos.dao.GenericDAO;
import com.hotelArgos.gestiondeeventos.model.Salon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalonDAO implements GenericDAO<Salon> {

    private Connection connection;

    public SalonDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void crear(Salon salon) throws SQLException {
        String query = "INSERT INTO salon (nombre, capacidad, descripcion) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, salon.getNombre());
            ps.setInt(2, salon.getCapacidad());
            ps.setString(3, salon.getDescripcion());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    salon.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public Salon obtenerPorId(int id) throws SQLException {
        String query = "SELECT * FROM salon WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Salon(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getInt("capacidad"),
                            rs.getString("descripcion")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Salon> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM salon";
        List<Salon> salones = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                salones.add(new Salon(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("capacidad"),
                        rs.getString("descripcion")
                ));
            }
        }
        return salones;
    }

    @Override
    public void actualizar(Salon salon) throws SQLException {
        String query = "UPDATE salon SET nombre = ?, capacidad = ?, descripcion = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, salon.getNombre());
            ps.setInt(2, salon.getCapacidad());
            ps.setString(3, salon.getDescripcion());
            ps.setInt(4, salon.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM salon WHERE id = ?";
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