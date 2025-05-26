package app.dao;

import app.model.EstadoPedido;
import app.model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadoPedidoDAO {
    
    public boolean insertar(EstadoPedido estado) {
        String sql = "INSERT INTO estado_pedido (tipo) VALUES (?)";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, estado.getTipo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    estado.setIdEstado(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar estado de pedido: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizar(EstadoPedido estado) {
        String sql = "UPDATE estado_pedido SET tipo = ?, updatedAt = CURRENT_TIMESTAMP WHERE idEstado = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.getTipo());
            stmt.setInt(2, estado.getIdEstado());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de pedido: " + e.getMessage());
        }
        return false;
    }
    
    public boolean eliminar(int idEstado) {
        String sql = "UPDATE estado_pedido SET deletedAt = CURRENT_TIMESTAMP WHERE idEstado = ?";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEstado);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar estado de pedido: " + e.getMessage());
        }
        return false;
    }
    
    public EstadoPedido buscarPorId(int idEstado) {
        String sql = "SELECT * FROM estado_pedido WHERE idEstado = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEstado);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearEstadoPedido(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar estado de pedido por ID: " + e.getMessage());
        }
        return null;
    }
    
    public EstadoPedido buscarPorTipo(String tipo) {
        String sql = "SELECT * FROM estado_pedido WHERE tipo = ? AND deletedAt IS NULL";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearEstadoPedido(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar estado de pedido por tipo: " + e.getMessage());
        }
        return null;
    }
    
    public List<EstadoPedido> listarTodos() {
        List<EstadoPedido> estados = new ArrayList<>();
        String sql = "SELECT * FROM estado_pedido WHERE deletedAt IS NULL ORDER BY tipo";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                estados.add(mapearEstadoPedido(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar estados de pedido: " + e.getMessage());
        }
        return estados;
    }
    
    public List<EstadoPedido> buscarPorTipoLike(String tipo) {
        List<EstadoPedido> estados = new ArrayList<>();
        String sql = "SELECT * FROM estado_pedido WHERE tipo LIKE ? AND deletedAt IS NULL ORDER BY tipo";
        
        try (Connection conn = Conexion.Conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + tipo + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                estados.add(mapearEstadoPedido(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar estados de pedido por tipo: " + e.getMessage());
        }
        return estados;
    }
    
    private EstadoPedido mapearEstadoPedido(ResultSet rs) throws SQLException {
        EstadoPedido estado = new EstadoPedido();
        estado.setIdEstado(rs.getInt("idEstado"));
        estado.setTipo(rs.getString("tipo"));
        estado.setCreatedAt(rs.getTimestamp("createdAt"));
        estado.setUpdatedAt(rs.getTimestamp("updatedAt"));
        estado.setDeletedAt(rs.getTimestamp("deletedAt"));
        return estado;
    }
}