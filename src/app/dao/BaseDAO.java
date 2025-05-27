// BaseDAO.java
package app.dao;

import app.model.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDAO {
    protected Connection obtenerConexion() throws SQLException {
        return Conexion.getConnection();
    }
    
    protected void cerrarRecursos(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) Conexion.releaseConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}