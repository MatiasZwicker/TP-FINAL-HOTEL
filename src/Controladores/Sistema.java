package Controladores;

import Clases.*;
import manejoJSON.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Sistema {

    private List<Cliente> clientes;
    private List<Empleado> empleados;
    private List<Usuario> usuarios;
    private List<Habitacion> habitaciones;
    private List<Reserva> reservas;
    private List<ServicioExtra> servicios;
    private List<Factura> facturas;

    private static final String ARCHIVO_DATOS = "hotel.json";

    public Sistema() {
        this.clientes = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.habitaciones = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.servicios = new ArrayList<>();
        this.facturas = new ArrayList<>();
    }

    public void cargarSistema() throws JSONException {
        JSONObject data = JSONUtiles.leerObjeto(ARCHIVO_DATOS);

        if (data.has("clientes")) {
            JSONArray clientesJson = data.getJSONArray("clientes");
            for (int i = 0; i < clientesJson.length(); i++) {
                this.clientes.add(ManejoJSONCliente.fromJSON(clientesJson.getJSONObject(i)));
            }
        }
        if (data.has("empleados")) {
            JSONArray empleadosJson = data.getJSONArray("empleados");
            for (int i = 0; i < empleadosJson.length(); i++) {
                this.empleados.add(ManejoJSONEmpleado.fromJSON(empleadosJson.getJSONObject(i)));
            }
        }
        if (data.has("usuarios")) {
            JSONArray usuariosJson = data.getJSONArray("usuarios");
            for (int i = 0; i < usuariosJson.length(); i++) {
                this.usuarios.add(ManejoJSONUsuario.fromJSON(usuariosJson.getJSONObject(i)));
            }
        }
        if (data.has("habitaciones")) {
            JSONArray habitacionesJson = data.getJSONArray("habitaciones");
            for (int i = 0; i < habitacionesJson.length(); i++) {
                this.habitaciones.add(ManejoJSONHabitacion.fromJSON(habitacionesJson.getJSONObject(i)));
            }
        }
        if (data.has("reservas")) {
            JSONArray reservasJson = data.getJSONArray("reservas");
            for (int i = 0; i < reservasJson.length(); i++) {
                this.reservas.add(ManejoJSONReserva.fromJSON(reservasJson.getJSONObject(i)));
            }
        }
        if (data.has("servicios")) {
            JSONArray serviciosJson = data.getJSONArray("servicios");
            for (int i = 0; i < serviciosJson.length(); i++) {
                this.servicios.add(ManejoJSONServicioExtra.fromJSON(serviciosJson.getJSONObject(i)));
            }
        }
        if (data.has("facturas")) {
            JSONArray facturasJson = data.getJSONArray("facturas");
            for (int i = 0; i < facturasJson.length(); i++) {
                this.facturas.add(ManejoJSONFactura.fromJSON(facturasJson.getJSONObject(i)));
            }
        }

        if (data.has("habitaciones")) {
            JSONArray habitacionesJson = data.getJSONArray("habitaciones");
            for (int i = 0; i < habitacionesJson.length(); i++) {
                JSONObject habJson = habitacionesJson.getJSONObject(i);
                UUID habitacionId = UUID.fromString(habJson.getString("id"));
                Optional<Habitacion> habitacionOpt = buscarHabitacionPorId(habitacionId);

                if (habitacionOpt.isPresent() && habJson.has("reservasIds")) {
                    Habitacion habitacion = habitacionOpt.get();
                    JSONArray reservasIds = habJson.getJSONArray("reservasIds");
                    for (int j = 0; j < reservasIds.length(); j++) {
                        UUID reservaId = UUID.fromString(reservasIds.getString(j));
                        Optional<Reserva> reservaOpt = buscarReservaPorId(reservaId);
                        reservaOpt.ifPresent(habitacion::agregarReserva);
                    }
                }
            }
        }
        // aca, en el futuro, iría la vinculación de Estadia con sus servicios, pagos y factura.

        System.out.println("✅ Sistema cargado correctamente desde " + ARCHIVO_DATOS);
    }

    public void guardarSistema() throws JSONException {
        JSONObject data = new JSONObject();

        JSONArray clientesJson = new JSONArray();
        for (Cliente cliente : this.clientes) {
            clientesJson.put(ManejoJSONCliente.toJSON(cliente));
        }
        data.put("clientes", clientesJson);

        JSONArray empleadosJson = new JSONArray();
        for (Empleado empleado : this.empleados) {
            empleadosJson.put(ManejoJSONEmpleado.toJSON(empleado));
        }
        data.put("empleados", empleadosJson);

        JSONArray usuariosJson = new JSONArray();
        for (Usuario usuario : this.usuarios) {
            usuariosJson.put(ManejoJSONUsuario.toJSON(usuario));
        }
        data.put("usuarios", usuariosJson);

        JSONArray habitacionesJson = new JSONArray();
        for (Habitacion habitacion : this.habitaciones) {
            habitacionesJson.put(ManejoJSONHabitacion.toJSON(habitacion));
        }
        data.put("habitaciones", habitacionesJson);

        JSONArray reservasJson = new JSONArray();
        for (Reserva reserva : this.reservas) {
            reservasJson.put(ManejoJSONReserva.toJSON(reserva));
        }
        data.put("reservas", reservasJson);

        //convertir lista de servicios a JsonArray
        JSONArray serviciosJson = new JSONArray();
        for (ServicioExtra servicioExtra : this.servicios) {
            serviciosJson.put(ManejoJSONServicioExtra.toJSON(servicioExtra));
        }
        data.put("servicios", serviciosJson);

        //factura
        JSONArray facturasJson = new JSONArray();
        for (Factura factura : this.facturas) {
            facturasJson.put(ManejoJSONFactura.toJSON(factura));
        }
        data.put("facturas", facturasJson);

        JSONUtiles.grabarObjeto(data, ARCHIVO_DATOS);
        System.out.println("💾 Sistema guardado correctamente en " + ARCHIVO_DATOS);
    }
    public void agregarEmpleado(Empleado empleado) {
        if (empleado == null) return;

        // Evitamos duplicados por DNI
        boolean existe = empleados.stream()
                .anyMatch(e -> e.getDni() == empleado.getDni());

        if (existe) {
            System.out.println("⚠️ Ya existe un empleado con el mismo DNI.");
        } else {
            empleados.add(empleado);
        }
    }
    public void agregarCliente(Cliente cliente) {
        this.clientes.add(cliente);
    }

    public void agregarHabitacion(Habitacion habitacion) {
        this.habitaciones.add(habitacion);
    }

    public void agregarReserva(Reserva reserva) {
        this.reservas.add(reserva);
        buscarHabitacionPorId(reserva.getHabitacionId()).ifPresent(h -> h.agregarReserva(reserva));
    }

    public List<Cliente> getClientes() {
        return this.clientes;
    }

    public List<Habitacion> getHabitaciones() {
        return this.habitaciones;
    }

    public List<Reserva> getReservas() {
        return this.reservas;
    }

    public Optional<Habitacion> buscarHabitacionPorId(UUID id) {
        return this.habitaciones.stream().filter(h -> h.getId().equals(id)).findFirst();
    }

    public Optional<Reserva> buscarReservaPorId(UUID id) {
        return this.reservas.stream().filter(r -> r.getId().equals(id)).findFirst();
    }
    /// busca por dni a ver si esta registrado
    public JSONObject buscarPorDNI(int dni, String archivoHotel) {
        try (FileReader reader = new FileReader(archivoHotel)) {
            JSONObject hotel = new JSONObject(new JSONTokener(reader));

            // Buscar en empleados
            if (hotel.has("empleados")) {
                JSONArray empleados = hotel.getJSONArray("empleados");
                for (int i = 0; i < empleados.length(); i++) {
                    JSONObject empleado = empleados.getJSONObject(i);
                    if (empleado.getInt("dni") == dni) {
                        empleado.put("tipo", "empleado");
                        return empleado;
                    }
                }
            }

            // Buscar en clientes
            if (hotel.has("clientes")) {
                JSONArray clientes = hotel.getJSONArray("clientes");
                for (int i = 0; i < clientes.length(); i++) {
                    JSONObject cliente = clientes.getJSONObject(i);
                    if (cliente.getInt("dni") == dni) {
                        cliente.put("tipo", "cliente");
                        return cliente;
                    }
                }
            }

        } catch (IOException | JSONException e) {
            System.out.println("Error al leer el archivo " + archivoHotel);
        }

        return null;
    }

    ///  busca por UUID
    public Optional<Cliente> buscarClientePorId(UUID id) {
        return this.clientes.stream().filter(c -> c.getId().equals(id)).findFirst();
    }
    ///  busca por DNI
    public Optional<Cliente> buscarClientePorDni(int dni) {
        return clientes.stream()
                .filter(c -> c.getDni() == dni)
                .findFirst();
    }
    /// busca empleado por dni
    public Optional<Empleado> buscarEmpleadoPorDni(int dni) {
        return empleados.stream().filter(e -> e.getDni() == dni).findFirst();
    }

    ///  busca habitacion por numero
    public Optional<Habitacion> buscarHabitacionPorNumero(int numero) {
        return habitaciones.stream()
                .filter(h -> h.getNumero() == numero)
                .findFirst();
    }
    // Leer archivo hotel.json (con objetos: clientes, empleados, habitaciones, etc.)
    public JSONObject leerArchivoHotel(String ruta) throws JSONException {
        try (FileReader reader = new FileReader(ruta)) {
            JSONTokener tokener = new JSONTokener(reader);
            return new JSONObject(tokener);
        } catch (IOException e) {
            System.out.println("⚠️ Error al leer el archivo " + ruta);
            return null;
        }
    }

    // Guardar archivo hotel.json actualizado
    public void guardarArchivoHotel(JSONObject data, String ruta) throws JSONException {
        try (FileWriter writer = new FileWriter(ruta, false)) { // 👈 false = sobrescribe
            writer.write(data.toString(4)); // 4 = indentación para legibilidad
            writer.flush();
        } catch (IOException e) {
            System.out.println("⚠️ Error al guardar el archivo " + ruta);
            e.printStackTrace();
        }
    }



}
