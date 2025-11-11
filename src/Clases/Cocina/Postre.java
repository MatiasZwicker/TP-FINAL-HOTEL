package Clases.Cocina;

import Interfaz.ItemCocina;

public class Postre implements ItemCocina {
    private String nombre;
    private double precio;

    public Postre(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    @Override
    public String getNombre() { return nombre; }

    @Override
    public double getPrecio() { return precio; }

}
