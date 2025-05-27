package app.model;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class Stock {
    private int idStock;
    private Producto producto;
    private BigDecimal unidad; // Cambiado a BigDecimal para coincidir con DECIMAL(10,2)
    private String medida;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public Stock() {}

    // Constructor de Stock
    public Stock(BigDecimal unidad, Producto producto, String medida) {
        this.unidad = unidad;
        this.producto = producto != null ? producto : new Producto();
        this.medida = medida;
    }

    // Constructor de Base de Datos
    public Stock(int idStock, Producto producto, BigDecimal unidad, String medida) {
        this.idStock = idStock;
        this.producto = producto;
        this.unidad = unidad;
        this.medida = medida;
    }

    // Getters y setters
    public int getIdStock() { return idStock; }
    public void setIdStock(int idStock) { this.idStock = idStock; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public BigDecimal getUnidad() { return unidad; }
    public void setUnidad(BigDecimal unidad) { this.unidad = unidad; }

    public String getMedida() { return medida; }
    public void setMedida(String medida) { this.medida = medida; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Timestamp getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Timestamp deletedAt) { this.deletedAt = deletedAt; }
}