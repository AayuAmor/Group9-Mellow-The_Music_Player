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
            String username= userView.getUsername().getText();
            String email= userView.getEmail().getText();
            String password = userView.getPassword().getText();
            UserData userdata = new UserData(username,email);
            boolean check = userdao.check(userdata);
            if(check){
                JOptionPane.showMessageDialog(userView,"Duplicate user");
            }else{
                userdao.signUp(userdata);
                JOptionPane.showMessageDialog(userView,"Sucessful");
                
                Login lc = new Login();
                LoginController log= new LoginController(lc);
                closer();
                log.open();
            }
        }catch (HeadlessException ex){
            System.out.println(ex.getMessage());
        }
    }
        }
}    

