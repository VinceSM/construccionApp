package app.dao;

import app.model.Pedido;
import app.model.Cliente;
import app.model.EstadoPedido;
import app.model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    private ClienteDAO clienteDAO = new ClienteDAO();
    private EstadoPedidoDAO estadoPedidoDAO = new EstadoPedidoDAO();
    
    public boolean insertar(Pedido pedido) {
        String sql = "INSERT INTO pedido (idCliente, idEstado, fechaPedido) VALUES (?, ?, ?)";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, pedido.getCliente().getIdCliente());
            stmt.setInt(2, pedido.getEstado().getIdEstado());
            stmt.setTimestamp(3, pedido.getFechaPedido());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    pedido.setIdPedido(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar pedido: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET idCliente = ?, idEstado = ?, updatedAt = CURRENT_TIMESTAMP WHERE idPedido = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pedido.getCliente().getIdCliente());
            stmt.setInt(2, pedido.getEstado().getIdEstado());
            stmt.setInt(3, pedido.getIdPedido());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar pedido: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizarEstado(int idPedido, int idEstado) {
        String sql = "UPDATE pedido SET idEstado = ?, updatedAt = CURRENT_TIMESTAMP WHERE idPedido = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEstado);
            stmt.setInt(2, idPedido);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del pedido: " + e.getMessage());
        }
        return false;
    }
    
    public boolean eliminar(int idPedido) {
        String sql = "UPDATE pedido SET deletedAt = CURRENT_TIMESTAMP WHERE idPedido = ?";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPedido);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
        }
        return false;
    }
    
    public Pedido buscarPorId(int idPedido) {
        String sql = "SELECT p.*, c.nombreCompleto, c.dni, c.telefono, e.tipo FROM pedido p " +
                    "INNER JOIN cliente c ON p.idCliente = c.idCliente " +
                    "INNER JOIN estado_pedido e ON p.idEstado = e.idEstado " +
                    "WHERE p.idPedido = ? AND p.deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearPedido(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar pedido por ID: " + e.getMessage());
        }
        return null;
    }
    
    public List<Pedido> buscarPorCliente(int idCliente) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombreCompleto, c.dni, c.telefono, e.tipo FROM pedido p " +
                    "INNER JOIN cliente c ON p.idCliente = c.idCliente " +
                    "INNER JOIN estado_pedido e ON p.idEstado = e.idEstado " +
                    "WHERE p.idCliente = ? AND p.deletedAt IS NULL ORDER BY p.fechaPedido DESC";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar pedidos por cliente: " + e.getMessage());
        }
        return pedidos;
    }
    
    public List<Pedido> buscarPorEstado(int idEstado) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombreCompleto, c.dni, c.telefono, e.tipo FROM pedido p " +
                    "INNER JOIN cliente c ON p.idCliente = c.idCliente " +
                    "INNER JOIN estado_pedido e ON p.idEstado = e.idEstado " +
                    "WHERE p.idEstado = ? AND p.deletedAt IS NULL ORDER BY p.fechaPedido DESC";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEstado);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar pedidos por estado: " + e.getMessage());
        }
        return pedidos;
    }
    
    public List<Pedido> buscarPorFecha(Date fechaInicio, Date fechaFin) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombreCompleto, c.dni, c.telefono, e.tipo FROM pedido p " +
                    "INNER JOIN cliente c ON p.idCliente = c.idCliente " +
                    "INNER JOIN estado_pedido e ON p.idEstado = e.idEstado " +
                    "WHERE DATE(p.fechaPedido) BETWEEN ? AND ? AND p.deletedAt IS NULL ORDER BY p.fechaPedido DESC";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, fechaInicio);
            stmt.setDate(2, fechaFin);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar pedidos por fecha: " + e.getMessage());
        }
        return pedidos;
    }
    
    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombreCompleto, c.dni, c.telefono, e.tipo FROM pedido p " +
                    "INNER JOIN cliente c ON p.idCliente = c.idCliente " +
                    "INNER JOIN estado_pedido e ON p.idEstado = e.idEstado " +
                    "WHERE p.deletedAt IS NULL ORDER BY p.fechaPedido DESC";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
        }
        return pedidos;
    }
    
    public List<Pedido> listarPedidosRecientes(int limite) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombreCompleto, c.dni, c.telefono, e.tipo FROM pedido p " +
                    "INNER JOIN cliente c ON p.idCliente = c.idCliente " +
                    "INNER JOIN estado_pedido e ON p.idEstado = e.idEstado " +
                    "WHERE p.deletedAt IS NULL ORDER BY p.fechaPedido DESC LIMIT ?";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limite);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos recientes: " + e.getMessage());
        }
        return pedidos;
    }
    
    public int contarPedidosPorEstado(int idEstado) {
        String sql = "SELECT COUNT(*) as total FROM pedido WHERE idEstado = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEstado);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar pedidos por estado: " + e.getMessage());
        }
        return 0;
    }
    
    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(rs.getInt("idPedido"));
        pedido.setFechaPedido(rs.getTimestamp("fechaPedido"));
        pedido.setUpdatedAt(rs.getTimestamp("updatedAt"));
        pedido.setDeletedAt(rs.getTimestamp("deletedAt"));
        
        // Mapear cliente
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("idCliente"));
        cliente.setNombreCompleto(rs.getString("nombreCompleto"));
        cliente.setDni(rs.getString("dni"));
        cliente.setTelefono(rs.getString("telefono"));
        pedido.setCliente(cliente);
        
        // Mapear estado
        EstadoPedido estado = new EstadoPedido();
        estado.setIdEstado(rs.getInt("idEstado"));
        estado.setTipo(rs.getString("tipo"));
        pedido.setEstado(estado);
        
        return pedido;
    }
}