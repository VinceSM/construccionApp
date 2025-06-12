package DAO;

import Model.Cliente;
import Model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOCliente {
    private Connection conexion;

    public DAOCliente() throws SQLException {
        conexion = Conexion.Conectar();
    }

public boolean crearCliente(Cliente cliente) throws SQLException {
    String sql = "INSERT INTO cliente (nombreCompleto, dni, telefono) VALUES (?, ?, ?)";
    try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
        stmt.setString(1, cliente.getNombreCompleto());
        stmt.setString(2, cliente.getDni());
        stmt.setString(3, cliente.getTelefono());
        int filasAfectadas = stmt.executeUpdate();
        return filasAfectadas > 0; // Si insertó al menos una fila, retorna true
    }
}


    public List<Cliente> listarClientes() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente(
                    rs.getInt("idCliente"),
                    rs.getString("nombreCompleto"),
                    rs.getString("dni"),
                    rs.getString("telefono")
                );
                lista.add(c);
            }
        }
        return lista;
    }

    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE idCliente = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cliente(
                    rs.getInt("idCliente"),
                    rs.getString("nombreCompleto"),
                    rs.getString("dni"),
                    rs.getString("telefono")
                );
            }
        }
        return null;
    }

    public List<Cliente> obtenerClientes() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean eliminarCliente(int idCliente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean actualizarCliente(Cliente cliente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Cliente obtenerPorId(int idCliente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
