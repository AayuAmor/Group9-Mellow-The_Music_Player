/*
 * AdminLogin moved to Dao package (UI class)
 */
package Dao;

import java.awt.event.ActionListener;
import view.Login;
import Controller.LoginController;

/**
 */
public class AdminLogin extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminLogin.class.getName());

    public AdminLogin() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        login = new javax.swing.JButton();
        back = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel1.setText("Admin Login");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(240, 60, 180, 40);

        email.setText("Enter admin email or username");
        getContentPane().add(email);
        email.setBounds(150, 150, 300, 30);

        password.setText("Enter password");
        getContentPane().add(password);
        password.setBounds(150, 200, 300, 30);

        login.setText("LOGIN");
        login.addActionListener(this::loginActionPerformed);
        getContentPane().add(login);
        login.setBounds(320, 260, 120, 30);

        back.setText("Back");
        back.addActionListener(this::backActionPerformed);
        getContentPane().add(back);
        back.setBounds(150, 260, 100, 30);

        setSize(new java.awt.Dimension(600, 420));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void loginActionPerformed(java.awt.event.ActionEvent evt) {
        // Handled by Controller
    }

    private void backActionPerformed(java.awt.event.ActionEvent evt) {
        // Navigate back to main login
        Login loginView = new Login();
        LoginController controller = new LoginController(loginView);
        controller.open();
        this.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton login;
    private javax.swing.JPasswordField password;
    // End of variables declaration//GEN-END:variables

    public void AddAdminLoginListener(ActionListener listener){
        login.addActionListener(listener);
    }

    public javax.swing.JPasswordField getPassword(){
        return password;
    }
    public javax.swing.JTextField getEmail(){
        return email;
    }

    /**
     * Small main to allow running this form directly from the IDE.
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            AdminLogin frame = new AdminLogin();
            frame.setVisible(true);
        });
    }
}
