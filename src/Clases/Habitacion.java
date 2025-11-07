package Clases;



import Interfaz.Reservable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Habitacion implements Reservable {
    private UUID id;
    private String numero;
    private String tipo; // sencillo, doble, suite, etc.
    private boolean disponible; // estado general (ej limpieza/reparacion)
    private List<Reserva> reservas = new ArrayList<>();

    public Habitacion() { this.id = UUID.randomUUID(); this.disponible = true; }

    public Habitacion(String numero, String tipo) {
        this();
        this.numero = numero;
        this.tipo = tipo;
    }

    @Override
    public boolean isDisponible(LocalDate desde, LocalDate hasta) {
        if (!disponible) return false;
        for (Reserva r : reservas) {
            if (r.seSolapa(desde, hasta)) return false;
        }
        return true;
    }

    public void agregarReserva(Reserva r) { reservas.add(r); }
    public List<Reserva> getReservas() { return reservas; }

    // getters/setters...
    public String getNumero() { return numero; }
    public String getTipo() { return tipo; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}