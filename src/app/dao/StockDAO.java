package app.dao;

import app.model.Stock;
import app.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class StockDAO extends BaseDAO {
    
    private static final String TABLA = "stock";
    private static final String[] COLUMNAS = {
        "id_stock", "id_producto", "unidad", "medida",
        "created_at", "updated_at", "deleted_at"
    };
    
    private ProductoDAO productoDAO = new ProductoDAO();

    public boolean insertar(Stock stock) {
        String sql = String.format(
            "INSERT INTO %s (id_producto, unidad, medida) VALUES (?, ?, ?)", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, stock.getProducto().getIdProducto());
            stmt.setBigDecimal(2, stock.getUnidad());
            stmt.setString(3, stock.getMedida());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    stock.setIdStock(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar stock: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return false;
    }
    
    public boolean actualizar(Stock stock) {
        String sql = String.format(
            "UPDATE %s SET id_producto = ?, unidad = ?, medida = ?, " +
            "updated_at = CURRENT_TIMESTAMP WHERE id_stock = ? AND deleted_at IS NULL", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, stock.getProducto().getIdProducto());
            stmt.setBigDecimal(2, stock.getUnidad());
            stmt.setString(3, stock.getMedida());
            stmt.setInt(4, stock.getIdStock());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public boolean actualizarCantidad(int idProducto, BigDecimal nuevaCantidad) {
        String sql = String.format(
            "UPDATE %s SET unidad = ?, updated_at = CURRENT_TIMESTAMP " +
            "WHERE id_producto = ? AND deleted_at IS NULL", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setBigDecimal(1, nuevaCantidad);
            stmt.setInt(2, idProducto);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cantidad de stock: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public boolean reducirStock(int idProducto, BigDecimal cantidad) {
        String sql = String.format(
            "UPDATE %s SET unidad = unidad - ?, updated_at = CURRENT_TIMESTAMP " +
            "WHERE id_producto = ? AND deleted_at IS NULL AND unidad >= ?", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setBigDecimal(1, cantidad);
            stmt.setInt(2, idProducto);
            stmt.setBigDecimal(3, cantidad);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al reducir stock: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public boolean aumentarStock(int idProducto, BigDecimal cantidad) {
        String sql = String.format(
            "UPDATE %s SET unidad = unidad + ?, updated_at = CURRENT_TIMESTAMP " +
            "WHERE id_producto = ? AND deleted_at IS NULL", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setBigDecimal(1, cantidad);
            stmt.setInt(2, idProducto);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al aumentar stock: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public boolean eliminar(int idStock) {
        String sql = String.format(
            "UPDATE %s SET deleted_at = CURRENT_TIMESTAMP WHERE id_stock = ?", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idStock);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar stock: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public Stock buscarPorId(int idStock) {
        String sql = String.format(
            "SELECT s.*, p.nombre, p.descripcion FROM %s s " +
            "INNER JOIN producto p ON s.id_producto = p.id_producto " +
            "WHERE s.id_stock = ? AND s.deleted_at IS NULL", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idStock);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearStock(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar stock por ID: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return null;
    }
    
    public Stock buscarPorProducto(int idProducto) {
        String sql = String.format(
            "SELECT s.*, p.nombre, p.descripcion FROM %s s " +
            "INNER JOIN producto p ON s.id_producto = p.id_producto " +
            "WHERE s.id_producto = ? AND s.deleted_at IS NULL", 
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
                return mapearStock(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar stock por producto: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return null;
    }
    
    public List<Stock> listarTodos() {
        List<Stock> stocks = new ArrayList<>();
        String sql = String.format(
            "SELECT s.*, p.nombre, p.descripcion FROM %s s " +
            "INNER JOIN producto p ON s.id_producto = p.id_producto " +
            "WHERE s.deleted_at IS NULL ORDER BY p.nombre", 
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
                stocks.add(mapearStock(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar stocks: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return stocks;
    }
    
    public List<Stock> listarStockBajo(BigDecimal limite) {
        List<Stock> stocks = new ArrayList<>();
        String sql = String.format(
            "SELECT s.*, p.nombre, p.descripcion FROM %s s " +
            "INNER JOIN producto p ON s.id_producto = p.id_producto " +
            "WHERE s.unidad <= ? AND s.deleted_at IS NULL ORDER BY s.unidad ASC", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setBigDecimal(1, limite);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                stocks.add(mapearStock(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar stock bajo: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return stocks;
    }
    
    private Stock mapearStock(ResultSet rs) throws SQLException {
        Stock stock = new Stock();
        stock.setIdStock(rs.getInt("id_stock"));
        stock.setUnidad(rs.getBigDecimal("unidad"));
        stock.setMedida(rs.getString("medida"));
        stock.setCreatedAt(rs.getTimestamp("created_at"));
        stock.setUpdatedAt(rs.getTimestamp("updated_at"));
        stock.setDeletedAt(rs.getTimestamp("deleted_at"));
        
        // Mapear producto
        productoDAO.mapearProducto(rs);
        
        return stock;
    }
}