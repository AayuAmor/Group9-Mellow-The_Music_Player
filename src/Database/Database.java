/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Database;
import java.sql.*;


/**
 *
 * @author oakin
 */
public interface Database {
    Connection openconnection();
    void closeConnection(Connection conn);
    ResultSet runQuery(Connection conn, String Query);
    int executeUpdate(Connection conn, String Query);
    
}
