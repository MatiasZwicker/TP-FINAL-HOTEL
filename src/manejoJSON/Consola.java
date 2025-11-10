package manejoJSON;

import Clases.*;
import Controladores.Sistema;
import Enums.Rol;
import org.json.JSONException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

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
        System.out.println("=== SISTEMA DE HOTEL (Consola) ===");

        boolean salir = false;
        // Bucle principal del menú
        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Opción: "); // leo la opción del usuario
            switch (opcion) {
                case 1 -> agregarCliente();      // agrega un nuevo cliente
                case 2 -> agregarHabitacion();   // agrega una habitación
                case 3 -> agregarReserva();      // crea una reserva
                case 4 -> mostrarTodo();         // muestra todos los datos
                case 0 -> {                      // guarda y termina el programa
                    sistema.guardarSistema();
                    System.out.println("👋 Fin del programa.");
                    salir = true;
                }
                default -> System.out.println("⚠️ Opción inválida.");
            }
        }
    }

    // Muestra el menú principal
    private void mostrarMenu() {
        System.out.println("\n1) Agregar Cliente");
        System.out.println("2) Agregar Habitación");
        System.out.println("3) Agregar Reserva");
        System.out.println("4) Mostrar Todo");
        System.out.println("0) Salir y Guardar");
    }

    // ================= CLIENTES =================

    // Método que crea y agrega un nuevo cliente
    private void agregarCliente() {
        System.out.println("\n--- Nuevo Cliente ---");

        // Pido todos los datos básicos
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        int dni = leerEntero("DNI: ");
        String email = leerTexto("Email: ");
        String telefono = leerTexto("Teléfono: ");
        String nacionalidad = leerTexto("Nacionalidad: ");

        // Creo el objeto Cliente con los datos ingresados
        // El último "false" indica que no es empleado
        Cliente nuevoCliente = new Cliente(telefono, dni, email, apellido, nombre, nacionalidad, false);

        // Lo agrego al sistema
        sistema.agregarCliente(nuevoCliente);
        System.out.println("✅ Cliente agregado correctamente.");
    }

    // ================= HABITACIONES =================

    // Método que agrega una nueva habitación
    private void agregarHabitacion() {
        System.out.println("\n--- Nueva Habitación ---");

        // Pido los datos básicos de la habitación
        int numero = leerEntero("Número de Habitación: ");
        String tipo = leerTexto("Tipo (SIMPLE/DOBLE/SUITE): ");

        // Creo y agrego la habitación
        Habitacion nuevaHabitacion = new Habitacion(numero, tipo);
        sistema.agregarHabitacion(nuevaHabitacion);
        System.out.println("✅ Habitación agregada correctamente.");
    }

    // ================= RESERVAS =================

    private void agregarReserva() {
        System.out.println("\n--- Nueva Reserva ---");

        // Antes de crear una reserva, verifico que haya al menos un cliente y una habitación
        if (sistema.getClientes().isEmpty() || sistema.getHabitaciones().isEmpty()) {
            System.out.println("⚠️ Necesitás al menos un cliente y una habitación para crear una reserva.");
            return;
        }

        // Listo todos los clientes existentes
        System.out.println("Clientes disponibles:");
        sistema.getClientes().forEach(c ->
                System.out.println("- DNI: " + c.getDni() + " | " + c.getNombreCompleto())
        );

        // Pido el DNI del cliente que va a hacer la reserva
        int dni = leerEntero("Ingrese el DNI del cliente: ");

        // Busco el cliente con ese DNI usando Optional (para evitar null)
        //Optional es una clase generica de Java que sirve para evitar el null
        Optional<Cliente> clienteOpt = sistema.buscarClientePorDni(dni);
        if (clienteOpt.isEmpty()) {
            System.out.println("❌ No se encontró un cliente con ese DNI.");
            return;
        }
        Cliente cliente = clienteOpt.get();

        // Listo todas las habitaciones para que el usuario elija
        System.out.println("Habitaciones disponibles:");
        sistema.getHabitaciones().forEach(h ->
                System.out.println("Tipo de habitacion " + h.getTipo() + " | Numero " + h.getNumero())
        );

        // Pido el número de habitación que se desea reservar
        int numHabitacion = leerEntero("Ingrese el numero de habitacion: ");

        // Busco la habitación seleccionada
        Optional<Habitacion> habitacionOpt = sistema.buscarHabitacionPorNumero(numHabitacion);
        if (habitacionOpt.isEmpty()) {
            System.out.println("❌ No se encontró una habitación con ese número.");
            return;
        }
        Habitacion habitacion = habitacionOpt.get();

        // --- Manejo de fechas ---
        LocalDate desde = null;
        LocalDate hasta = null;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // formato de fecha

        // Leo la fecha de inicio con validación
        while (true) {
            try {
                String texto = leerTexto("Fecha inicio (YYYY-MM-DD): ");
                desde = LocalDate.parse(texto, formato);
                break; // si es válida, salgo del bucle
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato inválido. Ingrese la fecha con formato YYYY-MM-DD (por ejemplo, 2025-11-10).");
            }
        }

        // Leo la fecha de fin con validación
        while (true) {
            try {
                String texto = leerTexto("Fecha fin (YYYY-MM-DD): ");
                hasta = LocalDate.parse(texto, formato);

                // Validación: la fecha de fin no puede ser anterior a la de inicio
                if (hasta.isBefore(desde)) {
                    System.out.println("❌ La fecha de fin no puede ser anterior a la de inicio.");
                } else {
                    break;
                }
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato inválido. Ingrese la fecha con formato YYYY-MM-DD (por ejemplo, 2025-11-15).");
            }
        }

        // Creo la nueva reserva y la agrego al sistema
        Reserva nuevaReserva = new Reserva(
                cliente.getNombreCompleto(),
                String.valueOf(cliente.getDni()),
                desde,
                hasta,
                habitacion.getId()
        );

        sistema.agregarReserva(nuevaReserva);
        System.out.println("✅ Reserva creada correctamente.");
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
}
