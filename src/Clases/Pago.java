package Clases;

import Enums.MetodoPago;

import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.UUID;

public class Pago {
    private UUID id;
    private LocalDateTime fecha;
    private Money monto;
    private MetodoPago metodo;
    private String referenciaExterna;

    public Pago() { this.id = UUID.randomUUID(); this.fecha = LocalDateTime.now(); }

    public Pago(Money monto, MetodoPago metodo, String referenciaExterna) {
        this.id = UUID.randomUUID();
        this.fecha = LocalDateTime.now();
        this.monto = monto;
        this.metodo = metodo;
        this.referenciaExterna = referenciaExterna;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public Money getMonto() {
        return monto;
    }

    public void setMonto(Money monto) {
        this.monto = monto;
    }

    public MetodoPago getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPago metodo) {
        this.metodo = metodo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
