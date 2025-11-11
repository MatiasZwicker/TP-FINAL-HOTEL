package Clases;

import Enums.Rol;

public class Empleado extends Persona {
    private Rol cargo;

    public Empleado() {
        super();
    }

    public Empleado(Rol cargo) {
        super();
        this.cargo = cargo;
    }

    public Empleado(String telefono, int dni, String email, String apellido, String nombre, Rol cargo) {
        super(telefono, dni, email, apellido, nombre);
        this.cargo = cargo;
    }

    public Rol getCargo() {
        return cargo;
    }

    public void setCargo(Rol cargo) {
        this.cargo = cargo;
    }
    @Override
    public String toString() {
        return "Empleado" + getNombreCompleto() +  " | Cargo: " + (cargo != null ? cargo : "N/A");
    }
}
