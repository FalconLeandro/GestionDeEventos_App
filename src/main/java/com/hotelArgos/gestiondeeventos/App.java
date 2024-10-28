package com.hotelArgos.gestiondeeventos;

import com.hotelArgos.gestiondeeventos.controller.*;
import com.hotelArgos.gestiondeeventos.dao.impl.*;
import com.hotelArgos.gestiondeeventos.model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bd_gestiondeeventosapp", "root", "mysql1034")) {
            Scanner scanner = new Scanner(System.in);
            menuPrincipal(connection, scanner);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void menuPrincipal(Connection connection, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("=== Menú Principal ===");
            System.out.println("1. Gestión de Eventos");
            System.out.println("2. Gestión de Empresas");
            System.out.println("3. Gestión de Salones");
            System.out.println("4. Gestión de Formatos de Salón");
            System.out.println("5. Gestión de Servicios Adicionales");
            System.out.println("6. Gestión de Catering");
            System.out.println("7. Gestión de Opciones de Catering");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    menuGestionEventos(connection, scanner);
                    break;
                case 2:
                    menuGestionEmpresas(connection, scanner);
                    break;
                case 3:
                    menuGestionSalones(connection, scanner);
                    break;
                case 4:
                    menuGestionFormatoSalon(connection, scanner);
                    break;
                case 5:
                    menuGestionServicios(connection, scanner);
                    break;
                case 6:
                    menuGestionCatering(connection, scanner);
                    break;
                case 7:
                    menuGestionOpcionesCatering(connection, scanner);
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // Menú de gestión de eventos
    public static void menuGestionEventos(Connection connection, Scanner scanner) throws SQLException {
        EventoController eventoController = new EventoController(connection);
        while (true) {
            System.out.println("=== Gestión de Eventos ===");
            System.out.println("1. Crear Evento");
            System.out.println("2. Listar Eventos");
            System.out.println("3. Actualizar Evento");
            System.out.println("4. Eliminar Evento");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    crearEvento(eventoController, scanner, connection);
                    break;
                case 2:
                    listarEventos(eventoController);
                    break;
                case 3:
                    actualizarEvento(eventoController, scanner);
                    break;
                case 4:
                    eliminarEvento(eventoController, scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // Evento
    public static void crearEvento(EventoController eventoController, Scanner scanner, Connection connection) throws SQLException {
        System.out.println("=== Crear Nuevo Evento ===");

        System.out.print("Nombre del Evento: ");
        String nombre = scanner.nextLine();

        System.out.print("Fecha del evento (AAAA-MM-DD): ");
        LocalDate fecha = LocalDate.parse(scanner.next());
        scanner.nextLine(); // Consumir el salto de línea

        System.out.print("Hora de inicio (HH:MM): ");
        LocalTime horaInicio = LocalTime.parse(scanner.next());
        scanner.nextLine(); // Consumir el salto de línea

        System.out.print("Hora de finalización (HH:MM): ");
        LocalTime horaFin = LocalTime.parse(scanner.next());
        scanner.nextLine(); // Consumir el salto de línea

        System.out.print("Cantidad de personas: ");
        int cantidadPersonas = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        System.out.print("Observaciones: ");
        String observaciones = scanner.nextLine();

        System.out.print("Estado (e.g., programado, en progreso, finalizado, cancelado): ");
        String estado = scanner.nextLine();

        // Asumiendo que el ID de Empresa ya existe y el usuario lo proporciona
        System.out.print("ID de la Empresa: ");
        int empresaId = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        EmpresaDAO empresaDAO = new EmpresaDAO(connection); // Pasa la conexión al DAO
        Empresa empresa = empresaDAO.obtenerPorId(empresaId); // Obtén la empresa desde la base de datos
        if (empresa == null) {
            System.out.println("Error: No se encontró la empresa con el ID especificado.");
            return; // Maneja el caso en que la empresa no exista
        }

        // Asumiendo que el ID de Salón ya existe y el usuario lo proporciona
        System.out.print("ID del Salón: ");
        int salonId = scanner.nextInt();
        scanner.nextLine();

        SalonDAO salonDAO = new SalonDAO(connection); // Pasa la conexión al DAO
        Salon salon = salonDAO.obtenerPorId(salonId); // Obtén la empresa desde la base de datos
        if (salon == null) {
            System.out.println("Error: No se encontró el salon con el ID especificado.");
            return; // Maneja el caso en que la empresa no exista
        }

        // Asumiendo que el ID de FormatoSalón ya existe y el usuario lo proporciona
        System.out.print("ID del Formato del Salón: ");
        int formatoSalonId = scanner.nextInt();
        scanner.nextLine();

        FormatoSalonDAO formatoSalonDAO = new FormatoSalonDAO(connection); // Pasa la conexión al DAO
        FormatoSalon formatoSalon = formatoSalonDAO.obtenerPorId(formatoSalonId); // Obtén el formatoSalon desde la base de datos
        if (formatoSalon == null) {
            System.out.println("Error: No se encontró el formatoSalon con el ID especificado.");
            return; // Maneja el caso en que el formatoSalon no exista
        }

        // Listar todos los caterings disponibles
        CateringDAO cateringDAO = new CateringDAO(connection);
        List<Catering> caterings = cateringDAO.obtenerTodos();
        if (caterings.isEmpty()) {
            System.out.println("No hay caterings disponibles.");
            return;
        }

        System.out.println("=== Lista de Caterings Disponibles ===");
        for (Catering catering : caterings) {
            System.out.println(catering.getId() + ": " + catering.getTipoCatering());
        }

        // Permitir al usuario seleccionar uno o varios caterings
        List<Catering> cateringsSeleccionados = new ArrayList<>();
        System.out.print("IDs de los Caterings (separados por comas): ");
        String[] idsCaterings = scanner.nextLine().split(",");
        for (String id : idsCaterings) {
            Catering catering = cateringDAO.obtenerPorId(Integer.parseInt(id.trim()));
            if (catering != null) {
                cateringsSeleccionados.add(catering);
            } else {
                System.out.println("Catering con ID " + id + " no encontrado.");
            }
        }

        // Listar todas las opciones de catering disponibles
        OpcionesCateringDAO opcionesCateringDAO = new OpcionesCateringDAO(connection);
        List<OpcionesCatering> opcionesCatering = opcionesCateringDAO.obtenerTodos();
        if (opcionesCatering.isEmpty()) {
            System.out.println("No hay opciones de catering disponibles.");
            return;
        }

        System.out.println("=== Lista de Opciones de Catering Disponibles ===");
        for (OpcionesCatering opcionCatering : opcionesCatering) {
            System.out.println(opcionCatering.getId() + ": " + opcionCatering.getNombre());
        }

        // Para cada catering seleccionado, listar y seleccionar opciones de catering
        for (Catering catering : cateringsSeleccionados) {
            System.out.println("=== Opciones de Catering para " + catering.getTipoCatering() + " ===");
            System.out.print("IDs de las Opciones de Catering (separados por comas): ");
            String[] idsOpciones = scanner.nextLine().split(",");
            List<OpcionesCatering> opcionesSeleccionadas = new ArrayList<>();
            for (String id : idsOpciones) {
                OpcionesCatering opcion = opcionesCatering.stream()
                        .filter(o -> o.getId() == Integer.parseInt(id.trim()))
                        .findFirst()
                        .orElse(null);
                if (opcion != null) {
                    opcionesSeleccionadas.add(opcion);
                } else {
                    System.out.println("Opción de Catering con ID " + id + " no encontrada.");
                }
            }
            catering.setOpcionesCatering(opcionesSeleccionadas);
        }

        // Asigna fechas de creación y actualización automáticas (puedes modificar esto según lo necesites)
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Crear el nuevo evento con todos los datos recolectados.
        Evento nuevoEvento = new Evento(nombre, fecha, horaInicio, horaFin, cantidadPersonas, observaciones, estado, empresa, salon, formatoSalon, cateringsSeleccionados, createdAt, updatedAt);
        eventoController.crearEvento(nuevoEvento);

        System.out.println("Evento creado exitosamente.");
    }

    public static void listarEventos(EventoController eventoController) {
        List<Evento> eventos = eventoController.obtenerTodosLosEventos();
        if (eventos.isEmpty()) {
            System.out.println("No hay eventos registrados.");
        } else {
            for (Evento evento : eventos) {
                System.out.println(evento);
            }
        }
    }

    public static void actualizarEvento(EventoController eventoController, Scanner scanner) {
        System.out.print("ID del Evento a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Evento evento = eventoController.obtenerEventoPorId(id);
        if (evento == null) {
            System.out.println("Evento no encontrado.");
            return;
        }

        System.out.print("Nuevo nombre del Evento (dejar en blanco para no cambiar): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) {
            evento.setNombre(nombre);
        }

        System.out.print("Nueva fecha del evento (AAAA-MM-DD) (dejar en blanco para no cambiar): ");
        String fechaStr = scanner.nextLine();
        if (!fechaStr.isEmpty()) {
            LocalDate fecha = LocalDate.parse(fechaStr);
            evento.setFecha(fecha);
        }

        System.out.print("Nueva hora de inicio (HH:MM) (dejar en blanco para no cambiar): ");
        String horaInicioStr = scanner.nextLine();
        if (!horaInicioStr.isEmpty()) {
            LocalTime horaInicio = LocalTime.parse(horaInicioStr);
            evento.setHoraInicio(horaInicio);
        }

        System.out.print("Nueva hora de finalización (HH:MM) (dejar en blanco para no cambiar): ");
        String horaFinStr = scanner.nextLine();
        if (!horaFinStr.isEmpty()) {
            LocalTime horaFin = LocalTime.parse(horaFinStr);
            evento.setHoraFin(horaFin);
        }

        System.out.print("Nueva cantidad de personas (dejar en blanco para no cambiar): ");
        String cantidadPersonasStr = scanner.nextLine();
        if (!cantidadPersonasStr.isEmpty()) {
            int cantidadPersonas = Integer.parseInt(cantidadPersonasStr);
            evento.setCantidadPersonas(cantidadPersonas);
        }

        System.out.print("Nuevas observaciones (dejar en blanco para no cambiar): ");
        String observaciones = scanner.nextLine();
        if (!observaciones.isEmpty()) {
            evento.setObservaciones(observaciones);
        }

        System.out.print("Nuevo estado (dejar en blanco para no cambiar): ");
        String estado = scanner.nextLine();
        if (!estado.isEmpty()) {
            evento.setEstado(estado);
        }

        eventoController.actualizarEvento(evento);
        System.out.println("Evento actualizado exitosamente.");
    }

    public static void eliminarEvento(EventoController eventoController, Scanner scanner) {
        System.out.print("ID del Evento a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        eventoController.eliminarEvento(id);
        System.out.println("Evento eliminado exitosamente.");
    }

    // Menú de gestión de empresas
    public static void menuGestionEmpresas(Connection connection, Scanner scanner) {
        EmpresaController empresaController = new EmpresaController(connection);
        while (true) {
            System.out.println("=== Gestión de Empresas ===");
            System.out.println("1. Crear Empresa");
            System.out.println("2. Listar Empresas");
            System.out.println("3. Actualizar Empresa");
            System.out.println("4. Eliminar Empresa");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    crearEmpresa(empresaController, scanner);
                    break;
                case 2:
                    listarEmpresas(empresaController);
                    break;
                case 3:
                    actualizarEmpresa(empresaController, scanner);
                    break;
                case 4:
                    eliminarEmpresa(empresaController, scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    public static void crearEmpresa(EmpresaController empresaController, Scanner scanner) {
        System.out.println("=== Crear Nueva Empresa ===");

        System.out.print("Nombre de la Empresa: ");
        String nombre = scanner.nextLine();

        System.out.print("Nombre del Contacto: ");
        String contacto = scanner.nextLine();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        Empresa nuevaEmpresa = new Empresa(nombre, contacto, telefono, email);
        empresaController.crearEmpresa(nuevaEmpresa);

        System.out.println("Empresa creada exitosamente.");
    }

    public static void listarEmpresas(EmpresaController empresaController) {
        List<Empresa> empresas = empresaController.obtenerTodasLasEmpresas();
        if (empresas.isEmpty()) {
            System.out.println("No hay empresas registradas.");
        } else {
            for (Empresa empresa : empresas) {
                System.out.println(empresa);
            }
        }
    }

    public static void actualizarEmpresa(EmpresaController empresaController, Scanner scanner) {
        System.out.print("ID de la Empresa a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Empresa empresa = empresaController.obtenerEmpresaPorId(id);
        if (empresa == null) {
            System.out.println("Empresa no encontrada.");
            return;
        }

        System.out.print("Nuevo nombre de la Empresa (dejar en blanco para no cambiar): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) {
            empresa.setNombre(nombre);
        }

        System.out.print("Nuevo nombre del Contacto (dejar en blanco para no cambiar): ");
        String contacto = scanner.nextLine();
        if (!contacto.isEmpty()) {
            empresa.setContacto(contacto);
        }

        System.out.print("Nuevo teléfono (dejar en blanco para no cambiar): ");
        String telefono = scanner.nextLine();
        if (!telefono.isEmpty()) {
            empresa.setTelefono(telefono);
        }

        System.out.print("Nuevo email (dejar en blanco para no cambiar): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            empresa.setEmail(email);
        }

        empresaController.actualizarEmpresa(empresa);
        System.out.println("Empresa actualizada exitosamente.");
    }

    public static void eliminarEmpresa(EmpresaController empresaController, Scanner scanner) {
        System.out.print("ID de la Empresa a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        empresaController.eliminarEmpresa(id);
        System.out.println("Empresa eliminada exitosamente.");
    }

    // Menú de gestión de salones
    public static void menuGestionSalones(Connection connection, Scanner scanner) {
        SalonController salonController = new SalonController(connection);
        while (true) {
            System.out.println("=== Gestión de Salones ===");
            System.out.println("1. Crear Salón");
            System.out.println("2. Listar Salones");
            System.out.println("3. Actualizar Salón");
            System.out.println("4. Eliminar Salón");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    crearSalon(salonController, scanner);
                    break;
                case 2:
                    listarSalones(salonController);
                    break;
                case 3:
                    actualizarSalon(salonController, scanner);
                    break;
                case 4:
                    eliminarSalon(salonController, scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    public static void crearSalon(SalonController salonController, Scanner scanner) {
        System.out.println("=== Crear Nuevo Salón ===");

        System.out.print("Nombre del Salón: ");
        String nombre = scanner.nextLine();

        System.out.print("Capacidad: ");
        int capacidad = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        System.out.print("Descripcion: ");
        String descripcion = scanner.nextLine();
        scanner.nextLine(); // Consumir el salto de línea

        Salon nuevoSalon = new Salon(nombre, capacidad, descripcion);
        salonController.crearSalon(nuevoSalon);

        System.out.println("Salón creado exitosamente.");
    }

    public static void listarSalones(SalonController salonController) {
        List<Salon> salones = salonController.obtenerTodosLosSalones();
        if (salones.isEmpty()) {
            System.out.println("No hay salones registrados.");
        } else {
            for (Salon salon : salones) {
                System.out.println(salon);
            }
        }
    }
    public static void actualizarSalon(SalonController salonController, Scanner scanner) {
        System.out.print("ID del Salón a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Salon salon = salonController.obtenerSalonPorId(id);
        if (salon == null) {
            System.out.println("Salón no encontrado.");
            return;
        }

        System.out.print("Nuevo nombre del Salón: ");
        String nombre = scanner.nextLine();
        salon.setNombre(nombre);

        System.out.print("Nueva capacidad del Salón: ");
        int capacidad = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea
        salon.setCapacidad(capacidad);

        salonController.actualizarSalon(salon);
        System.out.println("Salón actualizado exitosamente.");
    }

    public static void eliminarSalon(SalonController salonController, Scanner scanner) {
        System.out.print("ID del Salón a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        salonController.eliminarSalon(id);
        System.out.println("Salón eliminado exitosamente.");
    }

    //Menu Gestion Formato de Salon
    public static void menuGestionFormatoSalon(Connection connection, Scanner scanner) {
        FormatoSalonController formatoSalonController = new FormatoSalonController(connection);
        int opcion;
        do {
            System.out.println("=== Gestión de Formatos de Salón ===");
            System.out.println("1. Crear Formato de Salón");
            System.out.println("2. Listar Formatos de Salón");
            System.out.println("3. Actualizar Formato de Salón");
            System.out.println("4. Eliminar Formato de Salón");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    crearFormatoSalon(formatoSalonController, scanner);
                    break;
                case 2:
                    listarFormatosSalon(formatoSalonController);
                    break;
                case 3:
                    actualizarFormatoSalon(formatoSalonController, scanner);
                    break;
                case 4:
                    eliminarFormatoSalon(formatoSalonController, scanner);
                    break;
                case 0:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }

    public static void crearFormatoSalon(FormatoSalonController formatosalonController, Scanner scanner) {
        System.out.print("Nombre del Formato de Salón: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripcion del Formato de Salón: ");
        String descripcion = scanner.nextLine();

        FormatoSalon formatoSalon = new FormatoSalon(nombre, descripcion);
        formatosalonController.crearFormatoSalon(formatoSalon);
        System.out.println("Formato de Salón creado exitosamente.");
    }

    public static void listarFormatosSalon(FormatoSalonController formatosalonController) {
        List<FormatoSalon> formatosSalon = formatosalonController.obtenerTodosLosFormatosSalon();
        System.out.println("=== Lista de Formatos de Salón ===");
        for (FormatoSalon formatoSalon : formatosSalon) {
            System.out.println(formatoSalon);
        }
    }

    public static void actualizarFormatoSalon(FormatoSalonController formatosalonController, Scanner scanner) {
        System.out.print("ID del Formato de Salón a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        FormatoSalon formatoSalon = formatosalonController.obtenerFormatoSalonPorId(id);
        if (formatoSalon == null) {
            System.out.println("Formato de Salón no encontrado.");
            return;
        }

        System.out.print("Nuevo nombre del Formato de Salón: ");
        String nombre = scanner.nextLine();
        formatoSalon.setNombre(nombre);

        formatosalonController.actualizarFormatoSalon(formatoSalon);
        System.out.println("Formato de Salón actualizado exitosamente.");
    }

    public static void eliminarFormatoSalon(FormatoSalonController formatosalonController, Scanner scanner) {
        System.out.print("ID del Formato de Salón a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        formatosalonController.eliminarFormatoSalon(id);
        System.out.println("Formato de Salón eliminado exitosamente.");
    }

    // Menú de gestión de Servicios Adicionales
    public static void menuGestionServicios(Connection connection, Scanner scanner) throws SQLException {
        ServiciosAdicionalesController serviciosAdicionalesController = new ServiciosAdicionalesController(connection);
        int opcion;
        do {
            System.out.println("=== Gestión de Servicios Adicionales ===");
            System.out.println("1. Crear Servicio Adicional");
            System.out.println("2. Listar Servicios Adicionales");
            System.out.println("3. Actualizar Servicio Adicional");
            System.out.println("4. Eliminar Servicio Adicional");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    crearServicio(serviciosAdicionalesController, scanner);
                    break;
                case 2:
                    listarServicios(serviciosAdicionalesController);
                    break;
                case 3:
                    actualizarServicio(serviciosAdicionalesController, scanner);
                    break;
                case 4:
                    eliminarServicio(serviciosAdicionalesController, scanner);
                    break;
                case 0:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }

    public static void crearServicio(ServiciosAdicionalesController serviciosAdicionalesController, Scanner scanner) {
        System.out.print("Nombre del Servicio Adicional: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción del Servicio Adicional: ");
        String descripcion = scanner.nextLine();

        ServiciosAdicionales servicio = new ServiciosAdicionales(nombre, descripcion);
        serviciosAdicionalesController.crearServicioAdicional(servicio);
        System.out.println("Servicio Adicional creado exitosamente.");
    }

    public static void listarServicios(ServiciosAdicionalesController serviciosController) {
        List<ServiciosAdicionales> servicios = serviciosController.obtenerTodosLosServiciosAdicionales();
        System.out.println("=== Lista de Servicios Adicionales ===");
        for (ServiciosAdicionales servicio : servicios) {
            System.out.println(servicio);
        }
    }

    public static void actualizarServicio(ServiciosAdicionalesController serviciosController, Scanner scanner) throws SQLException {
        System.out.print("ID del Servicio Adicional a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        ServiciosAdicionales servicio = serviciosController.obtenerServicioAdicionalPorId(id);
        if (servicio == null) {
            System.out.println("Servicio Adicional no encontrado.");
            return;
        }

        System.out.print("Nuevo nombre del Servicio Adicional: ");
        String nombre = scanner.nextLine();
        servicio.setNombre(nombre);

        System.out.print("Nueva descripción del Servicio Adicional: ");
        String descripcion = scanner.nextLine();
        servicio.setDescripcion(descripcion);

        serviciosController.actualizarServicioAdicional(servicio);
        System.out.println("Servicio Adicional actualizado exitosamente.");
    }

    public static void eliminarServicio(ServiciosAdicionalesController serviciosController, Scanner scanner) {
        System.out.print("ID del Servicio Adicional a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        serviciosController.eliminarServicioAdicional(id);
        System.out.println("Servicio Adicional eliminado exitosamente.");
    }

    //Menú Gestion Catering
    public static void menuGestionCatering(Connection connection, Scanner scanner) {
        CateringController cateringController = new CateringController(connection);
        int opcion;
        do {
            System.out.println("=== Gestión de Catering ===");
            System.out.println("1. Crear Catering");
            System.out.println("2. Listar Catering");
            System.out.println("3. Actualizar Catering");
            System.out.println("4. Eliminar Catering");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    crearCatering(cateringController, scanner);
                    break;
                case 2:
                    listarCatering(cateringController);
                    break;
                case 3:
                    actualizarCatering(cateringController, scanner);
                    break;
                case 4:
                    eliminarCatering(cateringController, scanner);
                    break;
                case 0:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }

    public static void crearCatering(CateringController cateringController, Scanner scanner) {
        System.out.print("Tipo de Catering: ");
        String tipoCatering = scanner.nextLine();

        System.out.print("Observaciones: ");
        String observaciones = scanner.nextLine();

        Catering catering = new Catering(tipoCatering, observaciones, new ArrayList<OpcionesCatering>());
        cateringController.crearCatering(catering);
        System.out.println("Catering creado exitosamente.");
    }

    public static void listarCatering(CateringController cateringController) {
        List<Catering> caterings = cateringController.obtenerTodosLosCaterings();
        System.out.println("=== Lista de Caterings ===");
        for (Catering catering : caterings) {
            System.out.println(catering);
        }
    }

    public static void actualizarCatering(CateringController cateringController, Scanner scanner) {
        System.out.print("ID del Catering a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Catering catering = cateringController.obtenerCateringPorId(id);
        if (catering == null) {
            System.out.println("Catering no encontrado.");
            return;
        }

        System.out.print("Nuevo tipo de Catering: ");
        String tipoCatering = scanner.nextLine();
        catering.setTipoCatering(tipoCatering);

        cateringController.actualizarCatering(catering);
        System.out.println("Catering actualizado exitosamente.");
    }

    public static void eliminarCatering(CateringController cateringController, Scanner scanner) {
        System.out.print("ID del Catering a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        cateringController.eliminarCatering(id);
        System.out.println("Catering eliminado exitosamente.");
    }

    public static void menuGestionOpcionesCatering(Connection connection, Scanner scanner) throws SQLException {
        OpcionesCateringController opcionesCateringController = new OpcionesCateringController(connection);
        int opcion;
        do {
            System.out.println("=== Gestión de Opciones de Catering ===");
            System.out.println("1. Crear Opción de Catering");
            System.out.println("2. Listar Opciones de Catering");
            System.out.println("3. Actualizar Opción de Catering");
            System.out.println("4. Eliminar Opción de Catering");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    crearOpcionCatering(opcionesCateringController, scanner);
                    break;
                case 2:
                    listarOpcionesCatering(opcionesCateringController);
                    break;
                case 3:
                    actualizarOpcionCatering(opcionesCateringController, scanner);
                    break;
                case 4:
                    eliminarOpcionCatering(opcionesCateringController, scanner);
                    break;
                case 0:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }

    // Métodos para gestión de Opciones de Catering
    public static void crearOpcionCatering(OpcionesCateringController opcionesCateringController, Scanner scanner) throws SQLException {
        System.out.print("Nombre de la Opción de Catering: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción de la Opción de Catering: ");
        String descripcion = scanner.nextLine();

        OpcionesCatering opcionCatering = new OpcionesCatering(nombre, descripcion);
        opcionesCateringController.crearOpcionCatering(opcionCatering);
        System.out.println("Opción de Catering creada exitosamente.");
    }

    public static void listarOpcionesCatering(OpcionesCateringController opcionesCateringController) {
        List<OpcionesCatering> opcionesCatering = opcionesCateringController.obtenerTodasLasOpcionesCatering();
        System.out.println("=== Lista de Opciones de Catering ===");
        for (OpcionesCatering opcion : opcionesCatering) {
            System.out.println(opcion);
        }
    }

    public static void actualizarOpcionCatering(OpcionesCateringController opcionesCateringController, Scanner scanner) {
        System.out.print("ID de la Opción de Catering a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        OpcionesCatering opcionCatering = opcionesCateringController.obtenerOpcionCateringPorId(id);
        if (opcionCatering == null) {
            System.out.println("Opción de Catering no encontrada.");
            return;
        }

        System.out.print("Nuevo nombre de la Opción de Catering: ");
        String nombre = scanner.nextLine();
        opcionCatering.setNombre(nombre);

        System.out.print("Nueva descripción de la Opción de Catering: ");
        String descripcion = scanner.nextLine();
        opcionCatering.setDescripcion(descripcion);

        opcionesCateringController.actualizarOpcionCatering(opcionCatering);
        System.out.println("Opción de Catering actualizada exitosamente.");
    }

    public static void eliminarOpcionCatering(OpcionesCateringController opcionesCateringController, Scanner scanner) {
        System.out.print("ID de la Opción de Catering a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        opcionesCateringController.eliminarOpcionCatering(id);
        System.out.println("Opción de Catering eliminada exitosamente.");
    }
}