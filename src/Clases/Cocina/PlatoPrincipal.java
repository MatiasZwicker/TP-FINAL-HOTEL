package Clases.Cocina;

import Interfaz.ItemCocina;

public class PlatoPrincipal implements ItemCocina {
    private String nombre;
    private double precio;

    public PlatoPrincipal(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    @Override
    public String getNombre() { return nombre; }

    @Override
    public double getPrecio() { return precio; }
}

