package Model;

public class EstadoPedido {
    private int idEstado;
    private String tipo;

    public EstadoPedido() {}

    public EstadoPedido(int idEstado, String tipo) {
        this.idEstado = idEstado;
        this.tipo = tipo;
    }

    // Getters y Setters
    public int getIdEstado() { return idEstado; }
    public void setIdEstado(int idEstado) { this.idEstado = idEstado; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return tipo;
    }
}
