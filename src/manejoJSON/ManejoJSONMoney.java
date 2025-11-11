package manejoJSON;


import Clases.Money;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class ManejoJSONMoney {

    // Convierte un objeto Money a JSON
    public static JSONObject toJSON(Money money) throws JSONException {
        JSONObject moneyJson = new JSONObject();
        moneyJson.put("monto", money.getMonto().toString());
        moneyJson.put("moneda", money.getMoneda());
        return moneyJson;
    }

    // Convierte un JSON a objeto Money
    public static Money fromJSON(JSONObject moneyJson) throws JSONException {
        BigDecimal monto = new BigDecimal(moneyJson.getString("monto"));
        String moneda = moneyJson.getString("moneda");
        return new Money(monto, moneda);
    }
}
