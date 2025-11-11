package manejoJSON;

import Clases.Factura;
import Enums.EstadoFactura;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.UUID;

public class ManejoJSONFactura {

    public static JSONObject toJSON(Factura factura) throws JSONException {
        JSONObject facturaJson = new JSONObject();
        facturaJson.put("id", factura.getId().toString());
        facturaJson.put("numero", factura.getNumero());
        facturaJson.put("estado", factura.getEstado().name());

        // Guardamos fechas y objetos complejos
        if (factura.getFechaEmision() != null) {
            facturaJson.put("fechaEmision", factura.getFechaEmision().toString());
        }
        if (factura.getTotal() != null) {
            facturaJson.put("total", ManejoJSONMoney.toJSON(factura.getTotal()));
        }

        return facturaJson;
    }

    public static Factura fromJSON(JSONObject facJson) throws JSONException {
        Factura factura = new Factura();
        factura.setId(UUID.fromString(facJson.getString("id")));
        factura.setNumero(facJson.getString("numero"));
        factura.setEstado(EstadoFactura.valueOf(facJson.getString("estado")));

        // Leemos fechas y objetos complejos, comprobando si existen
        if (facJson.has("fechaEmision")) {
            factura.setFechaEmision(LocalDateTime.parse(facJson.getString("fechaEmision")));
        }
        if (facJson.has("total")) {
            factura.setTotal(ManejoJSONMoney.fromJSON(facJson.getJSONObject("total")));
        }

        return factura;
    }
}
