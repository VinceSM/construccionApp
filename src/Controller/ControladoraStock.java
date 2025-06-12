package Controller;

import Model.Stock;
import DAO.DAOStock;
import java.util.List;

public class ControladoraStock {
    private DAOStock dao = new DAOStock();

    public boolean guardarStock(Stock stock) {
        return dao.crearStock(stock);
    }

    public List<Stock> obtenerStocks() {
        return dao.obtenerStocks();
    }

    public boolean eliminarStock(int idStock) {
        return dao.eliminarStock(idStock);
    }

    public void actualizarStock(Stock stock) {
        return dao.actualizarStock(stock);
    }

    public Stock obtenerPorId(int idStock) {
        return dao.obtenerPorId(idStock);
    }

    public Stock obtenerPorProducto(int idProducto) {
        return dao.obtenerPorProducto(idProducto);
    }
}
