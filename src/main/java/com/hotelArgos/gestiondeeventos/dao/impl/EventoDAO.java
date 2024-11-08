package com.hotelArgos.gestiondeeventos.dao.impl;

import com.hotelArgos.gestiondeeventos.dao.GenericDAO;
import com.hotelArgos.gestiondeeventos.model.Catering;
import com.hotelArgos.gestiondeeventos.model.Evento;
import com.hotelArgos.gestiondeeventos.model.OpcionesCatering;
import com.hotelArgos.gestiondeeventos.model.ServiciosAdicionales;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO implements GenericDAO<Evento> {

    private Connection connection;

    public EventoDAO(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void crear(Evento evento) throws SQLException {
        String query = "INSERT INTO evento (nombre, fecha, hora_inicio, hora_fin, cantidad_personas, observaciones, estado, empresa_id, salon_id, formato_salon_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, evento.getNombre());
            ps.setDate(2, Date.valueOf(evento.getFecha()));
            ps.setTime(3, Time.valueOf(evento.getHoraInicio()));
            ps.setTime(4, Time.valueOf(evento.getHoraFin()));
            ps.setInt(5, evento.getCantidadPersonas());
            ps.setString(6, evento.getObservaciones());
            ps.setString(7, evento.getEstado());
            ps.setInt(8, evento.getEmpresa().getId());
            ps.setInt(9, evento.getSalon().getId());
            ps.setInt(10, evento.getFormatoSalon().getId());
            ps.setTimestamp(11, Timestamp.valueOf(evento.getCreatedAt()));
            ps.setTimestamp(12, Timestamp.valueOf(evento.getUpdatedAt()));
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    evento.setId(generatedKeys.getInt(1));
                }
            }

            String cateringQuery = "INSERT INTO evento_catering_opciones (evento_id, catering_id, opcion_id) VALUES (?, ?, ?)";
            try (PreparedStatement psCatering = connection.prepareStatement(cateringQuery)) {
                for (Catering catering : evento.getCaterings()) {
                    for (OpcionesCatering opcion : catering.getOpcionesCatering()) {
                        psCatering.setInt(1, evento.getId());
                        psCatering.setInt(2, catering.getId());
                        psCatering.setInt(3, opcion.getId());
                        psCatering.addBatch();
                    }
                }
                psCatering.executeBatch();
            }

            // Insertar servicios adicionales
            if (evento.getServiciosAdicionales() != null) {
                for (ServiciosAdicionales servicio : evento.getServiciosAdicionales()) {
                    String queryServicios = "INSERT INTO evento_servicios_adicionales (evento_id, servicio_adicional_id) VALUES (?, ?)";
                    try (PreparedStatement psServicios = connection.prepareStatement(queryServicios)) {
                        psServicios.setInt(1, evento.getId());
                        psServicios.setInt(2, servicio.getId());
                        psServicios.executeUpdate();
                    }
                }
            }
        }
    }

    @Override
    public Evento obtenerPorId(int id) throws SQLException {
        String query = "SELECT * FROM evento WHERE id = ?";
        Evento evento = null;
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    evento = new Evento(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getTime("hora_inicio").toLocalTime(),
                            rs.getTime("hora_fin").toLocalTime(),
                            rs.getInt("cantidad_personas"),
                            rs.getString("observaciones"),
                            new EmpresaDAO(connection).obtenerPorId(rs.getInt("empresa_id")),
                            new SalonDAO(connection).obtenerPorId(rs.getInt("salon_id")),
                            new FormatoSalonDAO(connection).obtenerPorId(rs.getInt("formato_salon_id")),
                            new ArrayList<>(), // Lista vacía para serviciosAdicionales
                            new ArrayList<>(), // Lista vacía para caterings
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    );
                    evento.setEstado(rs.getString("estado"));

                    // Recuperar caterings asociados al evento
                    String cateringQuery = "SELECT * FROM evento_catering_opciones WHERE evento_id = ?";
                    try (PreparedStatement psCatering = connection.prepareStatement(cateringQuery)) {
                        psCatering.setInt(1, evento.getId());
                        try (ResultSet rsCatering = psCatering.executeQuery()) {
                            while (rsCatering.next()) {
                                int cateringId = rsCatering.getInt("catering_id");
                                int opcionId = rsCatering.getInt("opcion_id");

                                Evento finalEvento = evento;
                                Catering catering = evento.getCaterings().stream()
                                        .filter(c -> c.getId() == cateringId)
                                        .findFirst()
                                        .orElseGet(() -> {
                                            try {
                                                Catering newCatering = new CateringDAO(connection).obtenerPorId(cateringId);
                                                finalEvento.getCaterings().add(newCatering);
                                                return newCatering;
                                            } catch (SQLException e) {
                                                throw new RuntimeException(e);
                                            }
                                        });

                                OpcionesCatering opcion = new OpcionesCateringDAO(connection).obtenerPorId(opcionId);
                                catering.getOpcionesCatering().add(opcion);
                            }
                        }

                    }
                    // Recuperar servicios adicionales asociados al evento
                    String serviciosQuery = "SELECT sa.* FROM servicios_adicionales sa " +
                            "JOIN evento_servicios_adicionales esa ON sa.id = esa.servicio_adicional_id " +
                            "WHERE esa.evento_id = ?";
                    try (PreparedStatement psServicios = connection.prepareStatement(serviciosQuery)) {
                        psServicios.setInt(1, evento.getId());
                        try (ResultSet rsServicios = psServicios.executeQuery()) {
                            while (rsServicios.next()) {
                                ServiciosAdicionales servicio = new ServiciosAdicionales(
                                        rsServicios.getInt("id"),
                                        rsServicios.getString("nombre"),
                                        rsServicios.getString("descripcion")
                                );
                                evento.getServiciosAdicionales().add(servicio);

                            }
                        }
                    }
                }
            }
        }
        return evento;
    }

    @Override
    public List<Evento> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM evento";
        List<Evento> eventos = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Evento evento = obtenerPorId(rs.getInt("id"));
                eventos.add(evento);
            }
        }
        return eventos;
    }

    @Override
    public void actualizar(Evento evento) throws SQLException {
        String query = "UPDATE evento SET nombre = ?, fecha = ?, hora_inicio = ?, hora_fin = ?, cantidad_personas = ?, observaciones = ?, estado = ?, empresa_id = ?, salon_id = ?, formato_salon_id = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, evento.getNombre());
            ps.setDate(2, Date.valueOf(evento.getFecha()));
            ps.setTime(3, Time.valueOf(evento.getHoraInicio()));
            ps.setTime(4, Time.valueOf(evento.getHoraFin()));
            ps.setInt(5, evento.getCantidadPersonas());
            ps.setString(6, evento.getObservaciones());
            ps.setString(7, evento.getEstado());
            ps.setInt(8, evento.getEmpresa().getId());
            ps.setInt(9, evento.getSalon().getId());
            ps.setInt(10, evento.getFormatoSalon().getId());
            ps.setTimestamp(11, Timestamp.valueOf(evento.getUpdatedAt()));
            ps.setInt(12, evento.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        // Eliminar asociaciones en la tabla evento_catering_opciones
        String deleteCateringQuery = "DELETE FROM evento_catering_opciones WHERE evento_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteCateringQuery)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }

        // Eliminar el evento
        String query = "DELETE FROM evento WHERE id = ?";
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