package app.dao;

import app.model.Cliente;
import app.model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    
    public boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nombreCompleto, dni, telefono) VALUES (?, ?, ?)";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cliente.getNombreCompleto());
            stmt.setString(2, cliente.getDni());
            stmt.setString(3, cliente.getTelefono());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    cliente.setIdCliente(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nombreCompleto = ?, dni = ?, telefono = ?, updatedAt = CURRENT_TIMESTAMP WHERE idCliente = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNombreCompleto());
            stmt.setString(2, cliente.getDni());
            stmt.setString(3, cliente.getTelefono());
            stmt.setInt(4, cliente.getIdCliente());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
        }
        return false;
    }
    
    public boolean eliminar(int idCliente) {
        String sql = "UPDATE cliente SET deletedAt = CURRENT_TIMESTAMP WHERE idCliente = ?";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        }
        return false;
    }
    
    public Cliente buscarPorId(int idCliente) {
        String sql = "SELECT * FROM cliente WHERE idCliente = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearCliente(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por ID: " + e.getMessage());
        }
        return null;
    }
    
    public Cliente buscarPorDni(String dni) {
        String sql = "SELECT * FROM cliente WHERE dni = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearCliente(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por DNI: " + e.getMessage());
        }
        return null;
    }
    
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE deletedAt IS NULL ORDER BY nombreCompleto";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return clientes;
    }
    
    public List<Cliente> buscarPorNombre(String nombre) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nombreCompleto LIKE ? AND deletedAt IS NULL ORDER BY nombreCompleto";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes por nombre: " + e.getMessage());
        }
        return clientes;
    }
    
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("idCliente"));
        cliente.setNombreCompleto(rs.getString("nombreCompleto"));
        cliente.setDni(rs.getString("dni"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setCreatedAt(rs.getTimestamp("createdAt"));
        cliente.setUpdatedAt(rs.getTimestamp("updatedAt"));
        cliente.setDeletedAt(rs.getTimestamp("deletedAt"));
        return cliente;
    }
}