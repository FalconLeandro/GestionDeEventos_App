package com.hotelArgos.gestiondeeventos.controller;

import com.hotelArgos.gestiondeeventos.dao.impl.EmpresaDAO;
import com.hotelArgos.gestiondeeventos.model.Empresa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpresaController {

    private EmpresaDAO empresaDAO;

    public EmpresaController(Connection connection) {
        this.empresaDAO = new EmpresaDAO(connection);
    }

    public void crearEmpresa(Empresa empresa) {
        try {
            empresaDAO.crear(empresa);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Empresa obtenerEmpresaPorId(int id) {
        try {
            return empresaDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Empresa> obtenerTodasLasEmpresas() {
        try {
            return empresaDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();  // Retorna una lista vacía en caso de error
    }

    public void actualizarEmpresa(Empresa empresa) {
        try {
            empresaDAO.actualizar(empresa);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarEmpresa(int id) {
        try {
            empresaDAO.eliminar(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
