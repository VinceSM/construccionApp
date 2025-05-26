package app.model;

import java.sql.Timestamp;

public class Cliente {
    private int idCliente;
    private String nombreCompleto;
    private String dni; // Cambiado a String para coincidir con la BD
    private String telefono; // Cambiado a String para coincidir con la BD
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public Cliente() {}

    public Cliente(int idCliente, String nombreCompleto, String dni, String telefono) {
        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.telefono = telefono;
    }

    // Getters y setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Timestamp getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Timestamp deletedAt) { this.deletedAt = deletedAt; }
}