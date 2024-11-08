package com.hotelArgos.gestiondeeventos.controller;

import com.hotelArgos.gestiondeeventos.dao.impl.*;
import com.hotelArgos.gestiondeeventos.model.Catering;
import com.hotelArgos.gestiondeeventos.model.Evento;
import com.hotelArgos.gestiondeeventos.model.OpcionesCatering;

import com.hotelArgos.gestiondeeventos.model.ServiciosAdicionales;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventoController {

    private EventoDAO eventoDAO;

    public EventoController(Connection connection) {
        this.eventoDAO = new EventoDAO(connection);
    }

    public Connection getConnection() {
        return eventoDAO.getConnection();
    }

    public void crearEvento(Evento evento) {
        try {
            evento.setEstadoVigente();
            eventoDAO.crear(evento);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Evento obtenerEventoPorId(int id) {
        try {
            return eventoDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Evento> obtenerTodosLosEventos() {
        try {
            return eventoDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();  // Retorna una lista vacía en caso de error
    }

    public void actualizarEvento(Evento evento) {
        try {
            // Actualizar el evento principal
            eventoDAO.actualizar(evento);

            // Actualizar la empresa
            EmpresaDAO empresaDAO = new EmpresaDAO(eventoDAO.getConnection());
            empresaDAO.actualizar(evento.getEmpresa());

            // Actualizar el salón
            SalonDAO salonDAO = new SalonDAO(eventoDAO.getConnection());
            salonDAO.actualizar(evento.getSalon());

            // Actualizar el formato del salón
            FormatoSalonDAO formatoSalonDAO = new FormatoSalonDAO(eventoDAO.getConnection());
            formatoSalonDAO.actualizar(evento.getFormatoSalon());

            // Actualizar los caterings
            CateringDAO cateringDAO = new CateringDAO(eventoDAO.getConnection());
            for (Catering catering : evento.getCaterings()) {
                cateringDAO.actualizar(catering);
            }

            // Actualizar las opciones de catering
            OpcionesCateringDAO opcionesCateringDAO = new OpcionesCateringDAO(eventoDAO.getConnection());
            for (Catering catering : evento.getCaterings()) {
                for (OpcionesCatering opcion : catering.getOpcionesCatering()) {
                    opcionesCateringDAO.actualizar(opcion);
                }
            }

            // Actualizar los servicios adicionales
            ServiciosAdicionalesDAO serviciosAdicionalesDAO = new ServiciosAdicionalesDAO(eventoDAO.getConnection());
            serviciosAdicionalesDAO.actualizarServiciosDeEvento(evento.getId(), evento.getServiciosAdicionales());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarEvento(int id) {
        try {
            eventoDAO.eliminar(id);
            System.out.println("Evento eliminado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar el evento: " + e.getMessage());
        }
    }
    public void cancelarEvento(int id) {
        try {
            Evento evento = eventoDAO.obtenerPorId(id);
            if (evento != null) {
                evento.cancelarEvento();
                eventoDAO.actualizar(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void finalizarEvento(int id) {
        try {
            Evento evento = eventoDAO.obtenerPorId(id);
            if (evento != null) {
                evento.finalizarEvento();
                eventoDAO.actualizar(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void generarInformePDF(Evento evento) {

        String filePath = "C:\\Users\\crono\\OneDrive\\Documentos\\informes-gestion-de-eventos-app\\informe_evento_" + evento.getId() + ".pdf";
        try {
            PdfWriter writer = new PdfWriter(filePath);
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Informe del Evento"));
            document.add(new Paragraph("ID: " + evento.getId()));
            document.add(new Paragraph("Nombre: " + evento.getNombre()));
            document.add(new Paragraph("Fecha: " + evento.getFecha()));
            document.add(new Paragraph("Hora de Inicio: " + evento.getHoraInicio()));
            document.add(new Paragraph("Hora de Fin: " + evento.getHoraFin()));
            document.add(new Paragraph("Cantidad de Personas: " + evento.getCantidadPersonas()));
            document.add(new Paragraph("Observaciones: " + evento.getObservaciones()));
            document.add(new Paragraph("Estado: " + evento.getEstado()));

            Table table = new Table(2);
            table.addCell("Servicio Adicional");
            table.addCell("Descripción");
            for (ServiciosAdicionales servicio : evento.getServiciosAdicionales()) {
                table.addCell(servicio.getNombre());
                table.addCell(servicio.getDescripcion());
            }
            document.add(table);

            document.close();
            System.out.println("PDF generado exitosamente en: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}