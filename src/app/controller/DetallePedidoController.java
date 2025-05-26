package app.controller;

import app.dao.DetallePedidoDAO;
import app.dao.PedidoDAO;
import app.dao.ProductoDAO;
import app.dao.StockDAO;
import app.model.DetallePedido;
import app.model.Pedido;
import app.model.Producto;
import java.util.List;
import java.math.BigDecimal;

public class DetallePedidoController {
    private DetallePedidoDAO detallePedidoDAO;
    private PedidoDAO pedidoDAO;
    private ProductoDAO productoDAO;
    private StockDAO stockDAO;
    
    public DetallePedidoController() {
        this.detallePedidoDAO = new DetallePedidoDAO();
        this.pedidoDAO = new PedidoDAO();
        this.productoDAO = new ProductoDAO();
        this.stockDAO = new StockDAO();
    }
    
    public boolean agregarDetalle(int idPedido, int idProducto, BigDecimal cantidad, BigDecimal precio) {
        // Validaciones
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("La cantidad debe ser mayor a cero");
            return false;
        }
        
        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("El precio debe ser mayor o igual a cero");
            return false;
        }
        
        // Verificar que el pedido existe
        Pedido pedido = pedidoDAO.buscarPorId(idPedido);
        if (pedido == null) {
            System.err.println("No se encontró el pedido con ID: " + idPedido);
            return false;
        }
        
        // Verificar que el producto existe
        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto == null) {
            System.err.println("No se encontró el producto con ID: " + idProducto);
            return false;
        }
        
        // Verificar disponibilidad de stock
        if (!verificarDisponibilidadStock(idProducto, cantidad)) {
            System.err.println("Stock insuficiente para el producto: " + producto.getNombre());
            return false;
        }
        
        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioProducto(precio);
        
        boolean resultado = detallePedidoDAO.insertar(detalle);
        if (resultado) {
            System.out.println("Detalle agregado exitosamente con ID: " + detalle.getIdDetalle());
            System.out.println("Subtotal: $" + detalle.getSubtotal());
        }
        
        return resultado;
    }
    
    public boolean actualizarDetalle(int idDetalle, int idPedido, int idProducto, BigDecimal cantidad, BigDecimal precio) {
        // Validaciones
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("La cantidad debe ser mayor a cero");
            return false;
        }
        
        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("El precio debe ser mayor o igual a cero");
            return false;
        }
        
        DetallePedido detalle = detallePedidoDAO.buscarPorId(idDetalle);
        if (detalle == null) {
            System.err.println("No se encontró el detalle con ID: " + idDetalle);
            return false;
        }
        
        // Verificar que el pedido existe
        Pedido pedido = pedidoDAO.buscarPorId(idPedido);
        if (pedido == null) {
            System.err.println("No se encontró el pedido con ID: " + idPedido);
            return false;
        }
        
        // Verificar que el producto existe
        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto == null) {
            System.err.println("No se encontró el producto con ID: " + idProducto);
            return false;
        }
        
        // Si cambió el producto o la cantidad, verificar stock
        if (detalle.getProducto().getIdProducto() != idProducto || 
            !detalle.getCantidad().equals(cantidad)) {
            if (!verificarDisponibilidadStock(idProducto, cantidad)) {
                System.err.println("Stock insuficiente para el producto: " + producto.getNombre());
                return false;
            }
        }
        
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioProducto(precio);
        
        boolean resultado = detallePedidoDAO.actualizar(detalle);
        if (resultado) {
            System.out.println("Detalle actualizado exitosamente");
        }
        
        return resultado;
    }
    
    public boolean actualizarCantidad(int idDetalle, BigDecimal nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("La cantidad debe ser mayor a cero");
            return false;
        }
        
        DetallePedido detalle = detallePedidoDAO.buscarPorId(idDetalle);
        if (detalle == null) {
            System.err.println("No se encontró el detalle con ID: " + idDetalle);
            return false;
        }
        
        // Verificar disponibilidad de stock para la nueva cantidad
        if (!verificarDisponibilidadStock(detalle.getProducto().getIdProducto(), nuevaCantidad)) {
            System.err.println("Stock insuficiente para la cantidad solicitada");
            return false;
        }
        
        boolean resultado = detallePedidoDAO.actualizarCantidad(idDetalle, nuevaCantidad);
        if (resultado) {
            System.out.println("Cantidad actualizada exitosamente");
        }
        
        return resultado;
    }
    
    public boolean eliminarDetalle(int idDetalle) {
        DetallePedido detalle = detallePedidoDAO.buscarPorId(idDetalle);
        if (detalle == null) {
            System.err.println("No se encontró el detalle con ID: " + idDetalle);
            return false;
        }
        
        boolean resultado = detallePedidoDAO.eliminar(idDetalle);
        if (resultado) {
            System.out.println("Detalle eliminado exitosamente");
        }
        
        return resultado;
    }
    
    public boolean eliminarDetallesPorPedido(int idPedido) {
        Pedido pedido = pedidoDAO.buscarPorId(idPedido);
        if (pedido == null) {
            System.err.println("No se encontró el pedido con ID: " + idPedido);
            return false;
        }
        
        boolean resultado = detallePedidoDAO.eliminarPorPedido(idPedido);
        if (resultado) {
            System.out.println("Todos los detalles del pedido eliminados exitosamente");
        }
        
        return resultado;
    }
    
    public DetallePedido buscarDetallePorId(int idDetalle) {
        return detallePedidoDAO.buscarPorId(idDetalle);
    }
    
    public List<DetallePedido> buscarDetallesPorPedido(int idPedido) {
        return detallePedidoDAO.buscarPorPedido(idPedido);
    }
    
    public List<DetallePedido> buscarDetallesPorProducto(int idProducto) {
        return detallePedidoDAO.buscarPorProducto(idProducto);
    }
    
    public List<DetallePedido> listarTodosLosDetalles() {
        return detallePedidoDAO.listarTodos();
    }
    
    public BigDecimal calcularTotalPedido(int idPedido) {
        return detallePedidoDAO.calcularTotalPedido(idPedido);
    }
    
    public BigDecimal calcularCantidadVendidaProducto(int idProducto) {
        return detallePedidoDAO.calcularCantidadVendidaProducto(idProducto);
    }
    
    private boolean verificarDisponibilidadStock(int idProducto, BigDecimal cantidadRequerida) {
        try {
            var stock = stockDAO.buscarPorProducto(idProducto);
            if (stock == null) {
                System.err.println("No se encontró stock para el producto");
                return false;
            }
            return stock.getUnidad().compareTo(cantidadRequerida) >= 0;
        } catch (Exception e) {
            System.err.println("Error al verificar stock: " + e.getMessage());
            return false;
        }
    }
    
    public void mostrarDetalle(DetallePedido detalle) {
        if (detalle != null) {
            System.out.println("=== INFORMACIÓN DEL DETALLE ===");
            System.out.println("ID Detalle: " + detalle.getIdDetalle());
            System.out.println("ID Pedido: " + detalle.getPedido().getIdPedido());
            System.out.println("Cliente: " + detalle.getPedido().getCliente().getNombreCompleto());
            System.out.println("Producto: " + detalle.getProducto().getNombre());
            System.out.println("Cantidad: " + detalle.getCantidad());
            System.out.println("Precio Unitario: $" + detalle.getPrecioProducto());
            System.out.println("Subtotal: $" + detalle.getSubtotal());
            System.out.println("Fecha: " + detalle.getCreatedAt());
            if (detalle.getUpdatedAt() != null) {
                System.out.println("Última actualización: " + detalle.getUpdatedAt());
            }
            System.out.println("==============================");
        } else {
            System.out.println("Detalle no encontrado");
        }
    }
    
    public void mostrarListaDetalles(List<DetallePedido> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            System.out.println("No se encontraron detalles");
            return;
        }
        
        System.out.println("=== LISTA DE DETALLES ===");
        System.out.printf("%-5s %-8s %-25s %-30s %-10s %-12s %-12s%n", 
            "ID", "Pedido", "Cliente", "Producto", "Cantidad", "Precio", "Subtotal");
        System.out.println("-------------------------------------------------------------------------------------");
        
        BigDecimal totalGeneral = BigDecimal.ZERO;
        for (DetallePedido detalle : detalles) {
            System.out.printf("%-5d %-8d %-25s %-30s %-10s $%-11s $%-11s%n", 
                detalle.getIdDetalle(),
                detalle.getPedido().getIdPedido(),
                detalle.getPedido().getCliente().getNombreCompleto(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioProducto(),
                detalle.getSubtotal()
            );
            totalGeneral = totalGeneral.add(detalle.getSubtotal());
        }
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Total de detalles: " + detalles.size());
        System.out.println("Total general: $" + totalGeneral);
    }
    
    public void mostrarDetallesPedido(int idPedido) {
        List<DetallePedido> detalles = buscarDetallesPorPedido(idPedido);
        
        if (detalles == null || detalles.isEmpty()) {
            System.out.println("No se encontraron detalles para este pedido");
            return;
        }
        
        Pedido pedido = pedidoDAO.buscarPorId(idPedido);
        System.out.println("=== DETALLES DEL PEDIDO #" + idPedido + " ===");
        if (pedido != null) {
            System.out.println("Cliente: " + pedido.getCliente().getNombreCompleto());
            System.out.println("Estado: " + pedido.getEstado().getTipo());
            System.out.println("Fecha: " + pedido.getFechaPedido());
        }
        System.out.println();
        
        System.out.printf("%-30s %-10s %-12s %-12s%n", "Producto", "Cantidad", "Precio Unit.", "Subtotal");
        System.out.println("----------------------------------------------------------------");
        
        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedido detalle : detalles) {
            System.out.printf("%-30s %-10s $%-11s $%-11s%n", 
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioProducto(),
                detalle.getSubtotal()
            );
            total = total.add(detalle.getSubtotal());
        }
        System.out.println("----------------------------------------------------------------");
        System.out.println("TOTAL DEL PEDIDO: $" + total);
        System.out.println("===============================================");
    }
    
    public void mostrarHistorialProducto(int idProducto) {
        List<DetallePedido> detalles = buscarDetallesPorProducto(idProducto);
        
        if (detalles == null || detalles.isEmpty()) {
            System.out.println("No se encontró historial de ventas para este producto");
            return;
        }
        
        Producto producto = productoDAO.buscarPorId(idProducto);
        System.out.println("=== HISTORIAL DE VENTAS - " + (producto != null ? producto.getNombre() : "Producto #" + idProducto) + " ===");
        
        System.out.printf("%-8s %-25s %-10s %-12s %-15s%n", "Pedido", "Cliente", "Cantidad", "Precio", "Fecha");
        System.out.println("------------------------------------------------------------------------");
        
        BigDecimal cantidadTotal = BigDecimal.ZERO;
        BigDecimal ventaTotal = BigDecimal.ZERO;
        
        for (DetallePedido detalle : detalles) {
            System.out.printf("%-8d %-25s %-10s $%-11s %-15s%n", 
                detalle.getPedido().getIdPedido(),
                detalle.getPedido().getCliente().getNombreCompleto(),
                detalle.getCantidad(),
                detalle.getPrecioProducto(),
                detalle.getCreatedAt().toString().substring(0, 10)
            );
            cantidadTotal = cantidadTotal.add(detalle.getCantidad());
            ventaTotal = ventaTotal.add(detalle.getSubtotal());
        }
        
        System.out.println("------------------------------------------------------------------------");
        System.out.println("Total vendido: " + cantidadTotal + " unidades");
        System.out.println("Ingresos totales: $" + ventaTotal);
        System.out.println("Número de ventas: " + detalles.size());
        System.out.println("=======================================================================");
    }
    
    public void mostrarResumenVentas() {
        List<DetallePedido> todosLosDetalles = listarTodosLosDetalles();
        
        if (todosLosDetalles == null || todosLosDetalles.isEmpty()) {
            System.out.println("No hay datos de ventas disponibles");
            return;
        }
        
        System.out.println("=== RESUMEN GENERAL DE VENTAS ===");
        
        BigDecimal ventasTotales = BigDecimal.ZERO;
        int totalTransacciones = todosLosDetalles.size();
        
        for (DetallePedido detalle : todosLosDetalles) {
            ventasTotales = ventasTotales.add(detalle.getSubtotal());
        }
        
        BigDecimal promedioVenta = totalTransacciones > 0 ? 
            ventasTotales.divide(BigDecimal.valueOf(totalTransacciones), 2, BigDecimal.ROUND_HALF_UP) : 
            BigDecimal.ZERO;
        
        System.out.println("Total de transacciones: " + totalTransacciones);
        System.out.println("Ventas totales: $" + ventasTotales);
        System.out.println("Promedio por transacción: $" + promedioVenta);
        System.out.println("================================");
    }
    
    public boolean validarStockParaPedido(int idPedido) {
        List<DetallePedido> detalles = buscarDetallesPorPedido(idPedido);
        
        if (detalles == null || detalles.isEmpty()) {
            System.out.println("El pedido no tiene detalles");
            return true;
        }
        
        boolean stockSuficiente = true;
        System.out.println("=== VALIDACIÓN DE STOCK PARA PEDIDO #" + idPedido + " ===");
        
        for (DetallePedido detalle : detalles) {
            boolean disponible = verificarDisponibilidadStock(
                detalle.getProducto().getIdProducto(), 
                detalle.getCantidad()
            );
            
            String estado = disponible ? "✅ OK" : "❌ INSUFICIENTE";
            System.out.printf("%-30s: %s (Requerido: %s)%n", 
                detalle.getProducto().getNombre(),
                estado,
                detalle.getCantidad()
            );
            
            if (!disponible) {
                stockSuficiente = false;
            }
        }
        
        System.out.println("================================================");
        System.out.println("Resultado: " + (stockSuficiente ? "✅ Stock suficiente" : "❌ Stock insuficiente"));
        
        return stockSuficiente;
    }
}