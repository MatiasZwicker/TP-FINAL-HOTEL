package manejoJSON;

import Clases.Money;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class ManejoJSONMoney {
    public static JSONObject toJSON(Money money) throws JSONException {
        JSONObject moneyJson = new JSONObject();
        moneyJson.put("monto", money.getMonto().toString());
        moneyJson.put("moneda", money.getMoneda());
        return moneyJson;
    }
    public static Money fromJSON(JSONObject montoJson) throws JSONException {
        BigDecimal monto = new BigDecimal(montoJson.getString("monto"));
        String moneda = montoJson.getString("moneda");
        return new Money(monto, moneda);
    }
}
