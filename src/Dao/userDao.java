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
import utils.PasswordService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Asus
 */
public class userDao {
    MySqlConnection mysql = new MySqlConnection() {}; 
    public void signUp(UserData user){
        Connection conn = mysql.openconnection();
        String sql=  "Insert into users (username, email, password, role) values(?,?,?,?)";
        try(PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getPassword());
            pstm.setString(4, "user"); // Default role is 'user'
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

    public boolean existsByEmail(String email) {
        Connection conn = mysql.openconnection();
        String sql = "SELECT 1 FROM users WHERE email = ? LIMIT 1";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    public boolean updatePasswordByEmail(String email, String plainPassword) {
        Connection conn = mysql.openconnection();
        String hashed = PasswordService.hashPassword(plainPassword);
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, hashed);
            pstm.setString(2, email);
            int rows = pstm.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    public List<UserData> getAllUsers() {
        List<UserData> users = new ArrayList<>();
        Connection conn = mysql.openconnection();
        String sql = "SELECT user_id, username, email, role, created_at FROM users";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                UserData u = new UserData();
                u.setId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setCreatedAt(rs.getTimestamp("created_at"));
                users.add(u);
            }
            return users;
        } catch (SQLException e) {
            System.out.println(e);
            return users;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    /**
     * Ensure at least one admin exists. If not, create the default admin with provided credentials.
     */
    public void ensureAdminExists(String username, String email, String plainPassword) {
        Connection conn = mysql.openconnection();
        String checkSql = "SELECT 1 FROM users WHERE role = 'admin' LIMIT 1";
        try (PreparedStatement checkPstm = conn.prepareStatement(checkSql)) {
            ResultSet rs = checkPstm.executeQuery();
            if (!rs.next()) {
                // create default admin
                String hashed = utils.PasswordService.hashPassword(plainPassword);
                String insertSql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, 'admin')";
                try (PreparedStatement insPstm = conn.prepareStatement(insertSql)) {
                    insPstm.setString(1, username);
                    insPstm.setString(2, email);
                    insPstm.setString(3, hashed);
                    insPstm.executeUpdate();
                    System.out.println("Default admin created: " + username);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
    }

    /**
     * Authenticate admin by username or email and password. Returns UserData if credentials match and role is admin, otherwise null.
     */
    public UserData authenticateAdmin(String usernameOrEmail, String plainPassword) {
        Connection conn = mysql.openconnection();
        String sql = "SELECT * FROM users WHERE username = ? OR email = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, usernameOrEmail);
            pstm.setString(2, usernameOrEmail);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                if (role == null || !"admin".equalsIgnoreCase(role)) {
                    return null; // not an admin
                }
                String storedPassword = rs.getString("password");
                boolean passwordMatches = false;
                try {
                    if (utils.PasswordService.isHashed(storedPassword)) {
                        passwordMatches = utils.PasswordService.verifyPassword(plainPassword, storedPassword);
                    } else {
                        passwordMatches = storedPassword != null && storedPassword.equals(plainPassword);
                    }
                } catch (Exception ex) {
                    System.out.println("Password verification error: " + ex.getMessage());
                    passwordMatches = false;
                }
                if (passwordMatches) {
                    UserData u = new UserData();
                    u.setId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setEmail(rs.getString("email"));
                    u.setRole(role);
                    u.setCreatedAt(rs.getTimestamp("created_at"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }
}
