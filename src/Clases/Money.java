package Clases;
import java.math.BigDecimal;

public class Money {
private BigDecimal monto;
    private String moneda;

    public Money() { this.monto = BigDecimal.ZERO; this.moneda = "ARS"; }
    public Money(BigDecimal monto, String moneda) { this.monto = monto; this.moneda = moneda; }

    public BigDecimal getMonto() { return monto; }
    public String getMoneda() { return moneda; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public Money add(Money other) {
        if (!this.moneda.equals(other.moneda)) throw new IllegalArgumentException("Monedas diferentes");
        return new Money(this.monto.add(other.monto), moneda);
    }

    public Money multiply(int qty) {
        return new Money(this.monto.multiply(java.math.BigDecimal.valueOf(qty)), moneda);
    }
}
