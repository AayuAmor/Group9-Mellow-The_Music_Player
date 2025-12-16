/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mellow;

import Controller.UserController;
import Database.Database;
import Database.MySqlConnection;
import view.signUp;

/**
 *
 * @author oakin
 */
public class Mellow {

    /**Mellow
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Test database connection
        Database db = new MySqlConnection();
        if(db.openconnection() != null){
            System.out.println("connection opened");
            db.closeConnection(db.openconnection());
            
            // Open SignUp window
            signUp signup = new signUp();
            UserController usercontroller = new UserController(signup);
            usercontroller.open();
        } else {
            System.out.println("connection closed - cannot start application");
            System.err.println("ERROR: Database connection failed!");
            System.err.println("Please check MySQL service is running and credentials are correct.");
        }
    }
}
