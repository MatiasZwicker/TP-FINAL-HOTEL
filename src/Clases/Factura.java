package Clases;


import Enums.EstadoFactura;
import Enums.TipoFactura;

import java.time.LocalDateTime;
import java.util.UUID;

public class Factura {
    private UUID id;
    private String numero;
    private LocalDateTime fechaEmision;
    private Money total;
    private EstadoFactura estado;
    private TipoFactura tipo;

    public Factura() {
        this.id = UUID.randomUUID();
        this.estado = EstadoFactura.BORRADOR;
    }

    public Factura(Money total, TipoFactura tipo) {
        this();
        this.total = total;
        this.tipo = tipo;
        this.numero = generarNumeroFactura();
    }

    private String generarNumeroFactura() {
        int aleatorio = (int) (Math.random() * 100000);
        return "FA-" + aleatorio;
    }

    public void emitir() {
        this.estado = EstadoFactura.EMITIDA;
        this.fechaEmision = LocalDateTime.now();

    }

    public void anular() { this.estado = EstadoFactura.ANULADA; }

    // Getters y setters...


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TipoFactura getTipo() {
        return tipo;
    }

    public void setTipo(TipoFactura tipo) {
        this.tipo = tipo;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public Money getTotal() {
        return total;
    }

    public void setTotal(Money total) {
        this.total = total;
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

