// AuthService.java
package com.hotelArgos.gestiondeeventos.auth;

import com.hotelArgos.gestiondeeventos.model.Usuario;
import com.hotelArgos.gestiondeeventos.dao.impl.UsuarioDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class AuthService {
    private UsuarioDAO usuarioDAO;

    public AuthService(Connection connection) {
        this.usuarioDAO = new UsuarioDAO(connection);
    }

    public boolean authenticate(String nombre, String password) {
        try {
            Usuario usuario = usuarioDAO.obtenerPorNombre(nombre);
            return usuario != null && usuario.getPassword().equals(password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}