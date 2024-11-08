//package test;
//
//import com.hotelArgos.gestiondeeventos.controller.*;
//import com.hotelArgos.gestiondeeventos.model.*;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class TestCRUD {
//    public static void main(String[] args) {
//
//        // Establecer la conexión a la base de datos
//        String url = "jdbc:mysql://localhost:3306/bd_gestiondeeventosApp";  // Cambiar por el nombre real de la BD
//        String username = "root";  // Cambiar por tu usuario
//        String password = "mysql1034";  // Cambiar por tu contraseña
//
//        try (Connection connection = DriverManager.getConnection(url, username, password)) {
//            System.out.println("Conexión a la base de datos establecida.");
//
//            // Inicializa los controladores
//            SalonController salonController = new SalonController(connection);
//            EmpresaController empresaController = new EmpresaController(connection);
//            FormatoSalonController formatoSalonController = new FormatoSalonController(connection);
//            ServiciosAdicionalesController servicioAdicionalController = new ServiciosAdicionalesController(connection);
//            CateringController cateringController = new CateringController(connection);
//            EventoController eventoController = new EventoController(connection);
//
//            // Crear una nueva Empresa
//            Empresa nuevaEmpresa = new Empresa("Empresa ABC", "contacto@empresaabc.com", "123456789", "ergewrg@ssdfg");
//            empresaController.crearEmpresa(nuevaEmpresa);
//            System.out.println("Empresa agregada: " + nuevaEmpresa);
//
//            // Crear un nuevo Salón
//            Salon nuevoSalon = new Salon("Salon Argos", 200, "Descripción del salón");
//            salonController.crearSalon(nuevoSalon);
//            System.out.println("Salón agregado: " + nuevoSalon);
//
//            // Crear un nuevo Formato de Salón
//            FormatoSalon nuevoFormato = new FormatoSalon("Formato conferencia", "35 sillas en U");
//            formatoSalonController.crearFormatoSalon(nuevoFormato);
//            System.out.println("Formato agregado: " + nuevoFormato);
//
//            // Crear un nuevo Catering con Opciones de Catering
//            List<OpcionesCatering> opcionesCatering = new ArrayList<>();
//            Catering nuevoCatering = new Catering("Catering Premium", "Incluye bebidas y postre", opcionesCatering);
//
//            // Crear opciones de catering y añadirlas a la lista
//            OpcionesCatering opcion1 = new OpcionesCatering("Opción 1", "Descripción de la opción 1", nuevoCatering);
//            OpcionesCatering opcion2 = new OpcionesCatering("Opción 2", "Descripción de la opción 2", nuevoCatering);
//            opcionesCatering.add(opcion1);
//            opcionesCatering.add(opcion2);
//
//            // Agregar el Catering a la base de datos
//            cateringController.crearCatering(nuevoCatering);
//            System.out.println("Catering agregado: " + nuevoCatering);
//
//            // Crear varios Servicios Adicionales
//            servicioAdicionalController.crearServicioAdicional("DJ", "Música toda la noche");
//            servicioAdicionalController.crearServicioAdicional("Iluminación", "Iluminación profesional para el evento");
//            servicioAdicionalController.crearServicioAdicional("Pc Portatil", "Servicio de Pc Portatil");
//
//            // Obtener los servicios recién creados desde la base de datos para agregarlos al evento
//            List<ServiciosAdicionales> serviciosAdicionales = new ArrayList<>();
//            ServiciosAdicionales servicioDJ = servicioAdicionalController.obtenerServicioAdicionalPorId(1); // ID del servicio 'DJ'
//            ServiciosAdicionales servicioIluminacion = servicioAdicionalController.obtenerServicioAdicionalPorId(2); // ID del servicio 'Iluminación'
//            ServiciosAdicionales servicioPcPortatil = servicioAdicionalController.obtenerServicioAdicionalPorId(3); // ID del servicio 'Pc Portatil'
//
//
//            // Agregar los servicios adicionales a la lista
//            serviciosAdicionales.add(servicioDJ);
//            serviciosAdicionales.add(servicioIluminacion);
//            serviciosAdicionales.add(servicioPcPortatil);
//
//            // Crear un nuevo Evento con los datos anteriores
//            LocalDateTime now = LocalDateTime.now();
//            Evento nuevoEvento = new Evento(
//                    "Laboratorio Ginkoviloba",
//                    LocalDate.of(2024, 9, 5),
//                    LocalTime.of(20, 0),
//                    LocalTime.of(23, 0),
//                    100,
//                    "Aniversario de la empresa",
//                    "Vigente",
//                    nuevaEmpresa,  // Empresa previamente creada
//                    nuevoSalon,    // Salón previamente creado
//                    nuevoFormato,  // Formato de salón previamente creado
//                    nuevoCatering, // Catering previamente creado
//                    serviciosAdicionales, // Lista de servicios adicionales
//                    now,
//                    now
//            );
//
//            // Crear el evento en la base de datos
//            eventoController.crearEvento(nuevoEvento);
//            System.out.println("Evento 'Cumpleaños de Juan' con varios servicios adicionales creado.");
//
//            eventoController.crearEvento(nuevoEvento);
//            System.out.println("Evento agregado: " + nuevoEvento);
//
//            // Leer o listar eventos
//            List<Evento> eventos = eventoController.obtenerTodosLosEventos();
//            System.out.println("Lista de eventos:");
//            for (Evento evento : eventos) {
//                System.out.println(evento);
//            }
//
//            // Actualizar un evento
//            nuevoEvento.setEstado("en progreso");
//            eventoController.actualizarEvento(nuevoEvento);
//            System.out.println("Evento actualizado: " + nuevoEvento);
//
//            // Eliminar un evento
//            eventoController.eliminarEvento(nuevoEvento.getId());
//            System.out.println("Evento eliminado.");
//
//            // Obtener el ID del servicio adicional que deseas eliminar desde la base de datos
//            servicioDJ = servicioAdicionalController.obtenerServicioAdicionalPorId(1); // Cambia el ID por el correcto
//            servicioIluminacion = servicioAdicionalController.obtenerServicioAdicionalPorId(2); // Cambia el ID por el correcto
//            servicioPcPortatil = servicioAdicionalController.obtenerServicioAdicionalPorId(3); // Cambia el ID por el correcto
//
//            // Limpiar la base de datos de prueba eliminando los servicios adicionales creados
//            servicioAdicionalController.eliminarServicioAdicional(servicioDJ.getId());
//            servicioAdicionalController.eliminarServicioAdicional(servicioIluminacion.getId());
//            servicioAdicionalController.eliminarServicioAdicional(servicioPcPortatil.getId());
//
//
//            // Limpiar la base de datos de prueba eliminando otras entidades
//            cateringController.eliminarCatering(nuevoCatering.getId());
//            salonController.eliminarSalon(nuevoSalon.getId());
//            empresaController.eliminarEmpresa(nuevaEmpresa.getId());
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Error al conectar a la base de datos.");
//        }
//    }
//}




