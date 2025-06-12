package DAO;

import Model.EstadoPedido;
import Model.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOEstadoPedido {
    private Connection conexion;

    public DAOEstadoPedido() throws SQLException {
        conexion = Conexion.Conectar();
    }

    public void crearEstado(EstadoPedido estado) throws SQLException {
        String sql = "INSERT INTO estado_pedido (tipo) VALUES (?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, estado.getTipo());
            stmt.executeUpdate();
        }
    }

    public List<EstadoPedido> listarEstados() throws SQLException {
        List<EstadoPedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM estado_pedido";
        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                EstadoPedido estado = new EstadoPedido(
                    rs.getInt("idestado"),
                    rs.getString("tipo")
                );
                lista.add(estado);
            }
        }
        return lista;
    }

    public EstadoPedido buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM estado_pedido WHERE idestado = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new EstadoPedido(
                    rs.getInt("idestado"),
                    rs.getString("tipo")
                );
            }
        }
        return null;
    }

    public List<EstadoPedido> obtenerEstados() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public EstadoPedido obtenerPorId(int idEstado) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
