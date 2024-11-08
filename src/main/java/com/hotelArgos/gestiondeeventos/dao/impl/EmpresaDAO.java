package com.hotelArgos.gestiondeeventos.dao.impl;

import com.hotelArgos.gestiondeeventos.dao.GenericDAO;
import com.hotelArgos.gestiondeeventos.model.Empresa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO implements GenericDAO<Empresa> {

    private Connection connection;

    public EmpresaDAO(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void crear(Empresa empresa) throws SQLException {
        String query = "INSERT INTO empresa (nombre, contacto, telefono, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, empresa.getNombre());
            ps.setString(2, empresa.getContacto());
            ps.setString(3, empresa.getTelefono());
            ps.setString(4, empresa.getEmail());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    empresa.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public Empresa obtenerPorId(int id) throws SQLException {
        String query = "SELECT * FROM empresa WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Empresa(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("contacto"),
                            rs.getString("telefono"),
                            rs.getString("email")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Empresa> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM empresa";
        List<Empresa> empresas = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                empresas.add(new Empresa(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("contacto"),
                        rs.getString("telefono"),
                        rs.getString("email")
                ));
            }
        }
        return empresas;
    }

    @Override
    public void actualizar(Empresa empresa) throws SQLException {
        String query = "UPDATE empresa SET nombre = ?, contacto = ?, telefono = ?, email = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, empresa.getNombre());
            ps.setString(2, empresa.getContacto());
            ps.setString(3, empresa.getTelefono());
            ps.setString(4, empresa.getEmail());
            ps.setInt(5, empresa.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM empresa WHERE id = ?";
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