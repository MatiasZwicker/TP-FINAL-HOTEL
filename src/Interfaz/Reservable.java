package Interfaz;

public interface Reservable {
    boolean isDisponible(java.time.LocalDate desde, java.time.LocalDate hasta);
}