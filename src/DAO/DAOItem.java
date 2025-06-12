package DAO;

import Model.Item;
import Model.Conexion;
import java.sql.*;
import java.util.List;

public class DAOItem {
    private Connection conexion;

    public DAOItem() throws SQLException {
        conexion = Conexion.Conectar();
    }

    public void crearItem(Item item, int idPedido) throws SQLException {
        String sql = "INSERT INTO item (cantidad, id_producto, id_pedido) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, item.getCantidad());
            stmt.setInt(2, item.getProducto().getIdProducto());
            stmt.setInt(3, idPedido);
            stmt.executeUpdate();
        }
    }

    public void agregarItem(Item item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean crearItem(Item item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Item> obtenerItemsPorPedido(int idPedido) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean eliminarItem(int idItem) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean actualizarItem(Item item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
