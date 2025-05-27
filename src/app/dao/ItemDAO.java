package app.dao;

import app.model.Item;
import app.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO extends BaseDAO {
    
    private ProductoDAO productoDAO = new ProductoDAO();
    
    public boolean insertar(Item item, int idPedido) {
        String sql = "INSERT INTO item (id_pedido, id_producto, cantidad) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, idPedido);
            stmt.setInt(2, item.getProducto().getIdProducto());
            stmt.setInt(3, item.getCantidad());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar item: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return false;
    }
    
    public boolean actualizar(Item item) {
        String sql = "UPDATE item SET id_producto = ?, cantidad = ? WHERE id_item = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, item.getProducto().getIdProducto());
            stmt.setInt(2, item.getCantidad());
            stmt.setInt(3, item.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar item: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public boolean eliminar(int idItem) {
        String sql = "DELETE FROM item WHERE id_item = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idItem);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar item: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public boolean eliminarPorPedido(int idPedido) {
        String sql = "DELETE FROM item WHERE id_pedido = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, idPedido);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar items por pedido: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public Item buscarPorId(int idItem) {
        String sql = "SELECT i.*, p.* FROM item i JOIN producto p ON i.id_producto = p.id_producto WHERE i.id_item = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idItem);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearItem(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar item por ID: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return null;
    }
    
    public List<Item> buscarPorPedido(int idPedido) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT i.*, p.* FROM item i JOIN producto p ON i.id_producto = p.id_producto WHERE i.id_pedido = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idPedido);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                items.add(mapearItem(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar items por pedido: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return items;
    }
    
    public Item mapearItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id_item"));
        item.setCantidad(rs.getInt("cantidad"));
        
        //Mapear Prodcuto
        productoDAO.mapearProducto(rs);
        return item;
    }
}