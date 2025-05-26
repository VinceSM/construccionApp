package app.controller;

import app.dao.EstadoPedidoDAO;
import app.model.EstadoPedido;
import java.util.List;

public class EstadoPedidoController {
    private EstadoPedidoDAO estadoPedidoDAO;
    
    public EstadoPedidoController() {
        this.estadoPedidoDAO = new EstadoPedidoDAO();
    }
    
    public boolean registrarEstado(String tipo) {
        // Validaciones
        if (tipo == null || tipo.trim().isEmpty()) {
            System.err.println("El tipo de estado es obligatorio");
            return false;
        }
        
        // Verificar si ya existe un estado con ese tipo
        if (estadoPedidoDAO.buscarPorTipo(tipo.trim()) != null) {
            System.err.println("Ya existe un estado con ese tipo");
            return false;
        }
        
        EstadoPedido estado = new EstadoPedido();
        estado.setTipo(tipo.trim());
        
        boolean resultado = estadoPedidoDAO.insertar(estado);
        if (resultado) {
            System.out.println("Estado registrado exitosamente con ID: " + estado.getIdEstado());
        }
        
        return resultado;
    }
    
    public boolean actualizarEstado(int idEstado, String tipo) {
        // Validaciones
        if (tipo == null || tipo.trim().isEmpty()) {
            System.err.println("El tipo de estado es obligatorio");
            return false;
        }
        
        EstadoPedido estado = estadoPedidoDAO.buscarPorId(idEstado);
        if (estado == null) {
            System.err.println("No se encontró el estado con ID: " + idEstado);
            return false;
        }
        
        // Verificar si ya existe otro estado con ese tipo
        EstadoPedido estadoExistente = estadoPedidoDAO.buscarPorTipo(tipo.trim());
        if (estadoExistente != null && estadoExistente.getIdEstado() != idEstado) {
            System.err.println("Ya existe otro estado con ese tipo");
            return false;
        }
        
        estado.setTipo(tipo.trim());
        
        boolean resultado = estadoPedidoDAO.actualizar(estado);
        if (resultado) {
            System.out.println("Estado actualizado exitosamente");
        }
        
        return resultado;
    }
    
    public boolean eliminarEstado(int idEstado) {
        EstadoPedido estado = estadoPedidoDAO.buscarPorId(idEstado);
        if (estado == null) {
            System.err.println("No se encontró el estado con ID: " + idEstado);
            return false;
        }
        
        boolean resultado = estadoPedidoDAO.eliminar(idEstado);
        if (resultado) {
            System.out.println("Estado eliminado exitosamente");
        }
        
        return resultado;
    }
    
    public EstadoPedido buscarEstadoPorId(int idEstado) {
        return estadoPedidoDAO.buscarPorId(idEstado);
    }
    
    public EstadoPedido buscarEstadoPorTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            System.err.println("El tipo no puede estar vacío");
            return null;
        }
        return estadoPedidoDAO.buscarPorTipo(tipo.trim());
    }
    
    public List<EstadoPedido> listarTodosLosEstados() {
        return estadoPedidoDAO.listarTodos();
    }
    
    public List<EstadoPedido> buscarEstadosPorTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            System.err.println("El tipo no puede estar vacío");
            return null;
        }
        return estadoPedidoDAO.buscarPorTipoLike(tipo.trim());
    }
    
    public void mostrarEstado(EstadoPedido estado) {
        if (estado != null) {
            System.out.println("=== INFORMACIÓN DEL ESTADO ===");
            System.out.println("ID: " + estado.getIdEstado());
            System.out.println("Tipo: " + estado.getTipo());
            System.out.println("Fecha de registro: " + estado.getCreatedAt());
            if (estado.getUpdatedAt() != null) {
                System.out.println("Última actualización: " + estado.getUpdatedAt());
            }
            System.out.println("=============================");
        } else {
            System.out.println("Estado no encontrado");
        }
    }
    
    public void mostrarListaEstados(List<EstadoPedido> estados) {
        if (estados == null || estados.isEmpty()) {
            System.out.println("No se encontraron estados");
            return;
        }
        
        System.out.println("=== LISTA DE ESTADOS ===");
        System.out.printf("%-5s %-20s %-20s%n", "ID", "Tipo", "Fecha Registro");
        System.out.println("------------------------------------------------");
        
        for (EstadoPedido estado : estados) {
            System.out.printf("%-5d %-20s %-20s%n", 
                estado.getIdEstado(),
                estado.getTipo(),
                estado.getCreatedAt() != null ? estado.getCreatedAt().toString().substring(0, 10) : "N/A"
            );
        }
        System.out.println("Total de estados: " + estados.size());
    }
}