/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Dao.loginDao;
import Model.UserData;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import view.AdminDashboard;
import view.Login;
import view.UserDashboard;

/**
 *
 * @author Asus
 */
public class LoginController {
    private final loginDao logindao= new loginDao();
    private final Login loginview;
    
    
    public  LoginController (Login LoginView){
        this.loginview=LoginView;
        
        loginview.AddLoginListener(new AddLoginListener());
        loginview.AddSignupButtonListener(new SignupButtonListener());
    }
    public void open(){
        this.loginview.setVisible(true);
    }
    public void closer(){
        this.loginview.dispose();
    }

    class AddLoginListener implements ActionListener {
@Override
    public void actionPerformed (ActionEvent e){
        try{
            String username= loginview.getEmail().getText();
            String password= new String(loginview.getPassword().getPassword());
            
            UserData userdata = new UserData(username, password);
            UserData loggedInUser = logindao.Login(userdata);
            
            if(loggedInUser != null){
                // Store user session
                Model.UserSession.getInstance().setUserData(loggedInUser);
                
                System.out.println("=== DEBUG: Login successful ===");
                System.out.println("Username: " + loggedInUser.getUsername());
                System.out.println("Email: " + loggedInUser.getEmail());
                System.out.println("Role: " + loggedInUser.getRole());
                
                JOptionPane.showMessageDialog(loginview,
                    "Welcome back, " + loggedInUser.getUsername() + "!",
                    "Login Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Navigate based on role
                if("admin".equalsIgnoreCase(loggedInUser.getRole())){
                    AdminDashboard adminDashboard = new AdminDashboard();
                    adminDashboard.setVisible(true);
                } else {
                    UserDashboard userDashboard = new UserDashboard();
                    userDashboard.setVisible(true);
                }
                
                closer();
            }else{
                JOptionPane.showMessageDialog(loginview,
                    "Invalid email/username or password.\nPlease check your credentials and try again.",
                    "Login Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }catch (HeadlessException ex){
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(loginview,"Login Error: " + ex.getMessage());
        }
    }
        }
    
    class SignupButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Navigate to SignUp window
            view.signUp signupView = new view.signUp();
            UserController userController = new UserController(signupView);
            closer();
            userController.open();
        }
    }
    }
