package app.dao;

import app.model.Pedido;
import app.model.Cliente;
import app.model.EstadoPedido;
import app.model.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO extends BaseDAO {
    
    private ItemDAO itemDAO = new ItemDAO();
    private EstadoPedidoDAO estadoDAO = new EstadoPedidoDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    
    public boolean insertar(Pedido pedido) {
        String sql = "INSERT INTO pedido (id_cliente, id_estado, fecha_pedido, total) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Insertar el pedido
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, pedido.getCliente().getIdCliente());
            stmt.setInt(2, pedido.getEstado().getIdEstado());
            stmt.setTimestamp(3, pedido.getFechaPedido());
            stmt.setDouble(4, pedido.getTotal());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                conn.rollback();
                return false;
            }
            
            // Obtener el ID generado
            rs = stmt.getGeneratedKeys();
            int idPedido = 0;
            if (rs.next()) {
                idPedido = rs.getInt(1);
                pedido.setIdPedido(idPedido);
            }
            
            // 2. Insertar los items
            ItemDAO itemDAO = new ItemDAO();
            for (Item item : pedido.getItems()) {
                if (!itemDAO.insertar(item, idPedido)) {
                    conn.rollback();
                    return false;
                }
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error al insertar pedido: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar auto-commit: " + e.getMessage());
            }
            cerrarRecursos(conn, stmt, rs);
        }
        return false;
    }
    
    public boolean actualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET id_cliente = ?, id_estado = ?, total = ?, updated_at = CURRENT_TIMESTAMP WHERE id_pedido = ? AND deleted_at IS NULL";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            conn.setAutoCommit(false);
            
            // 1. Actualizar el pedido
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, pedido.getCliente().getIdCliente());
            stmt.setInt(2, pedido.getEstado().getIdEstado());
            stmt.setDouble(3, pedido.getTotal());
            stmt.setInt(4, pedido.getIdPedido());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                conn.rollback();
                return false;
            }
            
            // 2. Actualizar items (eliminar los antiguos y crear nuevos)
            ItemDAO itemDAO = new ItemDAO();
            
            // Eliminar items antiguos
            if (!itemDAO.eliminarPorPedido(pedido.getIdPedido())) {
                conn.rollback();
                return false;
            }
            
            // Insertar nuevos items
            for (Item item : pedido.getItems()) {
                if (!itemDAO.insertar(item, pedido.getIdPedido())) {
                    conn.rollback();
                    return false;
                }
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error al actualizar pedido: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar auto-commit: " + e.getMessage());
            }
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public boolean eliminar(int idPedido) {
        String sql = "UPDATE pedido SET deleted_at = CURRENT_TIMESTAMP WHERE id_pedido = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idPedido);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public Pedido buscarPorId(int idPedido) {
        String sql = "SELECT p.*, c.*, e.* FROM pedido p " +
                     "JOIN cliente c ON p.id_cliente = c.id_cliente " +
                     "JOIN estado_pedido e ON p.id_estado = e.id_estado " +
                     "WHERE p.id_pedido = ? AND p.deleted_at IS NULL";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idPedido);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Pedido pedido = mapearPedido(rs);
                
                // Obtener los items del pedido
                ItemDAO itemDAO = new ItemDAO();
                List<Item> items = itemDAO.buscarPorPedido(idPedido);
                pedido.setItems(items);
                
                return pedido;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar pedido por ID: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return null;
    }
    
    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, c.*, e.* FROM pedido p " +
                     "JOIN cliente c ON p.id_cliente = c.id_cliente " +
                     "JOIN estado_pedido e ON p.id_estado = e.id_estado " +
                     "WHERE p.deleted_at IS NULL ORDER BY p.fecha_pedido DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Pedido pedido = mapearPedido(rs);
                
                // Obtener los items del pedido
                ItemDAO itemDAO = new ItemDAO();
                List<Item> items = itemDAO.buscarPorPedido(pedido.getIdPedido());
                pedido.setItems(items);
                
                pedidos.add(pedido);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return pedidos;
    }
    
    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(rs.getInt("id_pedido"));
        pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
        pedido.calcularTotal(); // solucion provisoria
        pedido.setUpdatedAt(rs.getTimestamp("updated_at"));
        pedido.setDeletedAt(rs.getTimestamp("deleted_at"));
        
        // Mapear cliente
        clienteDAO.mapearCliente(rs);
        
        // Mapear estado
        estadoDAO.mapearEstadoPedido(rs);
        
        // Mapear item
        itemDAO.mapearItem(rs);
        
        return pedido;
    }
}