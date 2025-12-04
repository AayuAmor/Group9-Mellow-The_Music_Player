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
public class EditPlaylistForm extends JFrame{
    public EditPlaylistForm() {
        setTitle("Edit Playlist");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel label = new JLabel("Playlist ID:");
        label.setBounds(40, 40, 150, 25);
        add(label);

        JTextField playlistId = new JTextField();
        playlistId.setBounds(150, 40, 180, 25);
        add(playlistId);

        JLabel newNameLbl = new JLabel("New Name:");
        newNameLbl.setBounds(40, 90, 150, 25);
        add(newNameLbl);

        JTextField newName = new JTextField();
        newName.setBounds(150, 90, 180, 25);
        add(newName);

        JButton update = new JButton("Update");
        update.setBounds(150, 160, 120, 35);
        add(update);

        update.addActionListener(e -> {
            // SQL UPDATE playlist logic here
            JOptionPane.showMessageDialog(this, "Playlist Updated!");
            dispose();
        });
    }
}
