package app.model;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class PrecioProducto {
    private int idPrecio;
    private Producto producto;
    private String moneda;
    private BigDecimal monto; // Cambiado a BigDecimal
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public PrecioProducto() {}

    public PrecioProducto(int idPrecio, Producto producto, String moneda, BigDecimal monto) {
        this.idPrecio = idPrecio;
        this.producto = producto;
        this.moneda = moneda;
        this.monto = monto;
    }

    // Getters y setters
    public int getIdPrecio() { return idPrecio; }
    public void setIdPrecio(int idPrecio) { this.idPrecio = idPrecio; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Timestamp getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Timestamp deletedAt) { this.deletedAt = deletedAt; }
}