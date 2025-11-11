package manejoJSON;

import Clases.Reserva;
import Clases.Habitacion;
import org.json.*;

import java.util.UUID;

public class ManejoJSONHabitacion {

    public static JSONObject toJSON(Habitacion h) throws JSONException {
        JSONObject habitacionJson = new JSONObject();
        habitacionJson.put("id", h.getId().toString());
        habitacionJson.put("numero", h.getNumero());
        habitacionJson.put("tipo", h.getTipo());
        habitacionJson.put("precio por noche", h.getPrecioxNoche());
        habitacionJson.put("disponible", h.isDisponible());

        // se guarda solo los IDs de las reservas
        JSONArray reservasID = new JSONArray();
        for (Reserva r : h.getReservas()) {
            reservasID.put(r.getId().toString());
        }
        habitacionJson.put("reservasIds", reservasID);
        return habitacionJson;

    }
    public static void guardar(Habitacion h) {
        try {
            JSONObject hotel = JSONUtiles.leerObjeto("hotel.json");
            JSONArray habitaciones = hotel.optJSONArray("habitaciones");
            if (habitaciones == null) habitaciones = new JSONArray();

            habitaciones.put(toJSON(h));
            hotel.put("habitaciones", habitaciones);

            JSONUtiles.grabarObjeto(hotel, "hotel.json");
            System.out.println("✅ Habitación guardada correctamente.");
        } catch (Exception e) {
            System.out.println("❌ Error al guardar habitación: " + e.getMessage());
        }
    }

    public static Habitacion fromJSON(JSONObject habitacionJson) throws JSONException {
        String idString = habitacionJson.getString("id");
        int numero  = habitacionJson.getInt("numero");
        String tipo = habitacionJson.getString("tipo");
        double precio = habitacionJson.getDouble("precio por noche");
        boolean disponible = habitacionJson.getBoolean("disponible");

        // creamos el Objeto habitacion usando el constructor
        Habitacion h = new Habitacion(numero, tipo, precio);

        // asigamos los valores restantes usando los setters
        h.setId(UUID.fromString(idString));
        h.setDisponible(disponible);

        return h;
    }

    public static void agregarReservaIdEnJSON(Habitacion h, UUID reservaId) {
        String ARCHIVO = "hotel.json";

        try {
            // 1) Leer el JSON completo del hotel
            JSONObject hotel = JSONUtiles.leerObjeto(ARCHIVO);
            JSONArray habitaciones = hotel.optJSONArray("habitaciones");
            if (habitaciones == null) habitaciones = new JSONArray();

            // 2) Buscar la habitación por ID y actualizar su array reservasIds
            for (int i = 0; i < habitaciones.length(); i++) {
                JSONObject habJson = habitaciones.getJSONObject(i);
                if (habJson.has("id") && habJson.getString("id").equals(h.getId().toString())) {

                    // Obtener o crear el array de reservasIds
                    JSONArray reservasIds = habJson.optJSONArray("reservasIds");
                    if (reservasIds == null) reservasIds = new JSONArray();

                    // Agregar el nuevo ID
                    reservasIds.put(reservaId.toString());
                    habJson.put("reservasIds", reservasIds);

                    // Reemplazar el objeto en el array (opcional si ya modificaste el objeto in situ)
                    habitaciones.put(i, habJson);
                    break;
                }
            }

            // 3) Guardar el JSON actualizado en el archivo
            JSONUtiles.grabarObjeto(hotel, ARCHIVO);

        } catch (Exception e) {
            System.err.println("Error agregando reservaId en hotel.json: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
