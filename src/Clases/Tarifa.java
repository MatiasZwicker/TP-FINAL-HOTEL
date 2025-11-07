package Clases;


import java.time.LocalDate;
import java.util.UUID;

public class Tarifa {
    private UUID id;
    private String tipoHabitacion;
    private LocalDate vigenciaDesde;
    private LocalDate vigenciaHasta;
    private Money precioPorNoche;
    private String politicaCancelacion;

    public Tarifa() {
        this.id = UUID.randomUUID();

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public LocalDate getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(LocalDate vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }

    public LocalDate getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(LocalDate vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public Money getPrecioPorNoche() {
        return precioPorNoche;
    }

    public void setPrecioPorNoche(Money precioPorNoche) {
        this.precioPorNoche = precioPorNoche;
    }

    public String getPoliticaCancelacion() {
        return politicaCancelacion;
    }

    public void setPoliticaCancelacion(String politicaCancelacion) {
        this.politicaCancelacion = politicaCancelacion;
    }
}