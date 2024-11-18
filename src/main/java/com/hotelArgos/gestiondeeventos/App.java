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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static UsuarioController usuarioController;
    private static EventoController eventoController;

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bd_gestiondeeventosapp", "root", "mysql1034");
            usuarioController = new UsuarioController(new UsuarioDAO(connection));
            eventoController = new EventoController(connection);

            EventoDAO eventoDAO = new EventoDAO(connection);
            EventoCalendarioEstado scheduler = new EventoCalendarioEstado(eventoDAO);
            scheduler.start();

            Usuario usuario = login();
            if (usuario != null) {
                System.out.println("Login exitoso!");
                Scanner scanner = new Scanner(System.in);
                menuPrincipal(connection, scanner, usuario);
            } else {
                System.out.println("Nombre o contraseña incorrectos. Saliendo de la aplicación...");
                System.exit(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Usuario login() {
        System.out.println("=== Inicio de sesión ===");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese contraseña: ");
        String password = scanner.nextLine();

        Usuario usuario = usuarioController.obtenerUsuarioPorNombre(nombre);
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }

    public static void menuPrincipal(Connection connection, Scanner scanner, Usuario usuario) throws SQLException {
        while (true) {
            System.out.println("=== Menú Principal ===");
            System.out.println("1. Gestión de Eventos");
            System.out.println("2. Imprimir Evento");
            System.out.println("3. Gestión de Empresas");
            System.out.println("4. Gestión de Formatos de Salón");
            System.out.println("5. Gestión de Servicios Adicionales");
            System.out.println("6. Gestión de Catering");
            System.out.println("7. Gestión de Opciones de Catering");
            if ("ADMIN".equals(usuario.getRol())) {
                System.out.println("8. Gestión de Salones");
                System.out.println("9. Gestión de Usuarios");
            }
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    menuGestionEventos(connection, scanner);
                    break;
                case 2:
                    imprimirEvento(scanner);
                    break;
                case 3:
                    menuGestionEmpresas(connection, scanner);
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
                case 8:
                    if ("ADMIN".equals(usuario.getRol())) {
                        menuGestionSalones(connection, scanner);
                    } else {
                        System.out.println("Opción no válida.");
                    }
                    break;
                case 9:
                    if ("ADMIN".equals(usuario.getRol())) {
                        menuGestionUsuarios(usuarioController, scanner);
                    } else {
                        System.out.println("Opción no válida.");
                    }
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    System.exit(0);
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
            System.out.println("4. Cancelar Evento");
            System.out.println("5. Imprimir Evento");
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
                    cancelarEvento(scanner);
                    break;
                case 5:
                    imprimirEvento(scanner);
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
        EmpresaController empresaController = new EmpresaController(connection);
        SalonController salonController = new SalonController(connection);
        FormatoSalonController formatoSalonController = new FormatoSalonController(connection);
        ServiciosAdicionalesController serviciosAdicionalesController = new ServiciosAdicionalesController(connection);
        CateringController cateringController = new CateringController(connection);
        OpcionesCateringController opcionesCateringController = new OpcionesCateringController(connection);
        System.out.println("=== Crear Nuevo Evento ===");

        LocalDate fechaEvento = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        System.out.print("Nombre del Evento: ");
        String nombre = scanner.nextLine();
        if (nombre == null || nombre.isEmpty()) {
            System.out.println("Error: El nombre del evento no puede estar vacío.");
            return;
        }

        while (fechaEvento == null) {
            System.out.print("Fecha del evento (DD-MM-AAAA): ");
            String fechaInput = scanner.nextLine();
            try {
                fechaEvento = LocalDate.parse(fechaInput, formatter);
                if (fechaEvento.isBefore(LocalDate.now())) {
                    System.out.println("Error: La fecha del evento no puede ser anterior a la fecha actual.");
                    fechaEvento = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato de fecha incorrecto. Por favor, ingrese la fecha en el formato DD-MM-AAAA.");
            }
        }
        scanner.nextLine(); // Consumir el salto de línea


        System.out.print("Hora de inicio (HH:MM): ");
        LocalTime horaInicio = null;
        try {
            horaInicio = LocalTime.parse(scanner.next());
        } catch (DateTimeParseException e) {
            System.out.println("Error: Formato de hora incorrecto. Por favor, ingrese la hora en el formato HH:MM.");
            return;
        }
        scanner.nextLine(); // Consumir el salto de línea

        System.out.print("Hora de finalización (HH:MM): ");
        LocalTime horaFin = null;
        try {
            horaFin = LocalTime.parse(scanner.next());
            if (horaInicio.isAfter(horaFin)) {
                System.out.println("Error: La hora de inicio debe ser anterior a la hora de fin.");
                return;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Error: Formato de hora incorrecto. Por favor, ingrese la hora en el formato HH:MM.");
            return;
        }
        scanner.nextLine(); // Consumir el salto de línea

        System.out.print("Cantidad de personas: ");
        int cantidadPersonas = scanner.nextInt();
        if (cantidadPersonas <= 0) {
            System.out.println("Error: La cantidad de personas debe ser mayor a cero.");
            return;
        }
        scanner.nextLine(); // Consumir el salto de línea

        System.out.print("Observaciones: ");
        String observaciones = scanner.nextLine();




        // Mostrar lista de empresas disponibles
        EmpresaDAO empresaDAO = new EmpresaDAO(connection);
        List<Empresa> empresas = empresaDAO.obtenerTodos();
        if (empresas.isEmpty()) {
            System.out.println("No hay empresas disponibles.");
            System.out.print("¿Desea crear una nueva empresa? (S/N): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("S")) {
                // Crear nueva empresa accediendo al método crearEmpresa() que se encuentra en la clase App
                crearEmpresa(empresaController, scanner);
                // Actualizar la lista de empresas después de crear una nueva
                empresas = empresaDAO.obtenerTodos();
            }
        }
        System.out.println("=== Lista de Empresas Disponibles ===");
        for (Empresa empresa : empresas) {
            System.out.println("ID: " + empresa.getId() + ", Nombre: " + empresa.getNombre());
        }
        Empresa empresa = null;
        Integer empresaId = null;
        while (empresaId == null) {
            System.out.print("ID de la Empresa: ");
            empresaId = scanner.nextInt();
            scanner.nextLine();
            empresa = empresaDAO.obtenerPorId(empresaId);
            if (empresa == null) {
                System.out.println("Error: No se encontró la empresa con el ID especificado.");
                empresaId = null;
            }
        }

// Mostrar lista de salones disponibles

        SalonDAO salonDAO = new SalonDAO(connection);
        List<Salon> salones = salonDAO.obtenerTodos();
        if (salones.isEmpty()) {
            System.out.println("No hay salones disponibles.");
            System.out.print("¿Desea crear un nuevo salón? (S/N): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("S")) {
                // Crear nuevo salón accediendo al método crearSalon() que se encuentra en la clase App
                crearSalon(salonController, scanner);
                // Actualizar la lista de salones después de crear uno nuevo
                salones = salonDAO.obtenerTodos();
            }
        }
        System.out.println("=== Lista de Salones Disponibles ===");
        for (Salon salon : salones) {
            System.out.println("ID: " + salon.getId() + ", Nombre: " + salon.getNombre());
        }
        Salon salon = null;
        Integer salonId = null;
        while (salonId == null) {
            System.out.print("ID del Salón: ");
            salonId = scanner.nextInt();
            scanner.nextLine();
            salon = salonDAO.obtenerPorId(salonId);
            if (salon == null) {
                System.out.println("Error: No se encontró el salón con el ID especificado.");
                salonId = null;
            }
        }

// Mostrar lista de formatos de salón disponibles
        FormatoSalonDAO formatoSalonDAO = new FormatoSalonDAO(connection);
        List<FormatoSalon> formatosSalon = formatoSalonDAO.obtenerTodos();
        if (formatosSalon.isEmpty()) {
            System.out.println("No hay formatos de salón disponibles.");
            System.out.print("¿Desea crear un nuevo formato de salón? (S/N): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("S")) {
                // Crear nuevo formato de salón accediendo al método crearFormatoSalon() que se encuentra en la clase App
                crearFormatoSalon(formatoSalonController, scanner);
                // Actualizar la lista de formatos de salón después de crear uno nuevo
                formatosSalon = formatoSalonDAO.obtenerTodos();
            }
        }
        System.out.println("=== Lista de Formatos de Salón Disponibles ===");
        for (FormatoSalon formatoSalon : formatosSalon) {
            System.out.println("ID: " + formatoSalon.getId() + ", Nombre: " + formatoSalon.getNombre());
        }
        FormatoSalon formatoSalon = null;
        Integer formatoSalonId = null;
        while (formatoSalonId == null) {
            System.out.print("ID del Formato de Salón: ");
            formatoSalonId = scanner.nextInt();
            scanner.nextLine();
            formatoSalon = formatoSalonDAO.obtenerPorId(formatoSalonId);
            if (formatoSalon == null) {
                System.out.println("Error: No se encontró el formato de salón con el ID especificado.");
                formatoSalonId = null;
            }
        }

        // Mostrar lista de servicios adicionales disponibles
        ServiciosAdicionalesDAO serviciosAdicionalesDAO = new ServiciosAdicionalesDAO(connection);
        List<ServiciosAdicionales> serviciosAdicionalesList = serviciosAdicionalesDAO.obtenerTodos();
        if (serviciosAdicionalesList.isEmpty()) {
            System.out.println("No hay servicios adicionales disponibles.");
        }

        System.out.println("=== Lista de servicios adicionales disponibles ===");
        for (ServiciosAdicionales servicio : serviciosAdicionalesList) {
            System.out.println("ID: " + servicio.getId() + ", Nombre: " + servicio.getNombre());
        }
        // Permitir al usuario seleccionar uno o varios servicios adicionales
        List<ServiciosAdicionales> serviciosAdicionalesSeleccionados = new ArrayList<>();
        System.out.print("IDs de los servicios adicionales (separados por comas): ");
        String[] idsServiciosAdicionales = scanner.nextLine().split(",");
        for (String id : idsServiciosAdicionales) {
            ServiciosAdicionales serviciosAdicionales = serviciosAdicionalesDAO.obtenerPorId(Integer.parseInt(id.trim()));
            if (serviciosAdicionales != null) {
                serviciosAdicionalesSeleccionados.add(serviciosAdicionales);
            } else {
                System.out.println("Error: No se encontró el servicio adicional con el ID especificado.");
                return;
            }
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
        System.out.println("=== Lista de Opciones de Catering Disponibles ===");
        OpcionesCateringDAO opcionesCateringDAO = new OpcionesCateringDAO(connection);
        List<OpcionesCatering> opcionesCatering = opcionesCateringDAO.obtenerTodos();
        if (opcionesCatering.isEmpty()) {
            System.out.println("No hay opciones de catering disponibles.");
            return;
        }

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
                    System.out.println("Error: No se encontró la opción de catering con el ID especificado.");
                    return;
                }
            }
            catering.setOpcionesCatering(opcionesSeleccionadas);
        }

        // Asigna fechas de creación y actualización automáticas (puedes modificar esto según lo necesites)
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Crear el nuevo evento con todos los datos recolectados.
        Evento nuevoEvento = new Evento(nombre, fechaEvento, horaInicio, horaFin, cantidadPersonas, observaciones, empresa,salon,formatoSalon, serviciosAdicionalesSeleccionados, cateringsSeleccionados, createdAt, updatedAt);
        eventoController.crearEvento(nuevoEvento);

        System.out.println("Evento creado exitosamente.");

        System.out.print("¿Desea imprimir el informe del evento en PDF? (S/N): ");
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("S")) {
            String filePath = "informe_evento_" + nuevoEvento.getId() + ".pdf";
            eventoController.generarInformePDF(nuevoEvento);
        }
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

    public static void actualizarEvento(EventoController eventoController, Scanner scanner) throws SQLException {
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
            try {
                LocalDate fecha = LocalDate.parse(fechaStr);
                evento.setFecha(fecha);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha incorrecto. Por favor, ingrese la fecha en el formato AAAA-MM-DD.");

            }
        }

        System.out.print("Nueva hora de inicio (HH:MM) (dejar en blanco para no cambiar): ");
        String horaInicioStr = scanner.nextLine();
        if (!horaInicioStr.isEmpty()) {
            try {
                LocalTime horaInicio = LocalTime.parse(horaInicioStr);
                evento.setHoraInicio(horaInicio);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de hora incorrecto. Por favor, ingrese la hora en el formato HH:MM.");
            }
        }

        System.out.print("Nueva hora de finalización (HH:MM) (dejar en blanco para no cambiar): ");
        String horaFinStr = scanner.nextLine();
        if (!horaFinStr.isEmpty()) {
            try {
                LocalTime horaFin = LocalTime.parse(horaFinStr);
                evento.setHoraFin(horaFin);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de hora incorrecto. Por favor, ingrese la hora en el formato HH:MM.");
            }
        }

        System.out.print("Nueva cantidad de personas (dejar en blanco para no cambiar): ");
        String cantidadPersonasStr = scanner.nextLine();
        if (!cantidadPersonasStr.isEmpty()) {
            try {
                int cantidadPersonas = Integer.parseInt(cantidadPersonasStr);
                evento.setCantidadPersonas(cantidadPersonas);
            } catch (NumberFormatException e) {
                System.out.println("Formato de cantidad de personas incorrecto. Por favor, ingrese un número válido.");
            }
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

        System.out.print("Nueva empresa (ID) (dejar en blanco para no cambiar): ");
        String empresaIdStr = scanner.nextLine();
        if (!empresaIdStr.isEmpty()) {
            try {
                int empresaId = Integer.parseInt(empresaIdStr);
                Empresa empresa = new EmpresaDAO(eventoController.getConnection()).obtenerPorId(empresaId);
                if (empresa == null) {
                    System.out.println("Empresa no encontrada.");
                    return;
                }
                evento.setEmpresa(empresa);
            } catch (NumberFormatException e) {
                System.out.println("Formato de ID de empresa incorrecto. Por favor, ingrese un número válido.");
                return;
            }
        }

        System.out.print("Nuevo salón (ID) (dejar en blanco para no cambiar): ");
        String salonIdStr = scanner.nextLine();
        if (!salonIdStr.isEmpty()) {
            try {
                int salonId = Integer.parseInt(salonIdStr);
                Salon salon = new SalonDAO(eventoController.getConnection()).obtenerPorId(salonId);
                if (salon == null) {
                    System.out.println("Salón no encontrado.");
                    return;
                }
                evento.setSalon(salon);
            } catch (NumberFormatException e) {
                System.out.println("Formato de ID de salón incorrecto. Por favor, ingrese un número válido.");
                return;
            }
        }

        System.out.print("Nuevo formato de salón (ID) (dejar en blanco para no cambiar): ");
        String formatoSalonIdStr = scanner.nextLine();
        if (!formatoSalonIdStr.isEmpty()) {
            try {
                int formatoSalonId = Integer.parseInt(formatoSalonIdStr);
                FormatoSalon formatoSalon = new FormatoSalonDAO(eventoController.getConnection()).obtenerPorId(formatoSalonId);
                if (formatoSalon == null) {
                    System.out.println("Formato de salón no encontrado.");
                    return;
                }
                evento.setFormatoSalon(formatoSalon);
            } catch (NumberFormatException e) {
                System.out.println("Formato de ID de formato de salón incorrecto. Por favor, ingrese un número válido.");
                return;
            }
        }

        // Actualizar servicios adicionales
        System.out.print("Actualizar servicios adicionales (S/N): ");
        String actualizarServicios = scanner.nextLine();
        if (actualizarServicios.equalsIgnoreCase("S")) {
            List<ServiciosAdicionales> serviciosAdicionales = new ArrayList<>();
            while (true) {
                System.out.print("ID del servicio adicional (0 para terminar): ");
                int servicioId = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea
                if (servicioId == 0) break;
                ServiciosAdicionales servicio = new ServiciosAdicionalesDAO(eventoController.getConnection()).obtenerPorId(servicioId);
                if (servicio != null) {
                    serviciosAdicionales.add(servicio);
                } else {
                    System.out.println("Servicio adicional no encontrado.");
                }
            }
            evento.setServiciosAdicionales(serviciosAdicionales);
        }

        // Actualizar caterings y opciones de catering
        System.out.print("Actualizar caterings (S/N): ");
        String actualizarCaterings = scanner.nextLine();
        if (actualizarCaterings.equalsIgnoreCase("S")) {
            List<Catering> caterings = new ArrayList<>();
            while (true) {
                System.out.print("ID del catering (0 para terminar): ");
                int cateringId = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea
                if (cateringId == 0) break;
                Catering catering = new CateringDAO(eventoController.getConnection()).obtenerPorId(cateringId);
                if (catering != null) {
                    List<OpcionesCatering> opcionesCatering = new ArrayList<>();
                    while (true) {
                        System.out.print("ID de la opción de catering (0 para terminar): ");
                        int opcionId = scanner.nextInt();
                        scanner.nextLine(); // Consumir el salto de línea
                        if (opcionId == 0) break;
                        OpcionesCatering opcion = new OpcionesCateringDAO(eventoController.getConnection()).obtenerPorId(opcionId);
                        if (opcion != null) {
                            opcionesCatering.add(opcion);
                        } else {
                            System.out.println("Opción de catering no encontrada.");
                        }
                    }
                    catering.setOpcionesCatering(opcionesCatering);
                    caterings.add(catering);
                } else {
                    System.out.println("Catering no encontrado.");
                }
            }
            evento.setCaterings(caterings);
        }

        eventoController.actualizarEvento(evento);
        System.out.println("Evento actualizado exitosamente.");

        System.out.print("¿Desea imprimir el informe del evento en PDF? (S/N): ");
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("S")) {
            String filePath = "informe_evento_" + evento.getId() + ".pdf";
            eventoController.generarInformePDF(evento);
        }
    }



    public static void eliminarEvento(EventoController eventoController, Scanner scanner) {
        System.out.print("ID del Evento a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Evento evento = eventoController.obtenerEventoPorId(id);
        if (evento == null) {
            System.out.println("Evento no encontrado.");
            return;
        }

        eventoController.eliminarEvento(id);
        System.out.println("Evento eliminado exitosamente.");
    }

    public static void cancelarEvento(Scanner scanner) {
        System.out.print("ID del Evento a cancelar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Evento evento = eventoController.obtenerEventoPorId(id);
        if (evento == null) {
            System.out.println("Evento no encontrado.");
            return;
        }

        eventoController.cancelarEvento(id);
        System.out.println("Evento cancelado exitosamente.");
    }
    public static void imprimirEvento(Scanner scanner) {
        System.out.println("=== Imprimir Evento ===");
        System.out.print("ID del Evento a imprimir: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea
        Evento evento = eventoController.obtenerEventoPorId(id);
        if (evento == null) {
            System.out.println("Evento no encontrado.");
            return;
        }
        eventoController.generarInformePDF(evento);
        System.out.println("Informe del evento impreso exitosamente.");
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

        System.out.print("Descripción: ");
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

        System.out.print("Nuevo nombre del Salón (dejar en blanco para no cambiar): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) {
            salon.setNombre(nombre);
        }

        System.out.print("Nueva capacidad del Salón (dejar en blanco para no cambiar): ");
        String capacidadInput = scanner.nextLine();
        if (!capacidadInput.isEmpty()) {
            int capacidad = Integer.parseInt(capacidadInput);
            salon.setCapacidad(capacidad);
        }

        System.out.print("Nueva descripción del Salón (dejar en blanco para no cambiar): ");
        String descripcion = scanner.nextLine();
        if (!descripcion.isEmpty()) {
            salon.setDescripcion(descripcion);
        }

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

        System.out.print("Descripción del Formato de Salón: ");
        String descripcion = scanner.nextLine();

        System.out.print("Imagen del Formato de Salón: ");
        String imagen = scanner.nextLine();

        FormatoSalon formatoSalon = new FormatoSalon(nombre, descripcion, imagen);
        formatosalonController.crearFormatoSalon(formatoSalon);
        System.out.println("Formato de Salón creado exitosamente.");
    }

    public static void listarFormatosSalon(FormatoSalonController formatoSalonController) {
        List<FormatoSalon> formatosSalon = formatoSalonController.obtenerTodosLosFormatosSalon();
        System.out.println("=== Lista de Formatos de Salón ===");
        if (formatosSalon.isEmpty()) {
            System.out.println("No hay formatos de salón registrados.");
        } else {
            for (FormatoSalon formatoSalon : formatosSalon) {
                System.out.println("ID: " + formatoSalon.getId() + ", Nombre: " + formatoSalon.getNombre() + ", Descripción: " + formatoSalon.getDescripcion() + ", Imagen: " + formatoSalon.getImagenFormatoSalon());
            }
        }
    }

    public static void actualizarFormatoSalon(FormatoSalonController formatoSalonController, Scanner scanner) {
        System.out.print("ID del Formato de Salón a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        FormatoSalon formatoSalon = formatoSalonController.obtenerFormatoSalonPorId(id);
        if (formatoSalon == null) {
            System.out.println("Formato de Salón no encontrado.");
            return;
        }

        System.out.print("Nuevo nombre del Formato de Salón (dejar en blanco para no cambiar): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) {
            formatoSalon.setNombre(nombre);
        }

        System.out.print("Nueva descripción del Formato de Salón (dejar en blanco para no cambiar): ");
        String descripcion = scanner.nextLine();
        if (!descripcion.isEmpty()) {
            formatoSalon.setDescripcion(descripcion);
        }

        System.out.print("Nueva imagen del Formato de Salón (dejar en blanco para no cambiar): ");
        String imagen = scanner.nextLine();
        if (!imagen.isEmpty()) {
            formatoSalon.setImagenFormatoSalon(imagen);
        }

        formatoSalonController.actualizarFormatoSalon(formatoSalon);
        System.out.println("Formato de Salón actualizado exitosamente.");
    }

    public static void eliminarFormatoSalon(FormatoSalonController formatoSalonController, Scanner scanner) {
        System.out.print("ID del Formato de Salón a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        FormatoSalon formatoSalon = formatoSalonController.obtenerFormatoSalonPorId(id);
        if (formatoSalon == null) {
            System.out.println("Formato de Salón no encontrado.");
            return;
        }

        formatoSalonController.eliminarFormatoSalon(id);
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
        if (servicios.isEmpty()) {
            System.out.println("No hay Servicios Adicionales registrados.");
        } else {
            for (ServiciosAdicionales servicio : servicios) {
                System.out.println(servicio);
            }
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

        System.out.print("Nuevo nombre del Servicio Adicional (dejar en blanco para no cambiar): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) {
            servicio.setNombre(nombre);
        }

        System.out.print("Nueva descripción del Servicio Adicional (dejar en blanco para no cambiar): ");
        String descripcion = scanner.nextLine();
        if (!descripcion.isEmpty()) {
            servicio.setDescripcion(descripcion);
        }


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
        if (caterings.isEmpty()) {
            System.out.println("No hay caterings registrados.");
        } else {
            for (Catering catering : caterings) {
                System.out.println(catering);
            }
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

        System.out.print("Nuevo tipo de Catering (dejar en blanco para no cambiar): ");
        String tipoCatering = scanner.nextLine();
        if (!tipoCatering.isEmpty()) {
            catering.setTipoCatering(tipoCatering);
        }


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
        if (opcionesCatering.isEmpty()) {
            System.out.println("No hay opciones de catering registrados.");
        } else {
            for (OpcionesCatering opcionCatering : opcionesCatering) {
                System.out.println(opcionCatering);
            }
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

        System.out.print("Nuevo nombre de la Opción de Catering (dejar en blanco para no cambiar): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) {
            opcionCatering.setNombre(nombre);
        }


        System.out.print("Nueva descripción de la Opción de Catering (dejar en blanco para no cambiar): ");
        String descripcion = scanner.nextLine();
        if (!descripcion.isEmpty()) {
            opcionCatering.setDescripcion(descripcion);
        }

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

    //Menu Gestion de Usuarios
    private static void menuGestionUsuarios(UsuarioController usuarioController, Scanner scanner) {
        int opcion;
        do {
            System.out.println("=== Gestión de Usuarios ===");
            System.out.println("1. Crear Usuario");
            System.out.println("2. Listar Usuarios");
            System.out.println("3. Actualizar Usuario");
            System.out.println("4. Eliminar Usuario");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    crearUsuario(usuarioController, scanner);
                    break;
                case 2:
                    listarUsuarios(usuarioController);
                    break;
                case 3:
                    actualizarUsuario(usuarioController, scanner);
                    break;
                case 4:
                    eliminarUsuario(usuarioController, scanner);
                    break;
                case 0:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }
    // Métodos para gestión de Usuarios
    private static void crearUsuario(UsuarioController usuarioController, Scanner scanner) {
        System.out.print("Nombre del Usuario: ");
        String nombre = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        System.out.print("Rol: ");
        String rol = scanner.nextLine();

        Usuario usuario = new Usuario(nombre, email, password, rol);
        usuarioController.crearUsuario(usuario);
        System.out.println("Usuario creado exitosamente.");
    }

    private static void listarUsuarios(UsuarioController usuarioController) {
        List<Usuario> usuarios = usuarioController.obtenerTodosLosUsuarios();
        System.out.println("=== Lista de Usuarios ===");
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            for (Usuario usuario : usuarios) {
                System.out.println(usuario);
            }
        }
    }

    private static void actualizarUsuario(UsuarioController usuarioController, Scanner scanner) {
        System.out.print("ID del Usuario a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Usuario usuario = usuarioController.obtenerUsuarioPorId(id);
        if (usuario == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        System.out.print("Nuevo nombre del Usuario (dejar en blanco para no cambiar): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty()) {
            usuario.setNombre(nombre);
        }

        System.out.print("Nuevo email del Usuario (dejar en blanco para no cambiar): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            usuario.setEmail(email);
        }

        System.out.print("Nueva contraseña del Usuario (dejar en blanco para no cambiar): ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) {
            usuario.setPassword(password);
        }

        System.out.print("Nuevo rol del Usuario (dejar en blanco para no cambiar): ");
        String rol = scanner.nextLine();
        if (!rol.isEmpty()) {
            usuario.setRol(rol);
        }

        usuarioController.actualizarUsuario(usuario);
        System.out.println("Usuario actualizado exitosamente.");
    }

    private static void eliminarUsuario(UsuarioController usuarioController, Scanner scanner) {
        System.out.print("ID del Usuario a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        usuarioController.eliminarUsuario(id);
        System.out.println("Usuario eliminado exitosamente.");
    }
}