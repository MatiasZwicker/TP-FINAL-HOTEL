package Clases.Cocina;

import Interfaz.ItemCocina;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestorCocina<T extends ItemCocina> {
    private List<T> items;

    public GestorCocina() {
        items = new ArrayList<>();
    }

    public void agregar(T item) {
        items.add(item);
    }

    public boolean eliminar(T item) {
        return items.remove(item);
    }

    public List<T> listar() {
        return items;
    }
    public void mostrarMenu() {
        if (items.isEmpty()) {
            System.out.println("No hay ítems cargados.");
            return;
        }
        items.forEach(i -> System.out.println(i.getNombre() + " - $" + i.getPrecio()));
    }
    public Optional<T> buscarPorNombre(String nombre) {
        return items.stream().filter(i -> i.getNombre().equalsIgnoreCase(nombre)).findFirst();
    }

}
