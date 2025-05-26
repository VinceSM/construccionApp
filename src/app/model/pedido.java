package app.model;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class Pedido {
    private int idPedido;
    private Cliente cliente;
    private EstadoPedido estado;
    private Timestamp fechaPedido;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
    private List<DetallePedido> detalles;

    public Pedido() {
        this.detalles = new ArrayList<>();
    }

    public Pedido(int idPedido, Cliente cliente, EstadoPedido estado, Timestamp fechaPedido) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.estado = estado;
        this.fechaPedido = fechaPedido;
        this.detalles = new ArrayList<>();
    }

    // Getters y setters
    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public Timestamp getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(Timestamp fechaPedido) { this.fechaPedido = fechaPedido; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Timestamp getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Timestamp deletedAt) { this.deletedAt = deletedAt; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }

    public void agregarDetalle(DetallePedido detalle) {
        this.detalles.add(detalle);
    }
}