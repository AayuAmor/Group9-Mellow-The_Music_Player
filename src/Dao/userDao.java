/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Database.MySqlConnection;
import Model.UserData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Asus
 */
public class userDao {
    MySqlConnection mysql = new MySqlConnection() {}; 
    public void signUp(UserData user){
        Connection conn = mysql.openconnection();
        String sql=  "Insert into users (username, email, password) values(?,?,?)";
        try(PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getPassword());
            pstm.executeUpdate();
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            mysql.closeConnection(conn);
        }
    }
    
public boolean check(UserData user) {
    Connection conn = mysql.openconnection();
    String sql = "Select * from users where username = ? or email= ?";
    try(PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getEmail());
            ResultSet result = pstm.executeQuery();
            return result.next();
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            mysql.closeConnection(conn);
        }
        return false;
    }
}
