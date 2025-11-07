package Exceptions;

public class ReservaNoDisponible extends RuntimeException {
    public ReservaNoDisponible(String message) {
        super(message);
    }
}
