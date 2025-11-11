package manejoJSON;

import Clases.Money;
import Clases.ServicioExtra;
import Enums.TipoServicio;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class ManejoJSONServicioExtra {

    public static JSONObject toJSON(ServicioExtra servicio) throws JSONException {
        JSONObject servicioJson = new JSONObject();
        servicioJson.put("id", servicio.getId().toString());
        servicioJson.put("descripcion", servicio.getDescripcion());
        servicioJson.put("cantidad", servicio.getCantidad());
        servicioJson.put("tipo", servicio.getTipo().name()); // Guardamos el Enum como String

        // Usamos el adaptador de Money para el precio
        servicioJson.put("precioUnitario", ManejoJSONMoney.toJSON(servicio.getPrecioUnitario()));

        return servicioJson;
    }

    public static ServicioExtra fromJSON(JSONObject serExtrajson) throws JSONException {
        UUID id = UUID.fromString(serExtrajson.getString("id"));
        String descripcion = serExtrajson.getString("descripcion");
        int cantidad = serExtrajson.getInt("cantidad");
        TipoServicio tipo = TipoServicio.valueOf(serExtrajson.getString("tipo"));

        // Usamos el adaptador de Money para reconstruir el precio
        Money precioUnitario = ManejoJSONMoney.fromJSON(serExtrajson.getJSONObject("precioUnitario"));

        ServicioExtra servicio = new ServicioExtra(descripcion, precioUnitario, cantidad, tipo);
        servicio.setId(id);

        return servicio;
    }
}
