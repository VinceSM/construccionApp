package Controller;


import Model.Cliente;
import DAO.DAOCliente;
import java.sql.SQLException;
import java.util.List;

public class ControladoraCliente {
    private DAOCliente dao = new DAOCliente();

    public boolean guardarCliente(Cliente cliente) throws SQLException {
        return dao.crearCliente(cliente); 
    }

    public List<Cliente> obtenerClientes() {
        return dao.obtenerClientes();
    }

    public boolean eliminarCliente(int idCliente) {
        return dao.eliminarCliente(idCliente);
    }

    public boolean actualizarCliente(Cliente cliente) {
        return dao.actualizarCliente(cliente);
    }

    public Cliente obtenerPorId(int idCliente) {
        return dao.obtenerPorId(idCliente);
    }
}
