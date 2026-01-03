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
public class loginDao {
    MySqlConnection mysql = new MySqlConnection();    
    public UserData Login(UserData user) {
        Connection conn = mysql.openconnection();
        String sql = "Select * from users where (username = ? OR email = ?)";
        try(PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getUsername()); // Can login with email or username
            ResultSet result = pstm.executeQuery();
            if(result.next()){
                String storedPassword = result.getString("password");
                String inputPassword = user.getPassword();
                boolean passwordMatches = false;
                try {
                    if (utils.PasswordService.isHashed(storedPassword)) {
                        passwordMatches = utils.PasswordService.verifyPassword(inputPassword, storedPassword);
                    } else {
                        // legacy plaintext comparison
                        passwordMatches = storedPassword != null && storedPassword.equals(inputPassword);
                    }
                } catch (Exception ex) {
                    System.out.println("Password verification error: " + ex.getMessage());
                    passwordMatches = false;
                }

                if (passwordMatches) {
                    UserData loggedInUser = new UserData(user.getUsername(), user.getPassword());
                    loggedInUser.setId(result.getInt("user_id"));
                    loggedInUser.setUsername(result.getString("username"));
                    loggedInUser.setEmail(result.getString("email"));
                    loggedInUser.setRole(result.getString("role")); // Fetch role from database
                    loggedInUser.setCreatedAt(result.getTimestamp("created_at"));
                    return loggedInUser;
                }
            }
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            mysql.closeConnection(conn);
        }
        return null;
    }
}

