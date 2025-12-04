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
public class AddSongForm extends JFrame{
    public AddSongForm() {
        setTitle("Add Song");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel nameLbl = new JLabel("Song Name:");
        nameLbl.setBounds(40, 40, 120, 25);
        add(nameLbl);

        JTextField songName = new JTextField();
        songName.setBounds(150, 40, 180, 25);
        add(songName);

        JLabel artistLbl = new JLabel("Artist:");
        artistLbl.setBounds(40, 90, 120, 25);
        add(artistLbl);

        JTextField artist = new JTextField();
        artist.setBounds(150, 90, 180, 25);
        add(artist);

        JButton saveBtn = new JButton("Save");
        saveBtn.setBounds(130, 180, 120, 35);
        add(saveBtn);

        saveBtn.addActionListener(e -> {
            String s = songName.getText();
            String a = artist.getText();

            // Add your SQL insert logic here
            JOptionPane.showMessageDialog(this, "Song Added!");
            dispose();
        });
    }
}

