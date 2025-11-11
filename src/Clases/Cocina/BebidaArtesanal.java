package Clases.Cocina;

import Interfaz.ItemCocina;

public class BebidaArtesanal implements ItemCocina {
    private String nombre;
    private double precio;

    public BebidaArtesanal(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    @Override
    public String getNombre() { return nombre; }

    @Override
    public double getPrecio() { return precio; }
}

