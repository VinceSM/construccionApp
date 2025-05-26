package app.dao;

import app.model.Stock;
import app.model.Producto;
import app.model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class StockDAO {
    private ProductoDAO productoDAO = new ProductoDAO();
    
    public boolean insertar(Stock stock) {
        String sql = "INSERT INTO stock (idProducto, unidad, medida) VALUES (?, ?, ?)";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, stock.getProducto().getIdProducto());
            stmt.setBigDecimal(2, stock.getUnidad());
            stmt.setString(3, stock.getMedida());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    stock.setIdStock(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar stock: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizar(Stock stock) {
        String sql = "UPDATE stock SET idProducto = ?, unidad = ?, medida = ?, updatedAt = CURRENT_TIMESTAMP WHERE idStock = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, stock.getProducto().getIdProducto());
            stmt.setBigDecimal(2, stock.getUnidad());
            stmt.setString(3, stock.getMedida());
            stmt.setInt(4, stock.getIdStock());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizarCantidad(int idProducto, BigDecimal nuevaCantidad) {
        String sql = "UPDATE stock SET unidad = ?, updatedAt = CURRENT_TIMESTAMP WHERE idProducto = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, nuevaCantidad);
            stmt.setInt(2, idProducto);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cantidad de stock: " + e.getMessage());
        }
        return false;
    }
    
    public boolean reducirStock(int idProducto, BigDecimal cantidad) {
        String sql = "UPDATE stock SET unidad = unidad - ?, updatedAt = CURRENT_TIMESTAMP WHERE idProducto = ? AND deletedAt IS NULL AND unidad >= ?";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, cantidad);
            stmt.setInt(2, idProducto);
            stmt.setBigDecimal(3, cantidad);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al reducir stock: " + e.getMessage());
        }
        return false;
    }
    
    public boolean aumentarStock(int idProducto, BigDecimal cantidad) {
        String sql = "UPDATE stock SET unidad = unidad + ?, updatedAt = CURRENT_TIMESTAMP WHERE idProducto = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, cantidad);
            stmt.setInt(2, idProducto);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al aumentar stock: " + e.getMessage());
        }
        return false;
    }
    
    public boolean eliminar(int idStock) {
        String sql = "UPDATE stock SET deletedAt = CURRENT_TIMESTAMP WHERE idStock = ?";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idStock);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar stock: " + e.getMessage());
        }
        return false;
    }
    
    public Stock buscarPorId(int idStock) {
        String sql = "SELECT s.*, p.nombre FROM stock s INNER JOIN producto p ON s.idProducto = p.idProducto WHERE s.idStock = ? AND s.deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idStock);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearStock(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar stock por ID: " + e.getMessage());
        }
        return null;
    }
    
    public Stock buscarPorProducto(int idProducto) {
        String sql = "SELECT s.*, p.nombre FROM stock s INNER JOIN producto p ON s.idProducto = p.idProducto WHERE s.idProducto = ? AND s.deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearStock(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar stock por producto: " + e.getMessage());
        }
        return null;
    }
    
    public List<Stock> listarTodos() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT s.*, p.nombre FROM stock s INNER JOIN producto p ON s.idProducto = p.idProducto WHERE s.deletedAt IS NULL ORDER BY p.nombre";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                stocks.add(mapearStock(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar stocks: " + e.getMessage());
        }
        return stocks;
    }
    
    public List<Stock> listarStockBajo(BigDecimal limite) {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT s.*, p.nombre FROM stock s INNER JOIN producto p ON s.idProducto = p.idProducto WHERE s.unidad <= ? AND s.deletedAt IS NULL ORDER BY s.unidad ASC";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, limite);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                stocks.add(mapearStock(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar stock bajo: " + e.getMessage());
        }
        return stocks;
    }
    
    private Stock mapearStock(ResultSet rs) throws SQLException {
        Stock stock = new Stock();
        stock.setIdStock(rs.getInt("idStock"));
        stock.setUnidad(rs.getBigDecimal("unidad"));
        stock.setMedida(rs.getString("medida"));
        stock.setCreatedAt(rs.getTimestamp("createdAt"));
        stock.setUpdatedAt(rs.getTimestamp("updatedAt"));
        stock.setDeletedAt(rs.getTimestamp("deletedAt"));
        
        // Mapear producto
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("idProducto"));
        producto.setNombre(rs.getString("nombre"));
        stock.setProducto(producto);
        
        return stock;
    }
}