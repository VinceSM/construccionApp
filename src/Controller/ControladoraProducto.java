/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Producto;
import DAO.DAOProducto;
import java.util.List;

public class ControladoraProducto {
    private DAOProducto dao = new DAOProducto();

    public boolean guardarProducto(Producto producto) {
        return dao.crearProducto(producto);
    }

    public List<Producto> obtenerProductos() {
        return dao.obtenerProductos();
    }

    public boolean eliminarProducto(int idProducto) {
        return dao.eliminarProducto(idProducto);
    }

    public boolean actualizarProducto(Producto producto) {
        return dao.actualizarProducto(producto);
    }

    public Producto obtenerPorId(int idProducto) {
        return dao.obtenerPorId(idProducto);
    }
}
