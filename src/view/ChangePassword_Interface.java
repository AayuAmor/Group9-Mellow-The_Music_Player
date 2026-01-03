package view;

import java.awt.Cursor;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChangePassword_Interface extends JFrame {

    private static final Logger logger = Logger.getLogger(ChangePassword_Interface.class.getName());

    private JTextField oldPasswordTextField;
    private JTextField newPasswordTextField;
    private JTextField confirmPasswordTextField;

    public ChangePassword_Interface() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setSize(994, 725);
        setResizable(false);
        setLayout(null);

        JPanel bannerPanel = new JPanel();
        bannerPanel.setBackground(new java.awt.Color(72, 118, 168));
        bannerPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 4, true));
        bannerPanel.setLayout(null);
        bannerPanel.setBounds(-10, 0, 1010, 120);

        JLabel titleLabel = new JLabel("Change Password");
        titleLabel.setFont(new java.awt.Font("Segoe UI Black", 1, 28));
        titleLabel.setForeground(new java.awt.Color(204, 204, 204));
        titleLabel.setBounds(200, 30, 300, 60);
        bannerPanel.add(titleLabel);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new java.awt.Color(159, 175, 192));
        backBtn.setFont(new java.awt.Font("Segoe UI", 1, 24));
        backBtn.setForeground(new java.awt.Color(255, 255, 255));
        backBtn.setBorder(null);
        backBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        backBtn.setBounds(20, 35, 133, 45);
        backBtn.addActionListener(e -> navigateBack());
        bannerPanel.add(backBtn);

        add(bannerPanel);

        JLabel oldLabel = new JLabel("Old Password:");
        oldLabel.setFont(new java.awt.Font("Segoe UI Black", 0, 18));
        oldLabel.setBounds(170, 190, 215, 44);
        add(oldLabel);

        oldPasswordTextField = new JTextField();
        oldPasswordTextField.setBackground(new java.awt.Color(204, 204, 204));
        oldPasswordTextField.setFont(new java.awt.Font("Segoe UI", 0, 18));
        oldPasswordTextField.setBounds(420, 190, 379, 40);
        add(oldPasswordTextField);

        JLabel newLabel = new JLabel("New Password:");
        newLabel.setFont(new java.awt.Font("Segoe UI Black", 0, 18));
        newLabel.setBounds(170, 250, 215, 44);
        add(newLabel);

        newPasswordTextField = new JTextField();
        newPasswordTextField.setBackground(new java.awt.Color(204, 204, 204));
        newPasswordTextField.setFont(new java.awt.Font("Segoe UI", 0, 18));
        newPasswordTextField.setBounds(420, 250, 379, 40);
        add(newPasswordTextField);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new java.awt.Font("Segoe UI Black", 0, 18));
        confirmLabel.setBounds(170, 310, 215, 44);
        add(confirmLabel);

        confirmPasswordTextField = new JTextField();
        confirmPasswordTextField.setBackground(new java.awt.Color(204, 204, 204));
        confirmPasswordTextField.setFont(new java.awt.Font("Segoe UI", 0, 18));
        confirmPasswordTextField.setBounds(420, 310, 379, 40);
        add(confirmPasswordTextField);

        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new java.awt.Color(72, 118, 168));
        saveBtn.setFont(new java.awt.Font("Segoe UI", 1, 14));
        saveBtn.setForeground(new java.awt.Color(255, 255, 255));
        saveBtn.setBounds(420, 380, 124, 42);
        saveBtn.addActionListener(e -> handleSave());
        add(saveBtn);

        setLocationRelativeTo(null);
    }

    private void navigateBack() {
        dispose();
    }

    private void handleSave() {
        String oldPassword = oldPasswordTextField.getText().trim();
        String newPassword = newPasswordTextField.getText().trim();
        String confirmPassword = confirmPasswordTextField.getText().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New password and confirmation do not match.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Password update logic not yet implemented.");
    }

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new ChangePassword_Interface().setVisible(true));
    }
}
