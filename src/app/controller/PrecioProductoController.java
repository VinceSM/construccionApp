package app.controller;

import app.dao.PrecioProductoDAO;
import app.dao.ProductoDAO;
import app.model.PrecioProducto;
import app.model.Producto;
import java.util.List;
import java.math.BigDecimal;

public class PrecioProductoController {
    private PrecioProductoDAO precioProductoDAO;
    private ProductoDAO productoDAO;
    
    public PrecioProductoController() {
        this.precioProductoDAO = new PrecioProductoDAO();
        this.productoDAO = new ProductoDAO();
    }
    
    public boolean registrarPrecio(int idProducto, String moneda, BigDecimal monto) {
        // Validaciones
        if (monto == null || monto.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("El monto debe ser mayor o igual a cero");
            return false;
        }
        
        if (moneda == null || moneda.trim().isEmpty()) {
            System.err.println("La moneda es obligatoria");
            return false;
        }
        
        // Verificar que el producto existe
        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto == null) {
            System.err.println("No se encontró el producto con ID: " + idProducto);
            return false;
        }
        
        PrecioProducto precio = new PrecioProducto();
        precio.setProducto(producto);
        precio.setMoneda(moneda.trim().toUpperCase());
        precio.setMonto(monto);
        
        boolean resultado = precioProductoDAO.insertar(precio);
        if (resultado) {
            System.out.println("Precio registrado exitosamente con ID: " + precio.getIdPrecio());
        }
        
