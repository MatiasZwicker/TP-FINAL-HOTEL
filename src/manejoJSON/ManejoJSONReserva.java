package manejoJSON;

import Clases.Reserva;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.UUID;

public class ManejoJSONReserva {

    private static final String ARCHIVO_HOTEL = "hotel.json";
    public static JSONObject toJSON(Reserva r) throws JSONException {
        JSONObject reservaJson = new JSONObject();
        reservaJson.put("id", r.getId().toString());
        reservaJson.put("nombreReservante", r.getNombreReservante());
        reservaJson.put("documento",  r.getDocumento());
        reservaJson.put("habitacionId", r.getHabitacionId().toString());
        reservaJson.put("cancelada", r.isCancelada());

        //convertimos las fecha a String para guardarlas en el JSON
        reservaJson.put("desde", r.getDesde().toString());
        reservaJson.put("hasta", r.getHasta().toString());

        return reservaJson;
    }
    public static void guardar(Reserva r) {
        try {
            JSONObject hotel = JSONUtiles.leerObjeto("hotel.json");
            JSONArray reservas = hotel.optJSONArray("reservas");
            if (reservas == null) reservas = new JSONArray();

            reservas.put(toJSON(r));
            hotel.put("reservas", reservas);

            JSONUtiles.grabarObjeto(hotel, "hotel.json");

        } catch (Exception e) {
            System.out.println("❌ Error al guardar reserva: " + e.getMessage());
        }
    }


    public static Reserva fromJSON(JSONObject json) throws JSONException {
        String idString = json.getString("id");
        String nombreReservante = json.getString("nombreReservante");
        String documento = json.getString("documento");
        String habitacionIdString = json.getString("habitacionId");
        boolean cancelada = json.getBoolean("cancelada");

        //leemos las fechas como strings y las convertimos en Localdate
        LocalDate desde = LocalDate.parse(json.getString("desde"));
        LocalDate hasta = LocalDate.parse(json.getString("hasta"));

        //creamos el objeto Reserva usando el constructor
        Reserva r = new Reserva(
                nombreReservante,
                documento,
                desde,
                hasta,
                UUID.fromString(habitacionIdString)
        );

        // asignamos los valores restantes usando los setters
        r.setId(UUID.fromString(idString));
        r.setCancelada(cancelada);
        return r;
    }
}
