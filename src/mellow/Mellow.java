/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mellow;

import Controller.LoginController;
import Database.Database;
import Database.MySqlConnection;
import view.Login;

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
            
            // Open Login window as app entry
            Login loginView = new Login();
            LoginController controller = new LoginController(loginView);
            controller.open();
        } else {
            System.out.println("connection closed - cannot start application");
            System.err.println("ERROR: Database connection failed!");
            System.err.println("Please check MySQL service is running and credentials are correct.");
        }
    }
}