        return resultado;
    }
    
    public boolean actualizarPrecio(int idPrecio, int idProducto, String moneda, BigDecimal monto) {
        // Validaciones
        if (monto == null || monto.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("El monto debe ser mayor o igual a cero");
            return false;
        }
        
        if (moneda == null || moneda.trim().isEmpty()) {
            System.err.println("La moneda es obligatoria");
            return false;
        }
        
        PrecioProducto precio = precioProductoDAO.buscarPorId(idPrecio);
        if (precio == null) {
            System.err.println("No se encontró el precio con ID: " + idPrecio);
            return false;
        }
        
        // Verificar que el producto existe
        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto == null) {
            System.err.println("No se encontró el producto con ID: " + idProducto);
            return false;
        }
        
        precio.setProducto(producto);
        precio.setMoneda(moneda.trim().toUpperCase());
        precio.setMonto(monto);
        
        boolean resultado = precioProductoDAO.actualizar(precio);
        if (resultado) {
            System.out.println("Precio actualizado exitosamente");
        }
        
        return resultado;
    }
    
    public boolean actualizarPrecioProducto(int idProducto, String moneda, BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("El precio debe ser mayor o igual a cero");
            return false;
        }
        
        if (moneda == null || moneda.trim().isEmpty()) {
            System.err.println("La moneda es obligatoria");
            return false;
        }
        
        // Verificar que el producto existe
        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto == null) {
            System.err.println("No se encontró el producto con ID: " + idProducto);
            return false;
        }
        
        boolean resultado = precioProductoDAO.actualizarPrecio(idProducto, moneda.trim().toUpperCase(), nuevoPrecio);
        if (resultado) {
            System.out.println("Precio del producto actualizado exitosamente");
        }
        
        return resultado;
    }
    
    public boolean eliminarPrecio(int idPrecio) {
        PrecioProducto precio = precioProductoDAO.buscarPorId(idPrecio);
        if (precio == null) {
            System.err.println("No se encontró el precio con ID: " + idPrecio);
            return false;
        }
        
        boolean resultado = precioProductoDAO.eliminar(idPrecio);
        if (resultado) {
            System.out.println("Precio eliminado exitosamente");
        }
        
        return resultado;
    }
    
    public PrecioProducto buscarPrecioPorId(int idPrecio) {
        return precioProductoDAO.buscarPorId(idPrecio);
    }
    
    public PrecioProducto buscarPrecioActual(int idProducto, String moneda) {
        if (moneda == null || moneda.trim().isEmpty()) {
            System.err.println("La moneda no puede estar vacía");
            return null;
        }
        return precioProductoDAO.buscarPrecioActualPorProducto(idProducto, moneda.trim().toUpperCase());
    }
    
    public List<PrecioProducto> buscarPreciosPorProducto(int idProducto) {
        return precioProductoDAO.buscarPorProducto(idProducto);
    }
    
    public List<PrecioProducto> listarTodosLosPrecios() {
        return precioProductoDAO.listarTodos();
    }
    
    public List<PrecioProducto> buscarPreciosPorMoneda(String moneda) {
        if (moneda == null || moneda.trim().isEmpty()) {
            System.err.println("La moneda no puede estar vacía");
            return null;
        }
        return precioProductoDAO.buscarPorMoneda(moneda.trim().toUpperCase());
    }
    
    public List<PrecioProducto> buscarPreciosPorRango(BigDecimal precioMin, BigDecimal precioMax, String moneda) {
        if (precioMin == null || precioMax == null) {
            System.err.println("Los precios mínimo y máximo son obligatorios");
            return null;
        }
        
        if (precioMin.compareTo(precioMax) > 0) {
            System.err.println("El precio mínimo no puede ser mayor al precio máximo");
            return null;
        }
        
        if (moneda == null || moneda.trim().isEmpty()) {
            System.err.println("La moneda no puede estar vacía");
            return null;
        }
        
        return precioProductoDAO.buscarPorRangoPrecio(precioMin, precioMax, moneda.trim().toUpperCase());
    }
    
    public void mostrarPrecio(PrecioProducto precio) {
        if (precio != null) {
            System.out.println("=== INFORMACIÓN DEL PRECIO ===");
            System.out.println("ID Precio: " + precio.getIdPrecio());
            System.out.println("Producto: " + precio.getProducto().getNombre());
            System.out.println("Moneda: " + precio.getMoneda());
            System.out.println("Monto: " + precio.getMonto());
            System.out.println("Fecha de registro: " + precio.getCreatedAt());
            if (precio.getUpdatedAt() != null) {
                System.out.println("Última actualización: " + precio.getUpdatedAt());
            }
            System.out.println("=============================");
        } else {
            System.out.println("Precio no encontrado");
        }
    }
    
    public void mostrarListaPrecios(List<PrecioProducto> precios) {
        if (precios == null || precios.isEmpty()) {
            System.out.println("No se encontraron precios");
            return;
        }
        
        System.out.println("=== LISTA DE PRECIOS ===");
        System.out.printf("%-5s %-30s %-8s %-15s %-15s%n", "ID", "Producto", "Moneda", "Precio", "Fecha");
        System.out.println("------------------------------------------------------------------------");
        
        for (PrecioProducto precio : precios) {
            System.out.printf("%-5d %-30s %-8s %-15s %-15s%n", 
                precio.getIdPrecio(),
                precio.getProducto().getNombre(),
                precio.getMoneda(),
                precio.getMonto(),
                precio.getCreatedAt() != null ? precio.getCreatedAt().toString().substring(0, 10) : "N/A"
            );
        }
        System.out.println("Total de precios: " + precios.size());
    }
    
    public void mostrarPreciosProducto(int idProducto) {
        List<PrecioProducto> precios = buscarPreciosPorProducto(idProducto);
        
        if (precios == null || precios.isEmpty()) {
            System.out.println("No se encontraron precios para este producto");
            return;
        }
        
        System.out.println("=== HISTORIAL DE PRECIOS DEL PRODUCTO ===");
        System.out.printf("%-8s %-15s %-15s%n", "Moneda", "Precio", "Fecha");
        System.out.println("----------------------------------------");
        
        for (PrecioProducto precio : precios) {
            System.out.printf("%-8s %-15s %-15s%n", 
                precio.getMoneda(),
                precio.getMonto(),
                precio.getCreatedAt() != null ? precio.getCreatedAt().toString().substring(0, 10) : "N/A"
            );
        }
    }
}