package app.dao;

import app.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO extends BaseDAO {
    
    private static final String TABLA = "cliente";
    private static final String[] COLUMNAS = {
        "id_cliente", "nombre_completo", "dni", "telefono", 
        "created_at", "updated_at", "deleted_at"
    };

    public boolean insertar(Cliente cliente) {
        String sql = String.format(
            "INSERT INTO %s (nombre_completo, dni, telefono) VALUES (?, ?, ?)", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, cliente.getNombreCompleto());
            stmt.setString(2, cliente.getDni());
            stmt.setString(3, cliente.getTelefono());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    cliente.setIdCliente(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return false;
    }
    
    public boolean actualizar(Cliente cliente) {
        String sql = String.format(
            "UPDATE %s SET nombre_completo = ?, dni = ?, telefono = ?, " +
            "updated_at = CURRENT_TIMESTAMP WHERE id_cliente = ? AND deleted_at IS NULL", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, cliente.getNombreCompleto());
            stmt.setString(2, cliente.getDni());
            stmt.setString(3, cliente.getTelefono());
            stmt.setInt(4, cliente.getIdCliente());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public boolean eliminar(int idCliente) {
        String sql = String.format(
            "UPDATE %s SET deleted_at = CURRENT_TIMESTAMP WHERE id_cliente = ?", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idCliente);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public Cliente buscarPorId(int idCliente) {
        String sql = String.format(
            "SELECT * FROM %s WHERE id_cliente = ? AND deleted_at IS NULL", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idCliente);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearCliente(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por ID: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return null;
    }
    
    public Cliente buscarPorDni(String dni) {
        String sql = String.format(
            "SELECT * FROM %s WHERE dni = ? AND deleted_at IS NULL", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, dni);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearCliente(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por DNI: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return null;
    }
    
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = String.format(
            "SELECT * FROM %s WHERE deleted_at IS NULL ORDER BY nombre_completo", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return clientes;
    }
    
    public List<Cliente> buscarPorNombre(String nombre) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = String.format(
            "SELECT * FROM %s WHERE nombre_completo LIKE ? AND deleted_at IS NULL ORDER BY nombre_completo", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, "%" + nombre + "%");
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes por nombre: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return clientes;
    }
    
    public Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setNombreCompleto(rs.getString("nombre_completo"));
        cliente.setDni(rs.getString("dni"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setCreatedAt(rs.getTimestamp("created_at"));
        cliente.setUpdatedAt(rs.getTimestamp("updated_at"));
        cliente.setDeletedAt(rs.getTimestamp("deleted_at"));
        return cliente;
    }
}