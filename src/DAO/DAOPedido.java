package DAO;

import Model.Item;
import Model.Pedido;
import Model.Conexion;
import java.sql.*;
import java.util.List;

public class DAOPedido {
    private Connection conexion;

    public DAOPedido() throws SQLException {
        conexion = Conexion.Conectar();
    }

    public int crearPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (fecha_pedido, id_cliente, id_estado) VALUES (?, ?, ?)";
        int idGenerado = -1;

        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pedido.getFechaPedido());
            stmt.setInt(2, pedido.getCliente().getIdCliente());
            stmt.setInt(3, pedido.getEstado().getIdEstado());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }

            // Guardar los items del pedido
            for (Item item : pedido.getItems()) {
                new DAOItem().crearItem(item, idGenerado);
            }
        }

        return idGenerado;
    }

    public List<Pedido> listarPedidos() throws SQLException {
        // Implementar si es necesario más adelante
        return null;
    }

    public List<Pedido> obtenerPorEstado(int idEstado) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Pedido> obtenerPorCliente(int idCliente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean eliminar(int idPedido) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean cambiarEstado(int idPedido, int nuevoEstadoId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Pedido> obtenerTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
