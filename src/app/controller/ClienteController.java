package app.controller;

import app.dao.ClienteDAO;
import app.model.Cliente;
import java.util.List;

public class ClienteController {
    private ClienteDAO clienteDAO;
    
    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }
    
    public boolean registrarCliente(String nombreCompleto, String dni, String telefono) {
        // Validaciones
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            System.err.println("El nombre completo es obligatorio");
            return false;
        }
        
        if (dni == null || dni.trim().isEmpty()) {
            System.err.println("El DNI es obligatorio");
            return false;
        }
        
        // Verificar si el DNI ya existe
        if (clienteDAO.buscarPorDni(dni) != null) {
            System.err.println("Ya existe un cliente con ese DNI");
            return false;
        }
        
        Cliente cliente = new Cliente();
        cliente.setNombreCompleto(nombreCompleto.trim());
        cliente.setDni(dni.trim());
        cliente.setTelefono(telefono != null ? telefono.trim() : null);
        
        boolean resultado = clienteDAO.insertar(cliente);
        if (resultado) {
            System.out.println("Cliente registrado exitosamente con ID: " + cliente.getIdCliente());
        }
        
        return resultado;
    }
    
    public boolean actualizarCliente(int idCliente, String nombreCompleto, String dni, String telefono) {
        // Validaciones
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            System.err.println("El nombre completo es obligatorio");
            return false;
        }
        
        if (dni == null || dni.trim().isEmpty()) {
            System.err.println("El DNI es obligatorio");
            return false;
        }
        
        // Verificar que el cliente existe
        Cliente clienteExistente = clienteDAO.buscarPorId(idCliente);
        if (clienteExistente == null) {
            System.err.println("No se encontró el cliente con ID: " + idCliente);
            return false;
        }
        
        // Verificar si el DNI ya existe en otro cliente
        Cliente clienteConDni = clienteDAO.buscarPorDni(dni);
        if (clienteConDni != null && clienteConDni.getIdCliente() != idCliente) {
            System.err.println("Ya existe otro cliente con ese DNI");
            return false;
        }
        
        clienteExistente.setNombreCompleto(nombreCompleto.trim());
        clienteExistente.setDni(dni.trim());
        clienteExistente.setTelefono(telefono != null ? telefono.trim() : null);
        
        boolean resultado = clienteDAO.actualizar(clienteExistente);
        if (resultado) {
            System.out.println("Cliente actualizado exitosamente");
        }
        
        return resultado;
    }
    
    public boolean eliminarCliente(int idCliente) {
        Cliente cliente = clienteDAO.buscarPorId(idCliente);
        if (cliente == null) {
            System.err.println("No se encontró el cliente con ID: " + idCliente);
            return false;
        }
        
        boolean resultado = clienteDAO.eliminar(idCliente);
        if (resultado) {
            System.out.println("Cliente eliminado exitosamente");
        }
        
        return resultado;
    }
    
    public Cliente buscarClientePorId(int idCliente) {
        return clienteDAO.buscarPorId(idCliente);
    }
    
    public Cliente buscarClientePorDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            System.err.println("El DNI no puede estar vacío");
            return null;
        }
        return clienteDAO.buscarPorDni(dni.trim());
    }
    
    public List<Cliente> listarTodosLosClientes() {
        return clienteDAO.listarTodos();
    }
    
    public List<Cliente> buscarClientesPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("El nombre no puede estar vacío");
            return null;
        }
        return clienteDAO.buscarPorNombre(nombre.trim());
    }
    
    public void mostrarCliente(Cliente cliente) {
        if (cliente != null) {
            System.out.println("=== INFORMACIÓN DEL CLIENTE ===");
            System.out.println("ID: " + cliente.getIdCliente());
            System.out.println("Nombre: " + cliente.getNombreCompleto());
            System.out.println("DNI: " + cliente.getDni());
            System.out.println("Teléfono: " + (cliente.getTelefono() != null ? cliente.getTelefono() : "No especificado"));
            System.out.println("Fecha de registro: " + cliente.getCreatedAt());
            System.out.println("===============================");
        } else {
            System.out.println("Cliente no encontrado");
        }
    }
    
    public void mostrarListaClientes(List<Cliente> clientes) {
        if (clientes == null || clientes.isEmpty()) {
            System.out.println("No se encontraron clientes");
            return;
        }
        
        System.out.println("=== LISTA DE CLIENTES ===");
        System.out.printf("%-5s %-30s %-15s %-15s%n", "ID", "Nombre", "DNI", "Teléfono");
        System.out.println("----------------------------------------------------------------");
        
        for (Cliente cliente : clientes) {
            System.out.printf("%-5d %-30s %-15s %-15s%n", 
                cliente.getIdCliente(),
                cliente.getNombreCompleto(),
                cliente.getDni(),
                cliente.getTelefono() != null ? cliente.getTelefono() : "N/A"
            );
        }
        System.out.println("Total de clientes: " + clientes.size());
    }
}