package Clases;

import Enums.EstadoFactura;

import java.time.LocalDateTime;
import java.util.UUID;

public class Factura {
    private UUID id;
    private String numero;
    private LocalDateTime fechaEmision;
    private Money total;
    private EstadoFactura estado;

    public Factura() { this.id = UUID.randomUUID(); this.estado = EstadoFactura.BORRADOR; }

    public void emitir() {
        this.estado = EstadoFactura.EMITIDA;
        this.fechaEmision = LocalDateTime.now();
    }

    public void anular() {
        this.estado = EstadoFactura.ANULADA;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Money getTotal() {
        return total;
    }

    public void setTotal(Money total) {
        this.total = total;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
