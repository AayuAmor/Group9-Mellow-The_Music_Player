package Controller;

import Dao.userDao;
import Model.UserData;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import Dao.AdminLogin;
import view.AdminDashboard;

/**
 * Controller for AdminLogin view
 */
public class AdminLoginController {
    private final userDao userdao = new userDao();
    private final AdminLogin adminLoginView;

    public AdminLoginController(AdminLogin view) {
        this.adminLoginView = view;
        adminLoginView.AddAdminLoginListener(new AdminLoginListener());
    }

    public void open(){
        this.adminLoginView.setVisible(true);
    }

    public void close(){
        this.adminLoginView.dispose();
    }

    class AdminLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String username = adminLoginView.getEmail().getText();
                String password = new String(adminLoginView.getPassword().getPassword());

                UserData loggedInUser = userdao.authenticateAdmin(username, password);

                if (loggedInUser != null) {
                    // store session
                    Model.UserSession.getInstance().setUserData(loggedInUser);

                    JOptionPane.showMessageDialog(adminLoginView,
                            "Welcome, " + loggedInUser.getUsername(),
                            "Login Successful",
                            JOptionPane.INFORMATION_MESSAGE);

                    AdminDashboard adminDashboard = new AdminDashboard();
                    adminDashboard.setVisible(true);
                    close();
                } else {
                    JOptionPane.showMessageDialog(adminLoginView,
                            "Invalid admin credentials or not an admin.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (HeadlessException ex) {
                System.out.println(ex.getMessage());
                JOptionPane.showMessageDialog(adminLoginView, "Login Error: " + ex.getMessage());
            }
        }
    }
}
