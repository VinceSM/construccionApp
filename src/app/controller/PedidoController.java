package app.controller;

import app.dao.PedidoDAO;
import app.dao.DetallePedidoDAO;
import app.dao.ClienteDAO;
import app.dao.EstadoPedidoDAO;
import app.dao.StockDAO;
import app.model.Pedido;
import app.model.DetallePedido;
import app.model.Cliente;
import app.model.EstadoPedido;
import app.model.Producto;
import java.util.List;
import java.sql.Timestamp;
import java.sql.Date;
import java.math.BigDecimal;

public class PedidoController {
    private PedidoDAO pedidoDAO;
    private DetallePedidoDAO detallePedidoDAO;
    private ClienteDAO clienteDAO;
    private EstadoPedidoDAO estadoPedidoDAO;
    private StockDAO stockDAO;
    
    public PedidoController() {
        this.pedidoDAO = new PedidoDAO();
        this.detallePedidoDAO = new DetallePedidoDAO();
        this.clienteDAO = new ClienteDAO();
        this.estadoPedidoDAO = new EstadoPedidoDAO();
        this.stockDAO = new StockDAO();
    }
    
    public boolean crearPedido(int idCliente, int idEstado) {
        // Verificar que el cliente existe
        Cliente cliente = clienteDAO.buscarPorId(idCliente);
        if (cliente == null) {
            System.err.println("No se encontró el cliente con ID: " + idCliente);
            return false;
        }
        
        // Verificar que el estado existe
        EstadoPedido estado = estadoPedidoDAO.buscarPorId(idEstado);
        if (estado == null) {
            System.err.println("No se encontró el estado con ID: " + idEstado);
            return false;
        }
        
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEstado(estado);
        pedido.setFechaPedido(new Timestamp(System.currentTimeMillis()));
        
        boolean resultado = pedidoDAO.insertar(pedido);
        if (resultado) {
            System.out.println("Pedido creado exitosamente con ID: " + pedido.getIdPedido());
        }
        
        return resultado;
    }
    
    public boolean actualizarPedido(int idPedido, int idCliente, int idEstado) {
        Pedido pedido = pedidoDAO.buscarPorId(idPedido);
        if (pedido == null) {
            System.err.println("No se encontró el pedido con ID: " + idPedido);
            return false;
        }
        
        // Verificar que el cliente existe
        Cliente cliente = clienteDAO.buscarPorId(idCliente);
        if (cliente == null) {
            System.err.println("No se encontró el cliente con ID: " + idCliente);
            return false;
        }
        
        // Verificar que el estado existe
        EstadoPedido estado = estadoPedidoDAO.buscarPorId(idEstado);
        if (estado == null) {
            System.err.println("No se encontró el estado con ID: " + idEstado);
            return false;
        }
        
        pedido.setCliente(cliente);
        pedido.setEstado(estado);
        
        boolean resultado = pedidoDAO.actualizar(pedido);
        if (resultado) {
            System.out.println("Pedido actualizado exitosamente");
        }
        
        return resultado;
    }
    
    public boolean cambiarEstadoPedido(int idPedido, int idEstado) {
        Pedido pedido = pedidoDAO.buscarPorId(idPedido);
        if (pedido == null) {
            System.err.println("No se encontró el pedido con ID: " + idPedido);
            return false;
        }
        
        EstadoPedido estado = estadoPedidoDAO.buscarPorId(idEstado);
        if (estado == null) {
            System.err.println("No se encontró el estado con ID: " + idEstado);
            return false;
        }
        
        boolean resultado = pedidoDAO.actualizarEstado(idPedido, idEstado);
        if (resultado) {
            System.out.println("Estado del pedido actualizado a: " + estado.getTipo());
            
            // Si el estado es "Entregado", reducir el stock
            if ("Entregado".equals(estado.getTipo())) {
                procesarEntregaPedido(idPedido);
            }
        }
        
        return resultado;
    }
    
    public boolean agregarDetallePedido(int idPedido, int idProducto, BigDecimal cantidad, BigDecimal precio) {
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
        
        /* Verificar disponibilidad de stock
        if (!stockDAO.buscarPorProducto(idProducto).getUnidad().subtract(cantidad).compareTo(BigDecimal.ZERO) >= 0) {
            System.err.println("Stock insuficiente para el producto");
            return false;
        }*/
        
        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        
        // Buscar el producto
        Producto producto = new Producto();
        producto.setIdProducto(idProducto);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioProducto(precio);
        
        boolean resultado = detallePedidoDAO.insertar(detalle);
        if (resultado) {
            System.out.println("Detalle agregado al pedido exitosamente");
        }
        
        return resultado;
    }
    
