package app.model;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class Pedido {
    private int idPedido;
    private Cliente cliente;
    private List<Item> items;
    private double total;
    private EstadoPedido estado;
    private Timestamp fechaPedido;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public Pedido() {
        this.items = new ArrayList<>();
    }

    public Pedido(int idPedido, Cliente cliente, EstadoPedido estado, Timestamp fechaPedido) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.estado = estado;
        this.fechaPedido = fechaPedido;
        this.items = new ArrayList<>();
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
    
    public double getTotal() { return total; }    
    
    public List<Item> getItems() { return items; }

    public void setItems(List<Item> items) {
        this.items = items != null ? items : new ArrayList<>();
        calcularTotal();
    }

    public void addItem(Item item) {
        items.add(item);
        calcularTotal();
    }
    
    public double calcularTotal() {
        this.total = 0;
        for (Item item : items) {
            this.total += item.getSubtotal();
        }
        
        return total;
    }
}
