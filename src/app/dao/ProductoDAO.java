package app.dao;

import app.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO extends BaseDAO {
    
    private static final String TABLA = "producto";
    private static final String[] COLUMNAS = {
        "id_producto", "nombre", "descripcion", "precio", "costo",
        "created_at", "updated_at", "deleted_at"
    };

    public boolean insertar(Producto producto) {
        String sql = String.format(
            "INSERT INTO %s (nombre, descripcion, precio, costo) VALUES (?, ?, ?, ?)", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setDouble(4, producto.getCosto());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    producto.setIdProducto(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return false;
    }
    
    public boolean actualizar(Producto producto) {
        String sql = String.format(
            "UPDATE %s SET nombre = ?, descripcion = ?, precio = ?, costo = ?, " +
            "updated_at = CURRENT_TIMESTAMP WHERE id_producto = ? AND deleted_at IS NULL", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setDouble(4, producto.getCosto());
            stmt.setInt(5, producto.getIdProducto());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public boolean eliminar(int idProducto) {
        String sql = String.format(
            "UPDATE %s SET deleted_at = CURRENT_TIMESTAMP WHERE id_producto = ?", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idProducto);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public Producto buscarPorId(int idProducto) {
        String sql = String.format(
            "SELECT * FROM %s WHERE id_producto = ? AND deleted_at IS NULL", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idProducto);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearProducto(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar producto por ID: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return null;
    }
    
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = String.format(
            "SELECT * FROM %s WHERE deleted_at IS NULL ORDER BY nombre", 
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
                productos.add(mapearProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return productos;
    }
    
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productos = new ArrayList<>();
        String sql = String.format(
            "SELECT * FROM %s WHERE nombre LIKE ? AND deleted_at IS NULL ORDER BY nombre", 
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
                productos.add(mapearProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar productos por nombre: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return productos;
    }
    
    public Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setCosto(rs.getDouble("costo"));
        producto.setCreatedAt((jdk.jfr.Timestamp) rs.getTimestamp("created_at"));
        producto.setUpdatedAt((jdk.jfr.Timestamp) rs.getTimestamp("updated_at"));
        producto.setDeletedAt((jdk.jfr.Timestamp) rs.getTimestamp("deleted_at"));
        return producto;
    }
}