package app.controller;

import app.dao.StockDAO;
import app.dao.ProductoDAO;
import app.model.Stock;
import app.model.Producto;
import java.util.List;
import java.math.BigDecimal;

public class StockController {
    private StockDAO stockDAO;
    private ProductoDAO productoDAO;
    
    public StockController() {
        this.stockDAO = new StockDAO();
        this.productoDAO = new ProductoDAO();
    }
    
    public boolean registrarStock(int idProducto, BigDecimal cantidad, String medida) {
        // Validaciones
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("La cantidad debe ser mayor o igual a cero");
            return false;
        }
        
        if (medida == null || medida.trim().isEmpty()) {
            System.err.println("La medida es obligatoria");
            return false;
        }
        
        // Verificar que el producto existe
        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto == null) {
            System.err.println("No se encontró el producto con ID: " + idProducto);
            return false;
        }
        
        // Verificar si ya existe stock para este producto
        Stock stockExistente = stockDAO.buscarPorProducto(idProducto);
        if (stockExistente != null) {
            System.err.println("Ya existe stock registrado para este producto. Use actualizar stock.");
            return false;
        }
        
        Stock stock = new Stock();
        stock.setProducto(producto);
        stock.setUnidad(cantidad);
        stock.setMedida(medida.trim());
        
        boolean resultado = stockDAO.insertar(stock);
        if (resultado) {
            System.out.println("Stock registrado exitosamente con ID: " + stock.getIdStock());
        }
        
        return resultado;
    }
    
    public boolean actualizarStock(int idStock, BigDecimal cantidad, String medida) {
        // Validaciones
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("La cantidad debe ser mayor o igual a cero");
            return false;
        }
        
        if (medida == null || medida.trim().isEmpty()) {
            System.err.println("La medida es obligatoria");
            return false;
        }
        
        Stock stock = stockDAO.buscarPorId(idStock);
        if (stock == null) {
            System.err.println("No se encontró el stock con ID: " + idStock);
            return false;
        }
        
        stock.setUnidad(cantidad);
        stock.setMedida(medida.trim());
        
        boolean resultado = stockDAO.actualizar(stock);
        if (resultado) {
            System.out.println("Stock actualizado exitosamente");
        }
        
        return resultado;
    }
    
    public boolean actualizarCantidadStock(int idProducto, BigDecimal nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("La cantidad debe ser mayor o igual a cero");
            return false;
        }
        
        Stock stock = stockDAO.buscarPorProducto(idProducto);
        if (stock == null) {
            System.err.println("No se encontró stock para el producto con ID: " + idProducto);
            return false;
        }
        
        boolean resultado = stockDAO.actualizarCantidad(idProducto, nuevaCantidad);
        if (resultado) {
            System.out.println("Cantidad de stock actualizada exitosamente");
        }
        
        return resultado;
    }
    
    public boolean reducirStock(int idProducto, BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("La cantidad a reducir debe ser mayor a cero");
            return false;
        }
        
        Stock stock = stockDAO.buscarPorProducto(idProducto);
        if (stock == null) {
            System.err.println("No se encontró stock para el producto con ID: " + idProducto);
            return false;
        }
        
        if (stock.getUnidad().compareTo(cantidad) < 0) {
            System.err.println("Stock insuficiente. Disponible: " + stock.getUnidad() + " " + stock.getMedida());
            return false;
        }
        
        boolean resultado = stockDAO.reducirStock(idProducto, cantidad);
        if (resultado) {
            System.out.println("Stock reducido exitosamente");
        }
        
        return resultado;
    }
    
    public boolean aumentarStock(int idProducto, BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("La cantidad a aumentar debe ser mayor a cero");
            return false;
        }
        
        Stock stock = stockDAO.buscarPorProducto(idProducto);
        if (stock == null) {
            System.err.println("No se encontró stock para el producto con ID: " + idProducto);
            return false;
        }
        
        boolean resultado = stockDAO.aumentarStock(idProducto, cantidad);
        if (resultado) {
            System.out.println("Stock aumentado exitosamente");
        }
        
        return resultado;
    }
    
    public boolean eliminarStock(int idStock) {
        Stock stock = stockDAO.buscarPorId(idStock);
        if (stock == null) {
            System.err.println("No se encontró el stock con ID: " + idStock);
            return false;
        }
        
        boolean resultado = stockDAO.eliminar(idStock);
        if (resultado) {
            System.out.println("Stock eliminado exitosamente");
        }
        
        return resultado;
    }
    
    public Stock buscarStockPorId(int idStock) {
        return stockDAO.buscarPorId(idStock);
    }
    
    public Stock buscarStockPorProducto(int idProducto) {
        return stockDAO.buscarPorProducto(idProducto);
    }
    
    public List<Stock> listarTodoElStock() {
        return stockDAO.listarTodos();
    }
    
    public List<Stock> listarStockBajo(BigDecimal limite) {
        if (limite == null || limite.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("El límite debe ser mayor o igual a cero");
            return null;
        }
        return stockDAO.listarStockBajo(limite);
    }
    
    public boolean verificarDisponibilidad(int idProducto, BigDecimal cantidadRequerida) {
        Stock stock = stockDAO.buscarPorProducto(idProducto);
        if (stock == null) {
            return false;
        }
        return stock.getUnidad().compareTo(cantidadRequerida) >= 0;
    }
    
    public void mostrarStock(Stock stock) {
        if (stock != null) {
            System.out.println("=== INFORMACIÓN DEL STOCK ===");
            System.out.println("ID Stock: " + stock.getIdStock());
            System.out.println("Producto: " + stock.getProducto().getNombre());
            System.out.println("Cantidad: " + stock.getUnidad() + " " + stock.getMedida());
            System.out.println("Fecha de registro: " + stock.getCreatedAt());
            if (stock.getUpdatedAt() != null) {
                System.out.println("Última actualización: " + stock.getUpdatedAt());
            }
            System.out.println("=============================");
        } else {
            System.out.println("Stock no encontrado");
        }
    }
    
    public void mostrarListaStock(List<Stock> stocks) {
        if (stocks == null || stocks.isEmpty()) {
            System.out.println("No se encontró stock");
            return;
        }
        
        System.out.println("=== LISTA DE STOCK ===");
        System.out.printf("%-5s %-30s %-15s %-10s%n", "ID", "Producto", "Cantidad", "Medida");
        System.out.println("----------------------------------------------------------------");
        
        for (Stock stock : stocks) {
            System.out.printf("%-5d %-30s %-15s %-10s%n", 
                stock.getIdStock(),
                stock.getProducto().getNombre(),
                stock.getUnidad(),
                stock.getMedida()
            );
        }
        System.out.println("Total de items en stock: " + stocks.size());
    }
    
    public void mostrarAlertasStockBajo(BigDecimal limite) {
        List<Stock> stockBajo = listarStockBajo(limite);
        
        if (stockBajo == null || stockBajo.isEmpty()) {
            System.out.println("No hay productos con stock bajo");
            return;
        }
        
        System.out.println("⚠️  ALERTA: PRODUCTOS CON STOCK BAJO ⚠️");
        System.out.printf("%-30s %-15s %-10s%n", "Producto", "Cantidad", "Medida");
        System.out.println("--------------------------------------------------------");
        
        for (Stock stock : stockBajo) {
            System.out.printf("%-30s %-15s %-10s%n", 
                stock.getProducto().getNombre(),
                stock.getUnidad(),
                stock.getMedida()
            );
        }
        System.out.println("Total de productos con stock bajo: " + stockBajo.size());
    }
}