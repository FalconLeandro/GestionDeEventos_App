package com.hotelArgos.gestiondeeventos.dao;

import java.util.List;

public interface GenericDAO<T> {
    void crear(T t) throws Exception;
    T obtenerPorId(int id) throws Exception;  // Mantén este método para tablas con una clave primaria
    List<T> obtenerTodos() throws Exception;
    void actualizar(T t) throws Exception;
    void eliminar(int id) throws Exception;

    // Método opcional para tablas intermedias
    void eliminarPorIds(int id1, int id2) throws Exception;  // Para manejar claves compuestas
}
