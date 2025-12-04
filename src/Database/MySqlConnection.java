/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
/**
 *
 * @author oakin
 */
public class MySqlConnection implements Database{
    
    
    @Override
    public Connection openconnection() {
     try{
            String password = "aayuamor01";
            String username = "root";
            String database = "UserData";
            Connection connection;
            connection= DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/"+database,username,password);
            if (connection ==null){
                System.out.println("not connected");
            }else{
                System.out.println("successful");
            }
            return connection;
        }catch(SQLException e){
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void closeConnection(Connection conn) {
     try{
           if (conn !=null && !conn.isClosed()){
           conn.close();
           }
       }catch(SQLException e){
            System.out.println(e);
           }
    }

    @Override
    public ResultSet runQuery(Connection conn, String Query) {
        try{
            Statement stmp = conn.createStatement();
            ResultSet result= stmp.executeQuery(Query);
            return result;
        }catch(SQLException e){
          System.out.println(e);
          return null;
        }
    }

    @Override
    public int executeUpdate(Connection conn, String Query) {
        try{
            java.sql.Statement stmp = conn.createStatement();
            int result= stmp.executeUpdate(Query);
            return result;        
        }catch(SQLException e){
          System.out.println(e);
          return -1;
        }
    } 
}
    

