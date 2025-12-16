/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Dao.userDao;
import Model.UserData;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import view.Login;
import view.signUp;

/**
 *
 * @author rohit
 */
public class UserController {
    private final userDao userdao= new userDao();
    private final signUp userView;
    
    public  UserController (signUp userView){
        this.userView=userView;
        
        userView.AddAAUserListener(new AddActionListener());
        userView.AddLoginButtonListener(new LoginButtonListener());
    }
    public void open(){
        this.userView.setVisible(true);
    }
    public void closer(){
        this.userView.dispose();
    }

    class AddActionListener implements ActionListener {
@Override
    public void actionPerformed (ActionEvent e){
        try{
            String username= userView.getUsername().getText().trim();
            String email= userView.getEmail().getText().trim();
            String password = userView.getPassword().getText();
            
            // Validate input fields
            if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(userView, 
                    "Please fill in all required fields.",
                    "Incomplete Form", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if(password.length() < 6){
                JOptionPane.showMessageDialog(userView, 
                    "Password must be at least 6 characters long.",
                    "Weak Password", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            UserData userdata = new UserData(username, email, password);
            boolean check = userdao.check(userdata);
            if(check){
                JOptionPane.showMessageDialog(userView,
                    "This username or email is already registered.\nPlease use a different one or login instead.",
                    "Account Already Exists", 
                    JOptionPane.INFORMATION_MESSAGE);
            }else{
                userdao.signUp(userdata);
                JOptionPane.showMessageDialog(userView,
                    "Account created successfully!\nPlease login to continue.",
                    "Registration Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                Login lc = new Login();
                LoginController log= new LoginController(lc);
                closer();
                log.open();
            }
        }catch (HeadlessException ex){
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(userView,
                "Unable to complete registration.\nPlease try again.",
                "Registration Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
        }
    
    class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Navigate to Login window
            Login loginView = new Login();
            LoginController loginController = new LoginController(loginView);
            closer();
            loginController.open();
        }
    }}