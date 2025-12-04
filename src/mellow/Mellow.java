/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mellow;

import Controller.UserController;
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
        // TODO code application logic here
        signUp signup = new signUp();
    UserController usercontroller = new UserController(signup);
    usercontroller.open();

}
}
