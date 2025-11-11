package manejoJSON;

import Clases.Cocina.*;
import Interfaz.ItemCocina;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManejoJSONPlatos {

    private static final String RUTA_PLATOS = "platos.json";

    // Guardar un nuevo plato en el archivo JSON
    public static void guardar(ItemCocina item) throws JSONException {
        JSONArray array = leerJSON(); // leer los platos existentes

        // Crear el JSONObject del plato
        JSONObject obj = new JSONObject();
        obj.put("nombre", item.getNombre());
        obj.put("precio", item.getPrecio());
        obj.put("tipo", item.getClass().getSimpleName()); // Entrada, PlatoPrincipal, Postre, BebidaArtesanal, etc.

        array.put(obj); // agregar al array

        escribirJSON(array); // guardar en archivo
    }

    // Leer todos los platos desde JSON
    public static List<ItemCocina> leerTodos() throws JSONException {
        JSONArray array = leerJSON();
        List<ItemCocina> lista = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String nombre = obj.getString("nombre");
            double precio = obj.getDouble("precio");
            String tipo = obj.getString("tipo");

            // Crear el objeto según el tipo
            ItemCocina item;
            switch (tipo) {
                case "Entrada" -> item = new Entrada(nombre, precio);
                case "PlatoPrincipal" -> item = new PlatoPrincipal(nombre, precio);
                case "Postre" -> item = new Postre(nombre, precio);
                case "BebidaArtesanal" -> item = new BebidaArtesanal(nombre, precio);
                case "Desayuno" -> item = new Desayuno(nombre, precio);
                case "Merienda" -> item = new Merienda(nombre, precio);
                default -> item = new PlatoPrincipal(nombre, precio); // por defecto
            }
            lista.add(item);
        }

        return lista;
    }

    // ================== MÉTODOS AUXILIARES ==================

    private static JSONArray leerJSON() throws JSONException {
        try (FileReader reader = new FileReader(RUTA_PLATOS)) {
            char[] buffer = new char[1024];
            int read = reader.read(buffer);
            String contenido = (read > 0) ? new String(buffer, 0, read) : "";
            if (contenido.isEmpty()) return new JSONArray();
            return new JSONArray(contenido);
        } catch (IOException e) {
            return new JSONArray(); // si no existe, devuelve vacío
        }
    }

    private static void escribirJSON(JSONArray array) throws JSONException {
        try (FileWriter file = new FileWriter(RUTA_PLATOS)) {
            file.write(array.toString(4)); // identado 4 espacios
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}