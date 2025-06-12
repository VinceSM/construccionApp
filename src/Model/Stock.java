package Model;

public class Stock {
    private int idStock;
    private double unidad;
    private String medida;
    private Producto producto;

    public Stock() {}

    public Stock(int idStock, double unidad, String medida, Producto producto) {
        this.idStock = idStock;
        this.unidad = unidad;
        this.medida = medida;
        this.producto = producto;
    }

    // Getters y Setters
    public int getIdStock() { return idStock; }
    public void setIdStock(int idStock) { this.idStock = idStock; }

    public double getUnidad() { return unidad; }
    public void setUnidad(double unidad) { this.unidad = unidad; }

    public String getMedida() { return medida; }
    public void setMedida(String medida) { this.medida = medida; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
