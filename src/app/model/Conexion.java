// Conexion.java mejorada
package app.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/estilodb";
    private static final String USER = "root";
    private static final String PASS = "";
    private static final int MAX_CONNECTIONS = 5;
    private static List<Connection> connectionPool = new ArrayList<>();
    
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // Inicializar pool
            for (int i = 0; i < MAX_CONNECTIONS; i++) {
                connectionPool.add(createConnection());
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error al inicializar el pool de conexiones", e);
        }
    }
    
    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
    public static synchronized Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            return createConnection();
        }
        return connectionPool.remove(connectionPool.size() - 1);
    }
    
    public static synchronized void releaseConnection(Connection conn) {
        if (conn != null) {
            connectionPool.add(conn);
        }
    }
}