package app.model;

import jdk.jfr.Timestamp;

public class Producto {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private double precio; 
    private double costo;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    // Constructor vacío
    public Producto() {
        this.nombre = "";
        this.descripcion = "";
        this.precio = 0.0;
        this.costo = 0.0;
    }

    public Producto(int idProducto, String nombre, double precio, double costo) {
        this.idProducto = idProducto;
        this.nombre = nombre;
    }
    
    public Producto(String nombre, String descripcion, double precio, double costo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.costo = costo;
    }
    
    // Constructor de Base de Datos
    public Producto(int idProducto, String nombre, String descripcion, double precio, double costo) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.costo = costo;
    }
    
    public Producto(int id){
        this.idProducto = id;
    }

    // Getters y setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Timestamp getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Timestamp deletedAt) { this.deletedAt = deletedAt; }
}


