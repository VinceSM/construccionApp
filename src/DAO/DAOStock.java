package DAO;

import Model.Producto;
import Model.Stock;
import Model.Conexion;
import java.sql.*;
import java.util.List;

public class DAOStock {
    private Connection conexion;

    public DAOStock() throws SQLException {
        conexion = Conexion.Conectar();
    }

    public void actualizarStock(Stock stock) throws SQLException {
        String sql = "UPDATE stock SET unidad = ?, medida = ? WHERE id_producto = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setDouble(1, stock.getUnidad());
            stmt.setString(2, stock.getMedida());
            stmt.setInt(3, stock.getProducto().getIdProducto());
            stmt.executeUpdate();
        }
    }

    public Stock obtenerStockPorProducto(int idProducto) throws SQLException {
        String sql = "SELECT * FROM stock WHERE id_producto = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Producto producto = new DAOProducto().buscarPorId(idProducto);
                return new Stock(
                    rs.getInt("idStock"),
                    rs.getDouble("unidad"),
                    rs.getString("medida"),
                    producto
                );
            }
        }
        return null;
    }

    public boolean crearStock(Stock stock) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Stock> obtenerStocks() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean eliminarStock(int idStock) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Stock obtenerPorId(int idStock) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Stock obtenerPorProducto(int idProducto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
