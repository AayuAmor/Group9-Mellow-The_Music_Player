package Controller;

import Dao.userDao;
import Model.UserData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import view.AdminDashboard;
import view.AdminLoginUI;

/**
 * Controller for AdminLogin view
 */
public class AdminLoginController {
    private final userDao userdao = new userDao();
    private final AdminLoginUI adminLoginView;

    public AdminLoginController(AdminLoginUI view) {
        this.adminLoginView = view;
        this.adminLoginView.addAdminLoginListener(new AdminLoginListener());
    }

    public void open() {
        this.adminLoginView.setVisible(true);
    }

    public void close() {
        this.adminLoginView.dispose();
    }

    class AdminLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = adminLoginView.getEmailField().getText().trim();
            String password = new String(adminLoginView.getPasswordField().getPassword());

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(adminLoginView,
                        "Please enter your admin email or username.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (password.isBlank()) {
                JOptionPane.showMessageDialog(adminLoginView,
                        "Password cannot be empty.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            UserData loggedInUser = userdao.authenticateAdmin(username, password);

            if (loggedInUser != null) {
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
                        "Invalid admin credentials or insufficient permissions.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
