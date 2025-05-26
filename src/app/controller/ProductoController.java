package app.controller;

import app.dao.ProductoDAO;
import app.model.Producto;
import java.util.List;

public class ProductoController {
    private ProductoDAO productoDAO;
    
    public ProductoController() {
        this.productoDAO = new ProductoDAO();
    }
    
    public boolean registrarProducto(String nombre) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("El nombre del producto es obligatorio");
            return false;
        }
        
        Producto producto = new Producto();
        producto.setNombre(nombre.trim());
        
        boolean resultado = productoDAO.insertar(producto);
        if (resultado) {
            System.out.println("Producto registrado exitosamente con ID: " + producto.getIdProducto());
        }
        
        return resultado;
    }
    
    public boolean actualizarProducto(int idProducto, String nombre) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("El nombre del producto es obligatorio");
            return false;
        }
        
        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto == null) {
            System.err.println("No se encontró el producto con ID: " + idProducto);
            return false;
        }
        
        producto.setNombre(nombre.trim());
        
        boolean resultado = productoDAO.actualizar(producto);
        if (resultado) {
            System.out.println("Producto actualizado exitosamente");
        }
        
        return resultado;
    }
    
    public boolean eliminarProducto(int idProducto) {
        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto == null) {
            System.err.println("No se encontró el producto con ID: " + idProducto);
            return false;
        }
        
        boolean resultado = productoDAO.eliminar(idProducto);
        if (resultado) {
            System.out.println("Producto eliminado exitosamente");
        }
        
        return resultado;
    }
    
    public Producto buscarProductoPorId(int idProducto) {
        return productoDAO.buscarPorId(idProducto);
    }
    
    public List<Producto> listarTodosLosProductos() {
        return productoDAO.listarTodos();
    }
    
    public List<Producto> buscarProductosPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("El nombre no puede estar vacío");
            return null;
        }
        return productoDAO.buscarPorNombre(nombre.trim());
    }
    
    public void mostrarProducto(Producto producto) {
        if (producto != null) {
            System.out.println("=== INFORMACIÓN DEL PRODUCTO ===");
            System.out.println("ID: " + producto.getIdProducto());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Fecha de registro: " + producto.getCreatedAt());
            if (producto.getUpdatedAt() != null) {
                System.out.println("Última actualización: " + producto.getUpdatedAt());
            }
            System.out.println("===============================");
        } else {
            System.out.println("Producto no encontrado");
        }
    }
    
    public void mostrarListaProductos(List<Producto> productos) {
        if (productos == null || productos.isEmpty()) {
            System.out.println("No se encontraron productos");
            return;
        }
        
        System.out.println("=== LISTA DE PRODUCTOS ===");
        System.out.printf("%-5s %-40s %-20s%n", "ID", "Nombre", "Fecha Registro");
        System.out.println("----------------------------------------------------------------");
        
        for (Producto producto : productos) {
            System.out.printf("%-5d %-40s %-20s%n", 
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getCreatedAt() != null ? producto.getCreatedAt().toString().substring(0, 10) : "N/A"
            );
        }
        System.out.println("Total de productos: " + productos.size());
    }
}