/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.UserData;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import view.Login;
import Dao.loginDao;

/**
 *
 * @author Asus
 */
public class LoginController {
    private final loginDao logindao= new loginDao();
    //private final Login loginView;
    private final Login loginview;
    
    
    public  LoginController (Login LoginView){
        this.loginview=LoginView;
        
        loginview.AddLoginListener(new AddLoginListener());
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
            String password= loginview.getPassword().getText();
            UserData userdata = new UserData(username,password);
            boolean check = logindao.Login(userdata);
            if(check){
                JOptionPane.showMessageDialog(loginview,"Duplicate user");
            }else{
                logindao.Login(userdata);
                JOptionPane.showMessageDialog(loginview,"Sucessful");
               
            }
        }catch (HeadlessException ex){
            System.out.println(ex.getMessage());
        }
    }
        }
    }
