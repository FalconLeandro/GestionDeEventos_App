package com.hotelArgos.gestiondeeventos.auth;

import com.hotelArgos.gestiondeeventos.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDAO {

    private Connection connection;

    public AuthDAO(Connection connection) {
        this.connection = connection;
    }

    public Usuario login(String email, String password) throws SQLException {
        String query = "SELECT * FROM usuario WHERE email = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("rol")
                    );
                }
            }
        }
        return null; // Retorna null si las credenciales no son válidas
    }

    public boolean register(Usuario usuario) throws SQLException {
        String query = "INSERT INTO usuario (nombre, email, password, rol) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getPassword());
            ps.setString(4, usuario.getRol());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}