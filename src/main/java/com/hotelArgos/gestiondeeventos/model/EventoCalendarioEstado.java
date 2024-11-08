package com.hotelArgos.gestiondeeventos.model;
import com.hotelArgos.gestiondeeventos.dao.impl.EventoDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventoCalendarioEstado {
    private EventoDAO eventoDAO;

    public EventoCalendarioEstado(EventoDAO eventoDAO) {
        this.eventoDAO = eventoDAO;
    }

    public void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateEventStatuses, 0, 1, TimeUnit.DAYS);
    }

    private void updateEventStatuses() {
        try {
            List<Evento> eventos = eventoDAO.obtenerTodos();
            LocalDate today = LocalDate.now();
            for (Evento evento : eventos) {
                if (evento.getFecha().isBefore(today) && "vigente".equalsIgnoreCase(evento.getEstado())) {
                    evento.finalizarEvento();
                    eventoDAO.actualizar(evento);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}