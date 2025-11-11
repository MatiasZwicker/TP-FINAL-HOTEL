package manejoJSON;

import Clases.*;
import Controladores.Sistema;
import Enums.Rol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Consola {

    // Atributo principal, contiene toda la lógica del sistema (clientes, habitaciones, reservas, etc.)
    private final Sistema sistema;

    private final Scanner sc = new Scanner(System.in);

    // Constructor: recibe una instancia de Sistema para trabajar sobre los mismos datos
    public Consola(Sistema sistema) {
        this.sistema = sistema;
    }

    // Método principal que se ejecuta al iniciar el programa
    public void iniciar() throws JSONException {
        System.out.println("\n=== 🏨 BIENVENIDOS AL HOTEL ===");

        Persona usuarioActual = null; // puede ser Cliente o Empleado
        boolean autenticado = false;

        while (!autenticado) {
            System.out.println("\n¿Usted ya está registrado?");
            System.out.println("1 - Sí, iniciar sesión");
            System.out.println("2 - No, registrarme");
            System.out.println("0 - Salir");

            int opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1 -> { // LOGIN
                    usuarioActual = loginUsuario();
                    if (usuarioActual != null) {
                        System.out.println("✅ Bienvenido, " + usuarioActual.getNombreCompleto() + "!");
                        autenticado = true;
                    } else {
                        System.out.println("❌ DNI no encontrado. Intente nuevamente.");
                    }
                }

                case 2 -> { // REGISTRO
                    System.out.println("\n¿Desea registrarse como?");
                    System.out.println("1 - Cliente");
                    System.out.println("2 - Empleado");

                    int tipo = leerEntero("Opción: ");
                    switch (tipo) {
                        case 1 -> {
                            Cliente nuevoCliente = agregarCliente();
                            if (nuevoCliente != null) {
                                usuarioActual = nuevoCliente;
                                autenticado = true;
                                System.out.println("✅ Registro exitoso. Bienvenido " + nuevoCliente.getNombreCompleto() + "!");
                                ManejoJSONCliente.guardar(nuevoCliente);
                            }
                        }
                        case 2 -> {
                            Empleado nuevoEmpleado = agregarEmpleado();
                            if (nuevoEmpleado != null) {
                                usuarioActual = nuevoEmpleado;
                                autenticado = true;
                                System.out.println("✅ Registro exitoso. Bienvenido " + nuevoEmpleado.getNombreCompleto() + "!");
                                ManejoJSONEmpleado.guardar(nuevoEmpleado);
                            }
                        }
                        default -> System.out.println("⚠️ Opción inválida.");
                    }
                }

                case 0 -> {
                    System.out.println("👋 Gracias por visitar el hotel. ¡Hasta luego!");
                    return;
                }

                default -> System.out.println("⚠️ Opción inválida.");
            }
        }

        // --- MENÚ SEGÚN TIPO DE USUARIO ---
        if (usuarioActual instanceof Empleado empleado) {
            menuEmpleado(empleado);
        } else if (usuarioActual instanceof Cliente cliente) {
            menuCliente(cliente);
        }
    }

    ///  funcion para logearse
    private Persona loginUsuario() {
        int dni = leerEntero("Ingrese su DNI: ");

        // Buscar primero si es empleado
        Optional<Empleado> empleadoOpt = sistema.buscarEmpleadoPorDni(dni);
        if (empleadoOpt.isPresent()) return empleadoOpt.get();

        // Si no, buscar si es cliente
        Optional<Cliente> clienteOpt = sistema.buscarClientePorDni(dni);
        return clienteOpt.orElse(null);
    }

    /// -------------------------MENUS---------------------------------
    private void menuCliente(Cliente cliente) throws JSONException {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n=== MENÚ CLIENTE ===");
            System.out.println("1 - Ver habitaciones disponibles");
            System.out.println("2 - Realizar una reserva");
            System.out.println("0 - Salir");

            int opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1 -> mostrarHabitaciones();
                case 2 -> agregarReserva(cliente);
                case 0 -> salir = true;
                default -> System.out.println("⚠️ Opción inválida.");
            }
        }
    }

    private void menuEmpleado(Empleado empleado) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n=== MENÚ EMPLEADO ===");
            System.out.println("1 - Agregar habitación");
            System.out.println("2 - Ver listado de reservas");
            System.out.println("3 - Ver listado de clientes");
            System.out.println("0 - Salir");

            int opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1 -> agregarHabitacion();

                case 2 -> mostrarReservas();
                case 3 -> mostrarClientes();
                case 0 -> salir = true;
                default -> System.out.println("⚠️ Opción inválida.");
            }
        }
    }

    // ================= EMPLEADOS =================

    // Método que crea y agrega un nuevo empleado
    private Empleado agregarEmpleado() throws JSONException {
        System.out.println("\n--- Nuevo Empleado ---");

        int dni = leerEntero("DNI: ");

        // 🔹 Verificamos si el DNI ya existe en hotel.json
        JSONObject personaExistente = sistema.buscarPorDNI(dni, "hotel.json");


        if (personaExistente != null) {

            // Si ya era un empleado, devolvemos el empleado encontrado
            if (personaExistente.getString("tipo").equals("empleado")) {
                // Podés reconstruir el objeto Empleado si necesitás devolverlo
                return new Empleado(
                        personaExistente.getString("telefono"),
                        personaExistente.getInt("dni"),
                        personaExistente.getString("email"),
                        personaExistente.getString("apellido"),
                        personaExistente.getString("nombre"),
                        Rol.valueOf(personaExistente.getString("rol")) // o como guardes el rol
                );
            } else {
                // Si era un cliente, podrías impedir crear un empleado con ese mismo DNI
                System.out.println("El DNI ya está registrado como cliente. No se puede registrar como empleado.");
                return null;
            }
        }

        // 🔹 Si el DNI no existe, se sigue con el registro normal
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String email = leerTexto("Email: ");
        String telefono = leerTexto("Teléfono: ");
        Rol cargo = leerRol();

        Empleado nuevo = new Empleado(telefono, dni, email, apellido, nombre, cargo);
        sistema.agregarEmpleado(nuevo);
        ManejoJSONEmpleado.guardar(nuevo);
        return nuevo;
    }

    private Rol leerRol() {
        System.out.println("Cargos disponibles:");
        for (Rol r : Rol.values()) {
            System.out.println("- " + r.name());
        }

        while (true) {
            String texto = leerTexto("Ingrese el cargo (ADMIN, COCINA, RECEPCIONISTA, LIMPIEZA): ").toUpperCase();
            try {
                return Rol.valueOf(texto);
            } catch (IllegalArgumentException e) {
                System.out.println("⚠️ Cargo inválido. Intente nuevamente.");
            }
        }
    }
    // ================= CLIENTES =================

    // Método que crea y agrega un nuevo cliente
    private Cliente agregarCliente() throws JSONException {
        System.out.println("\n--- Nuevo Cliente ---");

        int dni = leerEntero("DNI: ");

        // 🔹 Verificamos si el DNI ya existe en hotel.json
        JSONObject personaExistente = sistema.buscarPorDNI(dni, "hotel.json");

        if (personaExistente != null) {
                System.out.println("Bienvenido " + personaExistente.getString("nombre") +
                        ", usted ya estaba registrado como " + personaExistente.getString("tipo") + ".");


            // Si ya era cliente, devolvemos el cliente encontrado
            if (personaExistente.getString("tipo").equals("cliente")) {
                return new Cliente(
                        personaExistente.getString("telefono"),
                        personaExistente.getInt("dni"),
                        personaExistente.getString("email"),
                        personaExistente.getString("apellido"),
                        personaExistente.getString("nombre"),
                        personaExistente.getString("nacionalidad"),
                        false // no es empleado
                );
            } else {
                // Si ya era empleado, no permitir registrar como cliente

                System.out.println("El DNI ya está registrado como empleado. No se puede registrar como cliente.");
                return null;
            }

        }

        // 🔹 Si el DNI no existe, procedemos al registro normal
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String email = leerTexto("Email: ");
        String telefono = leerTexto("Teléfono: ");
        String nacionalidad = leerTexto("Nacionalidad: ");

        Cliente nuevoCliente = new Cliente(telefono, dni, email, apellido, nombre, nacionalidad, false);
        sistema.agregarCliente(nuevoCliente);

        return nuevoCliente;
    }

    private void mostrarClientes() {
        System.out.println("\n=== LISTADO DE CLIENTES ===");

        if (sistema.getClientes().isEmpty()) {
            System.out.println("(sin clientes registrados)");
            return;
        }

        for (Cliente c : sistema.getClientes()) {
            System.out.println(
                    "👤 " + c.getNombreCompleto() +
                            " | DNI: " + c.getDni() +
                            " | Email: " + c.getEmail() +
                            " | Nacionalidad: " + c.getNacionalidad()
            );
        }
    }

    // ================= HABITACIONES =================

    // Método que agrega una nueva habitación
    private void agregarHabitacion() {
        System.out.println("\n--- Nueva Habitación ---");

        // Pido los datos básicos de la habitación
        int numero = leerEntero("Número de Habitación: ");
        String tipo = leerTexto("Tipo (SIMPLE/DOBLE/SUITE): ");
        double precioXnoche = leerDouble();

        // Creo y agrego la habitación
        Habitacion nuevaHabitacion = new Habitacion(numero, tipo, precioXnoche);
        sistema.agregarHabitacion(nuevaHabitacion);


        ManejoJSONHabitacion.guardar(nuevaHabitacion);
    }

    private void mostrarHabitaciones() {
        System.out.println("\n=== LISTADO DE HABITACIONES ===");

        if (sistema.getHabitaciones().isEmpty()) {
            System.out.println("(sin habitaciones registradas)");
            return;
        }

        for (Habitacion h : sistema.getHabitaciones()) {
            System.out.println("Número: " + h.getNumero() +
                    " | Tipo: " + h.getTipo() +
                    " | Estado: " + (h.isDisponible() ? "Disponible ✅" : "Ocupada ❌") +
                    " | Precio por noche: " + h.getPrecioxNoche());
        }
    }

    // ================= RESERVAS =================

    private void agregarReserva(Cliente clienteActual) throws JSONException {
        System.out.println("\n--- Nueva Reserva ---");

        if (sistema.getHabitaciones().isEmpty()) {
            System.out.println("⚠️ No hay habitaciones cargadas en el sistema.");
            return;
        }

        // --- Paso 1: Pedir fechas ---
        LocalDate desde = null;
        LocalDate hasta = null;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            try {
                String texto = leerTexto("Fecha inicio (YYYY-MM-DD): ");
                desde = LocalDate.parse(texto, formato);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato inválido. Ingrese la fecha como YYYY-MM-DD.");
            }
        }

        while (true) {
            try {
                String texto = leerTexto("Fecha fin (YYYY-MM-DD): ");
                hasta = LocalDate.parse(texto, formato);

                if (hasta.isBefore(desde)) {
                    System.out.println("❌ La fecha de fin no puede ser anterior a la de inicio.");
                } else {
                    break;
                }
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato inválido. Ingrese la fecha como YYYY-MM-DD.");
            }
        }

        final LocalDate fechaDesde = desde;
        final LocalDate fechaHasta = hasta;

        // --- Paso 2: Filtrar habitaciones disponibles leyendo el JSON ---
        JSONObject hotelJSON = JSONUtiles.leerObjeto("hotel.json");
        JSONArray reservasJSON = hotelJSON.optJSONArray("reservas");
        if (reservasJSON == null) reservasJSON = new JSONArray(); // si no hay reservas aún

        JSONArray finalReservasJSON = reservasJSON;
        List<Habitacion> disponibles = sistema.getHabitaciones().stream()
                .filter(h -> {
                    try {
                        boolean haySolapamiento = false;
                        for (int i = 0; i < finalReservasJSON.length(); i++) {
                            JSONObject r = finalReservasJSON.getJSONObject(i);
                            if (r.getString("habitacionId").equals(h.getId().toString()) && !r.getBoolean("cancelada")) {
                                LocalDate rDesde = LocalDate.parse(r.getString("desde"));
                                LocalDate rHasta = LocalDate.parse(r.getString("hasta"));

                                if (!(rHasta.isBefore(fechaDesde) || rDesde.isAfter(fechaHasta))) {
                                    haySolapamiento = true;
                                    break;
                                }
                            }
                        }
                        return !haySolapamiento;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false; // si hay error, consideramos que no está disponible
                    }
                })
                .toList();

        if (disponibles.isEmpty()) {
            System.out.println("⚠️ No hay habitaciones disponibles entre " + desde + " y " + hasta + ".");
            return;
        }

        System.out.println("\nHabitaciones disponibles entre " + desde + " y " + hasta + ":");
        disponibles.forEach(h -> System.out.println(
                "Número: " + h.getNumero() +
                        " | Tipo: " + h.getTipo() +
                        " | Precio por noche: $" + h.getPrecioxNoche()
        ));

        // --- Paso 3: Elegir habitación ---
        int numHabitacion = leerEntero("Ingrese el número de habitación a reservar: ");
        Optional<Habitacion> habitacionOpt = disponibles.stream()
                .filter(h -> h.getNumero() == numHabitacion)
                .findFirst();

        if (habitacionOpt.isEmpty()) {
            System.out.println("❌ No se encontró una habitación disponible con ese número.");
            return;
        }

        Habitacion habitacion = habitacionOpt.get();

        // --- Paso 4: Crear y guardar reserva ---
        Reserva nuevaReserva = new Reserva(
                clienteActual.getNombreCompleto(),
                String.valueOf(clienteActual.getDni()),
                desde,
                hasta,
                habitacion.getId()
        );

        // Guardar en el sistema y en JSON
        sistema.agregarReserva(nuevaReserva);
        ManejoJSONReserva.guardar(nuevaReserva);

        // También actualizar la habitación en JSON agregando el ID de la reserva
        ManejoJSONHabitacion.agregarReservaIdEnJSON(habitacion, nuevaReserva.getId());

        System.out.println("✅ Reserva creada correctamente para " + clienteActual.getNombreCompleto());
    }



    private void mostrarReservas() {
        System.out.println("\n=== LISTADO DE RESERVAS ===");

        if (sistema.getReservas().isEmpty()) {
            System.out.println("(sin reservas registradas)");
            return;
        }

        for (Reserva r : sistema.getReservas()) {
            // Buscar el cliente y la habitación asociados a la reserva
            Cliente cliente = sistema.buscarClientePorDni(Integer.parseInt(r.getDocumento())).orElse(null);
            Habitacion habitacion = sistema.buscarHabitacionPorId(r.getHabitacionId()).orElse(null);

            String nombreCliente = (cliente != null) ? cliente.getNombreCompleto() : "Desconocido";
            String numHabitacion = (habitacion != null) ? String.valueOf(habitacion.getNumero()) : "N/A";

            System.out.println(
                    "🛎️ Reserva de: " + nombreCliente +
                            " | Habitación: " + numHabitacion +
                            " | Desde: " + r.getDesde() +
                            " | Hasta: " + r.getHasta()
            );
        }
    }

    // ================= MOSTRAR DATOS =================

    private void mostrarTodo() {
        System.out.println("\n=== CLIENTES ===");
        if (sistema.getClientes().isEmpty()) {
            System.out.println("(sin datos)");
        } else {
            sistema.getClientes().forEach(c -> {
                System.out.println("---------------------------");
                System.out.println("Nombre completo: " + c.getNombreCompleto());
                System.out.println("DNI: " + c.getDni());
                System.out.println("Email: " + c.getEmail());
                System.out.println("Nacionalidad: " + c.getNacionalidad());
            });
        }

        System.out.println("\n=== HABITACIONES ===");
        if (sistema.getHabitaciones().isEmpty()) {
            System.out.println("(sin datos)");
        } else {
            sistema.getHabitaciones().forEach(h -> {
                System.out.println("---------------------------");
                System.out.println("Número: " + h.getNumero());
                System.out.println("Tipo: " + h.getTipo());
            });
        }

        System.out.println("\n=== RESERVAS ===");
        if (sistema.getReservas().isEmpty()) {
            System.out.println("(sin datos)");
        } else {
            sistema.getReservas().forEach(r -> {
                // Busco la habitación correspondiente para mostrar su número
                Optional<Habitacion> habOpt = sistema.buscarHabitacionPorId(r.getHabitacionId());
                String numHabitacion = habOpt.map(h -> String.valueOf(h.getNumero())).orElse("N/A");

                System.out.println("---------------------------");
                System.out.println("Cliente: " + r.getNombreReservante() + " (DNI: " + r.getDocumento() + ")");
                System.out.println("Habitación: " + numHabitacion);
                System.out.println("Desde: " + r.getDesde());
                System.out.println("Hasta: " + r.getHasta());
            });
        }
    }

    // ================= MÉTODOS AUXILIARES =================

    // Método genérico para leer texto
    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    // Método genérico para leer números enteros con validación
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Ingrese un número válido.");
            }
        }
    }

    // Método genérico para leer double
    private double leerDouble() {
        while (true) {
            try {
                System.out.print("Precio por noche: $");
                String texto = sc.nextLine().trim().replace(",", "."); // permite usar coma o punto
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                System.out.println("❌ Valor inválido. Ingrese un número válido (por ejemplo: 2500 o 2500.50).");
            }
        }
    }

    public boolean habitacionDisponibleEnSistema(Habitacion h, LocalDate desde, LocalDate hasta) {
        // comprobar reservas del sistema que pertenezcan a la habitacion
        for (Reserva r : sistema.getReservas()) {
            if (r.getHabitacionId().equals(h.getId()) && !r.isCancelada() && r.seSolapa(desde, hasta)) {
                return false;
            }
        }
        return true;
    }
}
