package Model;

public class Item {
    private int idItem;
    private int cantidad;
    private Producto producto;
    private Pedido pedido;

    public Item() {}

    public Item(int idItem, int cantidad, Producto producto, Pedido pedido) {
        this.idItem = idItem;
        this.cantidad = cantidad;
        this.producto = producto;
        this.pedido = pedido;
    }

    // Getters y Setters
    public int getIdItem() { return idItem; }
    public void setIdItem(int idItem) { this.idItem = idItem; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public double getSubtotal() {
        return cantidad * producto.getPrecio();
    }
}
