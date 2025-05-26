package app.dao;

import app.model.Producto;
import app.model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO producto (nombre) VALUES (?)";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, producto.getNombre());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    producto.setIdProducto(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, updatedAt = CURRENT_TIMESTAMP WHERE idProducto = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getIdProducto());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
        return false;
    }
    
    public boolean eliminar(int idProducto) {
        String sql = "UPDATE producto SET deletedAt = CURRENT_TIMESTAMP WHERE idProducto = ?";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProducto);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
        return false;
    }
    
    public Producto buscarPorId(int idProducto) {
        String sql = "SELECT * FROM producto WHERE idProducto = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearProducto(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar producto por ID: " + e.getMessage());
        }
        return null;
    }
    
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE deletedAt IS NULL ORDER BY nombre";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return productos;
    }
    
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE nombre LIKE ? AND deletedAt IS NULL ORDER BY nombre";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar productos por nombre: " + e.getMessage());
        }
        return productos;
    }
    
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("idProducto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setCreatedAt((jdk.jfr.Timestamp) rs.getTimestamp("createdAt"));
        producto.setUpdatedAt((jdk.jfr.Timestamp) rs.getTimestamp("updatedAt"));
        producto.setDeletedAt((jdk.jfr.Timestamp) rs.getTimestamp("deletedAt"));
        return producto;
    }
}