    public boolean eliminarPedido(int idPedido) {
        Pedido pedido = pedidoDAO.buscarPorId(idPedido);
        if (pedido == null) {
            System.err.println("No se encontró el pedido con ID: " + idPedido);
            return false;
        }
        
        // Primero eliminar todos los detalles del pedido
        detallePedidoDAO.eliminarPorPedido(idPedido);
        
        // Luego eliminar el pedido
        boolean resultado = pedidoDAO.eliminar(idPedido);
        if (resultado) {
            System.out.println("Pedido eliminado exitosamente");
        }
        
        return resultado;
    }
    
    private void procesarEntregaPedido(int idPedido) {
        List<DetallePedido> detalles = detallePedidoDAO.buscarPorPedido(idPedido);
        
        for (DetallePedido detalle : detalles) {
            stockDAO.reducirStock(detalle.getProducto().getIdProducto(), detalle.getCantidad());
        }
        
        System.out.println("Stock actualizado por entrega del pedido");
    }
    
    public Pedido buscarPedidoPorId(int idPedido) {
        return pedidoDAO.buscarPorId(idPedido);
    }
    
    public List<Pedido> buscarPedidosPorCliente(int idCliente) {
        return pedidoDAO.buscarPorCliente(idCliente);
    }
    
    public List<Pedido> buscarPedidosPorEstado(int idEstado) {
        return pedidoDAO.buscarPorEstado(idEstado);
    }
    
    public List<Pedido> buscarPedidosPorFecha(Date fechaInicio, Date fechaFin) {
        return pedidoDAO.buscarPorFecha(fechaInicio, fechaFin);
    }
    
    public List<Pedido> listarTodosLosPedidos() {
        return pedidoDAO.listarTodos();
    }
    
    public List<Pedido> listarPedidosRecientes(int limite) {
        return pedidoDAO.listarPedidosRecientes(limite);
    }
    
    public BigDecimal calcularTotalPedido(int idPedido) {
        return detallePedidoDAO.calcularTotalPedido(idPedido);
    }
    
    public void mostrarPedido(Pedido pedido) {
        if (pedido != null) {
            System.out.println("=== INFORMACIÓN DEL PEDIDO ===");
            System.out.println("ID Pedido: " + pedido.getIdPedido());
            System.out.println("Cliente: " + pedido.getCliente().getNombreCompleto());
            System.out.println("DNI Cliente: " + pedido.getCliente().getDni());
            System.out.println("Estado: " + pedido.getEstado().getTipo());
            System.out.println("Fecha: " + pedido.getFechaPedido());
            
            // Mostrar detalles del pedido
            List<DetallePedido> detalles = detallePedidoDAO.buscarPorPedido(pedido.getIdPedido());
            if (!detalles.isEmpty()) {
                System.out.println("\n--- DETALLES DEL PEDIDO ---");
                System.out.printf("%-30s %-10s %-12s %-12s%n", "Producto", "Cantidad", "Precio Unit.", "Subtotal");
                System.out.println("----------------------------------------------------------------");
                
                BigDecimal total = BigDecimal.ZERO;
                for (DetallePedido detalle : detalles) {
                    System.out.printf("%-30s %-10s %-12s %-12s%n", 
                        detalle.getProducto().getNombre(),
                        detalle.getCantidad(),
                        detalle.getPrecioProducto(),
                        detalle.getSubtotal()
                    );
                    total = total.add(detalle.getSubtotal());
                }
                System.out.println("----------------------------------------------------------------");
                System.out.println("TOTAL: $" + total);
            }
            System.out.println("=============================");
        } else {
            System.out.println("Pedido no encontrado");
        }
    }
    
    public void mostrarListaPedidos(List<Pedido> pedidos) {
        if (pedidos == null || pedidos.isEmpty()) {
            System.out.println("No se encontraron pedidos");
            return;
        }
        
        System.out.println("=== LISTA DE PEDIDOS ===");
        System.out.printf("%-5s %-25s %-15s %-15s %-12s%n", "ID", "Cliente", "Estado", "Fecha", "Total");
        System.out.println("------------------------------------------------------------------------");
        
        for (Pedido pedido : pedidos) {
            BigDecimal total = calcularTotalPedido(pedido.getIdPedido());
            System.out.printf("%-5d %-25s %-15s %-15s $%-11s%n", 
                pedido.getIdPedido(),
                pedido.getCliente().getNombreCompleto(),
                pedido.getEstado().getTipo(),
                pedido.getFechaPedido().toString().substring(0, 10),
                total
            );
        }
        System.out.println("Total de pedidos: " + pedidos.size());
    }
    
    public void mostrarResumenPedidos() {
        List<EstadoPedido> estados = estadoPedidoDAO.listarTodos();
        
        System.out.println("=== RESUMEN DE PEDIDOS POR ESTADO ===");
        for (EstadoPedido estado : estados) {
            int cantidad = pedidoDAO.contarPedidosPorEstado(estado.getIdEstado());
            System.out.println(estado.getTipo() + ": " + cantidad + " pedidos");
        }
        System.out.println("====================================");
    }
}