package com.hotelArgos.gestiondeeventos.controller;

import com.hotelArgos.gestiondeeventos.dao.impl.UsuarioDAO;
import com.hotelArgos.gestiondeeventos.model.Usuario;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioController {

    private UsuarioDAO usuarioDAO;

    public UsuarioController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void crearUsuario(Usuario usuario) {
        try {
            usuarioDAO.crear(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario obtenerUsuarioPorId(int id) {
        try {
            return usuarioDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        try {
            return usuarioDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();  // Retorna una lista vacía en caso de error
    }

    public void actualizarUsuario(Usuario usuario) {
        try {
            usuarioDAO.actualizar(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarUsuario(int id) {
        try {
            usuarioDAO.eliminar(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        try {
            return usuarioDAO.obtenerPorEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
    }
        return null;
    }
}