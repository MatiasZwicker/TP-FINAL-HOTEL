package Clases.Cocina;

import Interfaz.ItemCocina;

public class Merienda implements ItemCocina {
    private String nombre;
    private double precio;

    public Merienda(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    @Override
    public String getNombre() { return nombre; }

    @Override
    public double getPrecio() { return precio; }
}

