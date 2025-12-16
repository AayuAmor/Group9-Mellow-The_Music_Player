/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Asus
 */
public class UserData {
    private int user_id;
    private String username;
    private String password;
    private String email;
    private String role;
    
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    
    public void setId(int user_id) {
        this.user_id = user_id;
    }
    public int getId() {
        return user_id;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }
    
    // Constructor for signup
    public UserData(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    // Constructor for login
    public UserData(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
