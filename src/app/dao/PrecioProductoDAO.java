package app.dao;

import app.model.PrecioProducto;
import app.model.Producto;
import app.model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class PrecioProductoDAO {
    private ProductoDAO productoDAO = new ProductoDAO();
    
    public boolean insertar(PrecioProducto precio) {
        String sql = "INSERT INTO precio_producto (idProducto, moneda, monto) VALUES (?, ?, ?)";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, precio.getProducto().getIdProducto());
            stmt.setString(2, precio.getMoneda());
            stmt.setBigDecimal(3, precio.getMonto());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    precio.setIdPrecio(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar precio de producto: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizar(PrecioProducto precio) {
        String sql = "UPDATE precio_producto SET idProducto = ?, moneda = ?, monto = ?, updatedAt = CURRENT_TIMESTAMP WHERE idPrecio = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, precio.getProducto().getIdProducto());
            stmt.setString(2, precio.getMoneda());
            stmt.setBigDecimal(3, precio.getMonto());
            stmt.setInt(4, precio.getIdPrecio());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar precio de producto: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizarPrecio(int idProducto, String moneda, BigDecimal nuevoPrecio) {
        String sql = "UPDATE precio_producto SET monto = ?, updatedAt = CURRENT_TIMESTAMP WHERE idProducto = ? AND moneda = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, nuevoPrecio);
            stmt.setInt(2, idProducto);
            stmt.setString(3, moneda);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar precio: " + e.getMessage());
        }
        return false;
    }
    
    public boolean eliminar(int idPrecio) {
        String sql = "UPDATE precio_producto SET deletedAt = CURRENT_TIMESTAMP WHERE idPrecio = ?";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPrecio);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar precio de producto: " + e.getMessage());
        }
        return false;
    }
    
    public PrecioProducto buscarPorId(int idPrecio) {
        String sql = "SELECT pp.*, p.nombre FROM precio_producto pp INNER JOIN producto p ON pp.idProducto = p.idProducto WHERE pp.idPrecio = ? AND pp.deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPrecio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearPrecioProducto(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar precio por ID: " + e.getMessage());
        }
        return null;
    }
    
    public PrecioProducto buscarPrecioActualPorProducto(int idProducto, String moneda) {
        String sql = "SELECT pp.*, p.nombre FROM precio_producto pp INNER JOIN producto p ON pp.idProducto = p.idProducto WHERE pp.idProducto = ? AND pp.moneda = ? AND pp.deletedAt IS NULL ORDER BY pp.createdAt DESC LIMIT 1";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProducto);
            stmt.setString(2, moneda);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearPrecioProducto(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar precio actual del producto: " + e.getMessage());
        }
        return null;
    }
    
    public List<PrecioProducto> buscarPorProducto(int idProducto) {
        List<PrecioProducto> precios = new ArrayList<>();
        String sql = "SELECT pp.*, p.nombre FROM precio_producto pp INNER JOIN producto p ON pp.idProducto = p.idProducto WHERE pp.idProducto = ? AND pp.deletedAt IS NULL ORDER BY pp.createdAt DESC";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                precios.add(mapearPrecioProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar precios por producto: " + e.getMessage());
        }
        return precios;
    }
    
    public List<PrecioProducto> listarTodos() {
        List<PrecioProducto> precios = new ArrayList<>();
        String sql = "SELECT pp.*, p.nombre FROM precio_producto pp INNER JOIN producto p ON pp.idProducto = p.idProducto WHERE pp.deletedAt IS NULL ORDER BY p.nombre, pp.moneda";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                precios.add(mapearPrecioProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar precios: " + e.getMessage());
        }
        return precios;
    }
    
    public List<PrecioProducto> buscarPorMoneda(String moneda) {
        List<PrecioProducto> precios = new ArrayList<>();
        String sql = "SELECT pp.*, p.nombre FROM precio_producto pp INNER JOIN producto p ON pp.idProducto = p.idProducto WHERE pp.moneda = ? AND pp.deletedAt IS NULL ORDER BY p.nombre";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, moneda);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                precios.add(mapearPrecioProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar precios por moneda: " + e.getMessage());
        }
        return precios;
    }
    
    public List<PrecioProducto> buscarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax, String moneda) {
        List<PrecioProducto> precios = new ArrayList<>();
        String sql = "SELECT pp.*, p.nombre FROM precio_producto pp INNER JOIN producto p ON pp.idProducto = p.idProducto WHERE pp.monto BETWEEN ? AND ? AND pp.moneda = ? AND pp.deletedAt IS NULL ORDER BY pp.monto";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, precioMin);
            stmt.setBigDecimal(2, precioMax);
            stmt.setString(3, moneda);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                precios.add(mapearPrecioProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar precios por rango: " + e.getMessage());
        }
        return precios;
    }
    
    private PrecioProducto mapearPrecioProducto(ResultSet rs) throws SQLException {
        PrecioProducto precio = new PrecioProducto();
        precio.setIdPrecio(rs.getInt("idPrecio"));
        precio.setMoneda(rs.getString("moneda"));
        precio.setMonto(rs.getBigDecimal("monto"));
        precio.setCreatedAt(rs.getTimestamp("createdAt"));
        precio.setUpdatedAt(rs.getTimestamp("updatedAt"));
        precio.setDeletedAt(rs.getTimestamp("deletedAt"));
        
        // Mapear producto
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("idProducto"));
        producto.setNombre(rs.getString("nombre"));
        precio.setProducto(producto);
        
        return precio;
    }
}