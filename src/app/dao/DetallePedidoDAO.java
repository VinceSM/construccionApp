package app.dao;

import app.model.DetallePedido;
import app.model.Pedido;
import app.model.Producto;
import app.model.Cliente;
import app.model.EstadoPedido;
import app.model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class DetallePedidoDAO {
    
    public boolean insertar(DetallePedido detalle) {
        String sql = "INSERT INTO detalle_pedido (idPedido, idProducto, cantidad, precio_producto) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, detalle.getPedido().getIdPedido());
            stmt.setInt(2, detalle.getProducto().getIdProducto());
            stmt.setBigDecimal(3, detalle.getCantidad());
            stmt.setBigDecimal(4, detalle.getPrecioProducto());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    detalle.setIdDetalle(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar detalle de pedido: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizar(DetallePedido detalle) {
        String sql = "UPDATE detalle_pedido SET idPedido = ?, idProducto = ?, cantidad = ?, precio_producto = ?, updatedAt = CURRENT_TIMESTAMP WHERE idDetalle = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, detalle.getPedido().getIdPedido());
            stmt.setInt(2, detalle.getProducto().getIdProducto());
            stmt.setBigDecimal(3, detalle.getCantidad());
            stmt.setBigDecimal(4, detalle.getPrecioProducto());
            stmt.setInt(5, detalle.getIdDetalle());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar detalle de pedido: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizarCantidad(int idDetalle, BigDecimal nuevaCantidad) {
        String sql = "UPDATE detalle_pedido SET cantidad = ?, updatedAt = CURRENT_TIMESTAMP WHERE idDetalle = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, nuevaCantidad);
            stmt.setInt(2, idDetalle);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cantidad del detalle: " + e.getMessage());
        }
        return false;
    }
    
    public boolean eliminar(int idDetalle) {
        String sql = "UPDATE detalle_pedido SET deletedAt = CURRENT_TIMESTAMP WHERE idDetalle = ?";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idDetalle);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle de pedido: " + e.getMessage());
        }
        return false;
    }
    
    public boolean eliminarPorPedido(int idPedido) {
        String sql = "UPDATE detalle_pedido SET deletedAt = CURRENT_TIMESTAMP WHERE idPedido = ?";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPedido);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalles del pedido: " + e.getMessage());
        }
        return false;
    }
    
    public DetallePedido buscarPorId(int idDetalle) {
        String sql = "SELECT dp.*, pr.nombre as producto_nombre, " +
                    "p.idPedido, p.fechaPedido, " +
                    "c.idCliente, c.nombreCompleto, c.dni, c.telefono, " +
                    "e.idEstado, e.tipo " +
                    "FROM detalle_pedido dp " +
                    "INNER JOIN producto pr ON dp.idProducto = pr.idProducto " +
                    "INNER JOIN pedido p ON dp.idPedido = p.idPedido " +
                    "INNER JOIN cliente c ON p.idCliente = c.idCliente " +
                    "INNER JOIN estado_pedido e ON p.idEstado = e.idEstado " +
                    "WHERE dp.idDetalle = ? AND dp.deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idDetalle);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearDetallePedido(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar detalle por ID: " + e.getMessage());
        }
        return null;
    }
    
    public List<DetallePedido> buscarPorPedido(int idPedido) {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT dp.*, pr.nombre as producto_nombre, " +
                    "p.idPedido, p.fechaPedido, " +
                    "c.idCliente, c.nombreCompleto, c.dni, c.telefono, " +
                    "e.idEstado, e.tipo " +
                    "FROM detalle_pedido dp " +
                    "INNER JOIN producto pr ON dp.idProducto = pr.idProducto " +
                    "INNER JOIN pedido p ON dp.idPedido = p.idPedido " +
                    "INNER JOIN cliente c ON p.idCliente = c.idCliente " +
                    "INNER JOIN estado_pedido e ON p.idEstado = e.idEstado " +
                    "WHERE dp.idPedido = ? AND dp.deletedAt IS NULL ORDER BY dp.idDetalle";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                detalles.add(mapearDetallePedido(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar detalles por pedido: " + e.getMessage());
        }
        return detalles;
    }
    
    public List<DetallePedido> buscarPorProducto(int idProducto) {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT dp.*, pr.nombre as producto_nombre, " +
                    "p.idPedido, p.fechaPedido, " +
                    "c.idCliente, c.nombreCompleto, c.dni, c.telefono, " +
                    "e.idEstado, e.tipo " +
                    "FROM detalle_pedido dp " +
                    "INNER JOIN producto pr ON dp.idProducto = pr.idProducto " +
                    "INNER JOIN pedido p ON dp.idPedido = p.idPedido " +
                    "INNER JOIN cliente c ON p.idCliente = c.idCliente " +
                    "INNER JOIN estado_pedido e ON p.idEstado = e.idEstado " +
                    "WHERE dp.idProducto = ? AND dp.deletedAt IS NULL ORDER BY p.fechaPedido DESC";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                detalles.add(mapearDetallePedido(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar detalles por producto: " + e.getMessage());
        }
        return detalles;
    }
    
    public BigDecimal calcularTotalPedido(int idPedido) {
        String sql = "SELECT SUM(subtotal) as total FROM detalle_pedido WHERE idPedido = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al calcular total del pedido: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
    
    public BigDecimal calcularCantidadVendidaProducto(int idProducto) {
        String sql = "SELECT SUM(dp.cantidad) as total FROM detalle_pedido dp " +
                    "INNER JOIN pedido p ON dp.idPedido = p.idPedido " +
                    "INNER JOIN estado_pedido e ON p.idEstado = e.idEstado " +
                    "WHERE dp.idProducto = ? AND e.tipo = 'Entregado' AND dp.deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al calcular cantidad vendida: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
    
    public List<DetallePedido> listarTodos() {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT dp.*, pr.nombre as producto_nombre, " +
                    "p.idPedido, p.fechaPedido, " +
                    "c.idCliente, c.nombreCompleto, c.dni, c.telefono, " +
                    "e.idEstado, e.tipo " +
                    "FROM detalle_pedido dp " +
                    "INNER JOIN producto pr ON dp.idProducto = pr.idProducto " +
                    "INNER JOIN pedido p ON dp.idPedido = p.idPedido " +
                    "INNER JOIN cliente c ON p.idCliente = c.idCliente " +
                    "INNER JOIN estado_pedido e ON p.idEstado = e.idEstado " +
                    "WHERE dp.deletedAt IS NULL ORDER BY p.fechaPedido DESC, dp.idDetalle";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                detalles.add(mapearDetallePedido(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar detalles: " + e.getMessage());
        }
        return detalles;
    }
    
    private DetallePedido mapearDetallePedido(ResultSet rs) throws SQLException {
        DetallePedido detalle = new DetallePedido();
        detalle.setIdDetalle(rs.getInt("idDetalle"));
        detalle.setCantidad(rs.getBigDecimal("cantidad"));
        detalle.setPrecioProducto(rs.getBigDecimal("precio_producto"));
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));
        detalle.setCreatedAt(rs.getTimestamp("createdAt"));
        detalle.setUpdatedAt(rs.getTimestamp("updatedAt"));
        detalle.setDeletedAt(rs.getTimestamp("deletedAt"));
        
        // Mapear producto
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("idProducto"));
        producto.setNombre(rs.getString("producto_nombre"));
        detalle.setProducto(producto);
        
        // Mapear pedido
        Pedido pedido = new Pedido();
        pedido.setIdPedido(rs.getInt("idPedido"));
        pedido.setFechaPedido(rs.getTimestamp("fechaPedido"));
        
        // Mapear cliente del pedido
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("idCliente"));
        cliente.setNombreCompleto(rs.getString("nombreCompleto"));
        cliente.setDni(rs.getString("dni"));
        cliente.setTelefono(rs.getString("telefono"));
        pedido.setCliente(cliente);
        
        // Mapear estado del pedido
        EstadoPedido estado = new EstadoPedido();
        estado.setIdEstado(rs.getInt("idEstado"));
        estado.setTipo(rs.getString("tipo"));
        pedido.setEstado(estado);
        
        detalle.setPedido(pedido);
        
        return detalle;
    }
}