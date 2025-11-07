package Clases;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Estadia {
    private UUID id;
    private UUID reservaId;
    private UUID habitacionId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private List<ServicioExtra> servicios = new ArrayList<>();
    private List<Pago> pagos = new ArrayList<>();
    private Factura factura;

    public Estadia() {
        this.id = UUID.randomUUID();
    }

    public Estadia(UUID reservaId, UUID habitacionId, LocalDate checkIn, LocalDate checkOut) {
        this();
        this.reservaId = reservaId;
        this.habitacionId = habitacionId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Money calcularTotal(Money precioPorNoche) {
        long noches = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        if (noches <= 0) noches = 1;
        Money total = precioPorNoche.multiply((int) noches);
        for (ServicioExtra s : servicios) {
            total = total.add(s.subtotal());
        }
        return total;
    }

    public Factura getFactura() {
        return factura;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public List<ServicioExtra> getServicios() {
        return servicios;
    }

    public void setServicios(List<ServicioExtra> servicios) {
        this.servicios = servicios;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public UUID getHabitacionId() {
        return habitacionId;
    }

    public void setHabitacionId(UUID habitacionId) {
        this.habitacionId = habitacionId;
    }

    public UUID getReservaId() {
        return reservaId;
    }

    public void setReservaId(UUID reservaId) {
        this.reservaId = reservaId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void agregarServicio(ServicioExtra s) { servicios.add(s); }
    public void agregarPago(Pago p) { pagos.add(p); }
    public void setFactura(Factura f) { this.factura = f; }


}
