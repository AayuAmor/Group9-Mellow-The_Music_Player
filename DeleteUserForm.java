/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AdminDashboard;
import javax.swing.*;
/**
 *
 * @author BIBEK's
 */
public class DeleteUserForm extends JFrame{
    public DeleteUserForm() {
        setTitle("Delete User");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel label = new JLabel("Enter User ID:");
        label.setBounds(40, 40, 200, 25);
        add(label);

        JTextField userId = new JTextField();
        userId.setBounds(150, 40, 150, 25);
        add(userId);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(120, 120, 120, 35);
        add(deleteBtn);

        deleteBtn.addActionListener(e -> {
            // SQL DELETE user logic
            JOptionPane.showMessageDialog(this, "User Deleted!");
            dispose();
        });
    }
}
