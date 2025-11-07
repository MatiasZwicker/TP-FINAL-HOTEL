package Clases;

import java.time.LocalDate;
import java.util.UUID;

public class Reserva {
    private UUID id;
    private String nombreReservante;
    private String documento;
    private LocalDate desde;
    private LocalDate hasta;
    private UUID habitacionId;
    private boolean cancelada;

    public Reserva() { this.id = UUID.randomUUID(); }

    public Reserva(String nombreReservante, String documento, LocalDate desde, LocalDate hasta, UUID habitacionId) {
        this();
        this.nombreReservante = nombreReservante;
        this.documento = documento;
        this.desde = desde;
        this.hasta = hasta;
        this.habitacionId = habitacionId;
        this.cancelada = false;
    }

    public boolean seSolapa(LocalDate dDesde, LocalDate dHasta) {
        // solapan si no (uno termina antes que empiece el otro)
        return !(this.hasta.isBefore(dDesde) || this.desde.isAfter(dHasta));
    }

    // getters/setters...
    public UUID getHabitacionId() { return habitacionId; }
    public LocalDate getDesde() { return desde; }
    public LocalDate getHasta() { return hasta; }
    public void setCancelada(boolean cancelada) { this.cancelada = cancelada; }
}
