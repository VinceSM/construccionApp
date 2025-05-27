package app.dao;

import app.model.EstadoPedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadoPedidoDAO extends BaseDAO {
    
    private static final String TABLA = "estado_pedido";
    private static final String[] COLUMNAS = {
        "id_estado", "tipo", "created_at", "updated_at", "deleted_at"
    };

    public boolean insertar(EstadoPedido estado) {
        String sql = String.format(
            "INSERT INTO %s (tipo) VALUES (?)", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, estado.getTipo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    estado.setIdEstado(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar estado de pedido: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return false;
    }
    
    public boolean actualizar(EstadoPedido estado) {
        String sql = String.format(
            "UPDATE %s SET tipo = ?, updated_at = CURRENT_TIMESTAMP " +
            "WHERE id_estado = ? AND deleted_at IS NULL", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, estado.getTipo());
            stmt.setInt(2, estado.getIdEstado());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de pedido: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public boolean eliminar(int idEstado) {
        String sql = String.format(
            "UPDATE %s SET deleted_at = CURRENT_TIMESTAMP WHERE id_estado = ?", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idEstado);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar estado de pedido: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return false;
    }
    
    public EstadoPedido buscarPorId(int idEstado) {
        String sql = String.format(
            "SELECT * FROM %s WHERE id_estado = ? AND deleted_at IS NULL", 
            TABLA
        );
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, idEstado);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearEstadoPedido(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar estado de pedido por ID: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return null;
    }
    
    public List<EstadoPedido> listarTodos() {
        List<EstadoPedido> estados = new ArrayList<>();
        String sql = String.format(
            "SELECT * FROM %s WHERE deleted_at IS NULL ORDER BY tipo", 
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
                estados.add(mapearEstadoPedido(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar estados de pedido: " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return estados;
    }
    
    public EstadoPedido mapearEstadoPedido(ResultSet rs) throws SQLException {
        EstadoPedido estado = new EstadoPedido();
        estado.setIdEstado(rs.getInt("id_estado"));
        estado.setTipo(rs.getString("tipo"));
        estado.setCreatedAt(rs.getTimestamp("created_at"));
        estado.setUpdatedAt(rs.getTimestamp("updated_at"));
        estado.setDeletedAt(rs.getTimestamp("deleted_at"));
        return estado;
    }
}