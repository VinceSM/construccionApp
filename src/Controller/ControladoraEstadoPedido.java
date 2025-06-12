package Controller;


import Model.EstadoPedido;
import DAO.DAOEstadoPedido;
import java.util.List;

public class ControladoraEstadoPedido {
    private DAOEstadoPedido dao = new DAOEstadoPedido();

    public void guardarEstado(EstadoPedido estado) {
        return dao.crearEstado(estado);
    }

    public List<EstadoPedido> obtenerEstados() {
        return dao.obtenerEstados();
    }

    public EstadoPedido obtenerPorId(int idEstado) {
        return dao.obtenerPorId(idEstado);
    }
}

