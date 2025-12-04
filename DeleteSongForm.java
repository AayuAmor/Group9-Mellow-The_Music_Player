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
public class DeleteSongForm extends JFrame{
    public DeleteSongForm() {
        setTitle("Delete Song");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel label = new JLabel("Enter Song ID to delete:");
        label.setBounds(40, 40, 200, 25);
        add(label);

        JTextField songId = new JTextField();
        songId.setBounds(200, 40, 120, 25);
        add(songId);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(130, 120, 120, 35);
        add(deleteBtn);

        deleteBtn.addActionListener(e -> {
            // SQL DELETE logic
            JOptionPane.showMessageDialog(this, "Song Deleted!");
            dispose();
        });
    }
}
