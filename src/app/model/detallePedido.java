package app.model;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class DetallePedido {
    private int idDetalle;
    private Pedido pedido;
    private Producto producto;
    private BigDecimal cantidad; // Cambiado a BigDecimal
    private BigDecimal precioProducto; // Cambiado a BigDecimal
    private BigDecimal subtotal; // Campo calculado
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public DetallePedido() {}

    public DetallePedido(int idDetalle, Pedido pedido, Producto producto, BigDecimal cantidad, BigDecimal precioProducto) {
        this.idDetalle = idDetalle;
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioProducto = precioProducto;
        this.subtotal = cantidad.multiply(precioProducto);
    }

    // Getters y setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioProducto() { return precioProducto; }
    public void setPrecioProducto(BigDecimal precioProducto) { this.precioProducto = precioProducto; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Timestamp getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Timestamp deletedAt) { this.deletedAt = deletedAt; }
}