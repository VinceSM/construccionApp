package Model;

import java.util.Date;
import java.util.List;

public class Pedido {
    private int idPedido;
    private Date fechaPedido;
    private Cliente cliente;
    private EstadoPedido estado;
    private List<Item> items; // Lista de productos asociados al pedido

    public Pedido() {}

    public Pedido(int idPedido, Date fechaPedido, Cliente cliente, EstadoPedido estado, List<Item> items) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.cliente = cliente;
        this.estado = estado;
        this.items = items;
    }

    // Getters y Setters
    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public Date getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(Date fechaPedido) { this.fechaPedido = fechaPedido; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}
