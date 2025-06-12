package Controller;

import DAO.DAOPedido;
import DAO.DAOItem;
import Model.Pedido;
import Model.Item;
import java.sql.SQLException;
import java.util.List;

public class ControladoraPedido {

    private final DAOPedido daoPedido;
    private final DAOItem daoItem;

    public ControladoraPedido() throws SQLException {
        this.daoPedido = new DAOPedido();
        this.daoItem = new DAOItem();
    }

    // Crear pedido y guardar sus ítems
    public boolean guardarPedido(Pedido pedido, List<Item> items) {
        boolean pedidoGuardado = daoPedido.crearPedido(pedido);
        if (pedidoGuardado) {
            for (Item item : items) {
                item.setIdPedido(pedido.getId()); // Suponiendo que el DAO lo asigna con LAST_INSERT_ID
                daoItem.agregarItem(item);
            }
            return true;
        }
        return false;
    }

    // Obtener todos los pedidos
    public List<Pedido> obtenerPedidos() {
        return daoPedido.obtenerTodos();
    }

    // Cambiar estado del pedido
    public boolean cambiarEstadoPedido(int idPedido, int nuevoEstadoId) {
        return daoPedido.cambiarEstado(idPedido, nuevoEstadoId);
    }

    // Eliminar pedido
    public boolean eliminarPedido(int idPedido) {
        return daoPedido.eliminar(idPedido);
    }

    // Buscar por cliente
    public List<Pedido> buscarPorCliente(int idCliente) {
        return daoPedido.obtenerPorCliente(idCliente);
    }

    // Buscar por estado
    public List<Pedido> buscarPorEstado(int idEstado) {
        return daoPedido.obtenerPorEstado(idEstado);
    }
}
