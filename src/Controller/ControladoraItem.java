package Controller;


import Model.Item;
import DAO.DAOItem;
import java.util.List;

public class ControladoraItem {
    private DAOItem dao = new DAOItem();

    public boolean guardarItem(Item item) {
        return dao.crearItem(item);
    }

    public List<Item> obtenerItemsDePedido(int idPedido) {
        return dao.obtenerItemsPorPedido(idPedido);
    }

    public boolean eliminarItem(int idItem) {
        return dao.eliminarItem(idItem);
    }

    public boolean actualizarItem(Item item) {
        return dao.actualizarItem(item);
    }
}
