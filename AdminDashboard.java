/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AdminDashboard;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author BIBEK's
 */
public class AdminDashboard extends JFrame{
    private JButton btnAddSong, btnDeleteSong, btnEditPlaylist, btnDeleteUser, btnResetAdmin, btnLogout;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(150, 20, 250, 30);
        add(title);

        btnAddSong = new JButton("Add Songs");
        btnAddSong.setBounds(150, 80, 200, 35);
        add(btnAddSong);

        btnDeleteSong = new JButton("Delete Songs");
        btnDeleteSong.setBounds(150, 130, 200, 35);
        add(btnDeleteSong);

        btnEditPlaylist = new JButton("Edit Playlist");
        btnEditPlaylist.setBounds(150, 180, 200, 35);
        add(btnEditPlaylist);

        btnDeleteUser = new JButton("Delete User");
        btnDeleteUser.setBounds(150, 230, 200, 35);
        add(btnDeleteUser);

        btnResetAdmin = new JButton("Reset Admin");
        btnResetAdmin.setBounds(150, 280, 200, 35);
        add(btnResetAdmin);

        btnLogout = new JButton("Logout");
        btnLogout.setBounds(150, 330, 200, 35);
        add(btnLogout);

        // --- Actions ---
        btnAddSong.addActionListener(e -> new AddSongForm().setVisible(true));
        btnDeleteSong.addActionListener(e -> new DeleteSongForm().setVisible(true));
        btnEditPlaylist.addActionListener(e -> new EditPlaylistForm().setVisible(true));
        btnDeleteUser.addActionListener(e -> new DeleteUserForm().setVisible(true));
        btnResetAdmin.addActionListener(e -> resetAdmin());
        btnLogout.addActionListener(e -> logout());
    }

    private void resetAdmin() {
        JOptionPane.showMessageDialog(this, "Admin reset functionality here.");
        // Add your SQL query to reset admin password or details.
    }

    private void logout() {
        JOptionPane.showMessageDialog(this, "Logged out.");
        System.exit(0);
    }

    public static void main(String[] args) {
        new AdminDashboard().setVisible(true);
    }
}
