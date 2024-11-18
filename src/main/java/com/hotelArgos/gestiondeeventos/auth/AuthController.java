// AuthController.java
package com.hotelArgos.gestiondeeventos.auth;

import com.hotelArgos.gestiondeeventos.model.Usuario;

import java.sql.Connection;

public class AuthController {
    private AuthService authService;

    public AuthController(Connection connection) {
        this.authService = new AuthService(connection);
    }

    public boolean login(String nombre, String password) {
        return authService.authenticate(nombre, password);
    }
}