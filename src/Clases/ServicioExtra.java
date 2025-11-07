package Clases;

import Enums.TipoServicio;

import java.util.UUID;


    public class ServicioExtra {
        private UUID id;
        private String descripcion;
        private Money precioUnitario;
        private int cantidad;
        private TipoServicio tipo;

        public ServicioExtra() { this.id = UUID.randomUUID(); }

        public ServicioExtra(String descripcion, Money precioUnitario, int cantidad, TipoServicio tipo) {
            this.id = UUID.randomUUID();
            this.descripcion = descripcion;
            this.precioUnitario = precioUnitario;
            this.cantidad = cantidad;
            this.tipo = tipo;
        }

        public Money subtotal() {
            return precioUnitario.multiply(cantidad);
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public TipoServicio getTipo() {
            return tipo;
        }

        public void setTipo(TipoServicio tipo) {
            this.tipo = tipo;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public Money getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(Money precioUnitario) {
            this.precioUnitario = precioUnitario;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }
