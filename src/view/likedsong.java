/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import Dao.LikedSongDao;
import Model.PlaySource;
import Model.Song;
import Model.UserSession;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import service.PlaybackManager;
import utils.LikedSongsNotifier;
import javax.swing.JOptionPane;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;

/**
 * Liked Songs view - displays user's favorited songs
 * Clicking a song sends full playlist to PlaybackManager with LIKED_SONGS
 * source
 *
 * @author rohit
 */
public class likedsong extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(likedsong.class.getName());
    private List<Song> likedSongs = new ArrayList<>();
    private final LikedSongDao likedSongDao;
    private List<Song> displayedSongs = new ArrayList<>();
    private final ExecutorService searchExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "liked-songs-search");
        t.setDaemon(true);
        return t;
    });
    private final Runnable refreshCallback = this::refreshLikedSongs;

    /**
     * Creates new form likedsong
     */
    public likedsong() {
        this.likedSongDao = new LikedSongDao();
        initComponents();
        loadLikedSongs();

        // Refresh when Player toggles like/unlike
        LikedSongsNotifier.register(refreshCallback);

        // Play on double-click
        LikedSongTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = LikedSongTable.getSelectedRow();
                    if (row >= 0) {
                        playSongFromLiked(row);
                    }
                }
            }
        });

        // After initComponents, configure NowPlaying panel with NowPlayingCard
        NowPlaying.removeAll();
        NowPlaying.setLayout(new BorderLayout());
        NowPlaying.setOpaque(true);
        NowPlaying.setBackground(new java.awt.Color(95, 138, 184));
        NowPlaying.add(new NowPlayingCard(), BorderLayout.CENTER);
        NowPlaying.setPreferredSize(new java.awt.Dimension(0, 80));
        NowPlaying.revalidate();
        NowPlaying.repaint();
    }

    /**
     * Load liked songs from database for logged-in user
     */
    private void loadLikedSongs() {
        UserSession session = UserSession.getInstance();
        if (!session.isLoggedIn()) {
            logger.warning("User not logged in - cannot load liked songs");
            return;
        }

        int userId = session.getUserId();
        likedSongs = likedSongDao.getLikedSongs(userId);

        List<Song> toRender = new ArrayList<>(likedSongs);
        SwingUtilities.invokeLater(() -> renderLikedSongs(toRender));

        logger.info("Loaded " + likedSongs.size() + " liked songs");
    }

    /**
     * Render liked songs in the table
     * TODO: Implement table rendering similar to other views
     */
    private void renderLikedSongs(List<Song> songs) {
        displayedSongs = songs == null ? new ArrayList<>() : new ArrayList<>(songs);
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
                new Object[] { "SN", "Title", "Artist", "Duration" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < displayedSongs.size(); i++) {
            Song s = displayedSongs.get(i);
            String duration = String.format("%d:%02d", s.getDurationSeconds() / 60, s.getDurationSeconds() % 60);
            model.addRow(new Object[] { i + 1, s.getTitle(), s.getArtist(), duration });
        }

        LikedSongTable.setModel(model);
    }

    private void refreshLikedSongs() {
        searchExecutor.submit(this::loadLikedSongs);
    }

    private void performSearch() {
        UserSession session = UserSession.getInstance();
        if (!session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Please login to view liked songs.", "Login Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String term = SearchBar.getText().trim();
        if (term.isEmpty() || term.equals("Search") || term.equals("               Search")) {
            renderLikedSongs(likedSongs);
            return;
        }

        int userId = session.getUserId();
        searchExecutor.submit(() -> {
            List<Song> results = likedSongDao.searchLikedSongs(userId, term);
            SwingUtilities.invokeLater(() -> {
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "No liked songs found for: " + term,
                            "Search Results",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                renderLikedSongs(results);
            });
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        searchBtn = new javax.swing.JButton();
        SearchBar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Backbtn = new javax.swing.JButton();
        NowPlaying = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        LikedSongTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(95, 138, 184));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(95, 138, 184));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        jPanel2.setForeground(new java.awt.Color(80, 135, 193));
        jPanel2.setLayout(null);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/mellow logo.png"))); // NOI18N
        jPanel2.add(jLabel1);
        jLabel1.setBounds(10, 10, 200, 129);

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setLayout(null);
        jPanel2.add(jPanel3);
        jPanel3.setBounds(0, 153, 956, 0);

        searchPanel.setBackground(new java.awt.Color(89, 141, 193));

        searchBtn.setBackground(new java.awt.Color(197, 191, 191));
        searchBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/meteor-icons_search.png"))); // NOI18N
        searchBtn.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(51, 51, 51)));
        searchBtn.addActionListener(this::searchBtnActionPerformed);

        SearchBar.setBackground(new java.awt.Color(160, 148, 148));
        SearchBar.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        SearchBar.setForeground(new java.awt.Color(204, 204, 204));
        SearchBar.setText("               Search");
        SearchBar.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 153, 153),
                new java.awt.Color(102, 102, 102)));
        SearchBar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SearchBarFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                SearchBarFocusLost(evt);
            }
        });
        SearchBar.addActionListener(this::SearchBarActionPerformed);

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
                searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchPanelLayout.createSequentialGroup()
                                .addComponent(searchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 592,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));
        searchPanelLayout.setVerticalGroup(
                searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(searchPanelLayout.createSequentialGroup()
                                .addGroup(searchPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(searchBtn, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(SearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 42,
                                                Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)));

        jPanel2.add(searchPanel);
        searchPanel.setBounds(220, 20, 650, 40);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(-4, -10, 1040, 170);

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 28)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Liked Songs");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 160, 300, 50);

        Backbtn.setBackground(java.awt.SystemColor.activeCaption);
        Backbtn.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        Backbtn.setForeground(new java.awt.Color(255, 255, 255));
        Backbtn.setText("←Back");
        Backbtn.addActionListener(this::BackbtnActionPerformed);
        jPanel1.add(Backbtn);
        Backbtn.setBounds(770, 580, 110, 30);

        NowPlaying.setBackground(new java.awt.Color(95, 138, 184));
        NowPlaying.setForeground(new java.awt.Color(102, 102, 102));
        NowPlaying.setLayout(null);
        jPanel1.add(NowPlaying);
        NowPlaying.setBounds(50, 540, 550, 80);

        LikedSongTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "SN", "Title ", "Artist", "Duration"
                }) {
            boolean[] canEdit = new boolean[] {
                    false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane2.setViewportView(LikedSongTable);
        if (LikedSongTable.getColumnModel().getColumnCount() > 0) {
            LikedSongTable.getColumnModel().getColumn(0).setResizable(false);
            LikedSongTable.getColumnModel().getColumn(1).setResizable(false);
            LikedSongTable.getColumnModel().getColumn(2).setResizable(false);
            LikedSongTable.getColumnModel().getColumn(3).setResizable(false);
        }

        jScrollPane1.setViewportView(jScrollPane2);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(210, 220, 560, 290);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 955, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_searchBtnActionPerformed
        performSearch();
    }// GEN-LAST:event_searchBtnActionPerformed

    private void SearchBarFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_SearchBarFocusGained
        if (SearchBar.getText().equals("               Search")) {
            SearchBar.setText("");
        }
    }// GEN-LAST:event_SearchBarFocusGained

    private void SearchBarFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_SearchBarFocusLost
        if (SearchBar.getText().isBlank()) {
            SearchBar.setText("               Search");
        }
    }// GEN-LAST:event_SearchBarFocusLost

    private void SearchBarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_SearchBarActionPerformed
        performSearch();
    }// GEN-LAST:event_SearchBarActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jTextField1ActionPerformed

    // GEN-LAST:event_jTextField1FocusLost

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_searchActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_searchActionPerformed

    private void search1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_search1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_search1ActionPerformed

    private void song3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_song3ActionPerformed
        // Play liked song 6
        playSongFromLiked(5);
    }// GEN-LAST:event_song3ActionPerformed

    private void song6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_song6ActionPerformed
        // Play liked song 7
        playSongFromLiked(6);
    }// GEN-LAST:event_song6ActionPerformed

    private void BackbtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_BackbtnActionPerformed
        // Navigate back to the UserDashboard without stopping playback
        UserDashboard dashboard = new UserDashboard();
        dashboard.setVisible(true);
        this.dispose();
    }// GEN-LAST:event_BackbtnActionPerformed

    private void song1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_song1ActionPerformed
        // Play liked song 1
        playSongFromLiked(0);
    }// GEN-LAST:event_song1ActionPerformed

    private void song2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_song2ActionPerformed
        // Play liked song 2
        playSongFromLiked(1);
    }// GEN-LAST:event_song2ActionPerformed

    private void song4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_song4ActionPerformed
        // Play liked song 3
        playSongFromLiked(2);
    }// GEN-LAST:event_song4ActionPerformed

    private void song5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_song5ActionPerformed
        // Play liked song 4
        playSongFromLiked(3);
    }// GEN-LAST:event_song5ActionPerformed

    private void song7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_song7ActionPerformed
        // Play liked song 5
        playSongFromLiked(4);
    }// GEN-LAST:event_song7ActionPerformed

    /**
     * Helper method to play a liked song at given index
     * Sends full liked songs list and index to PlaybackManager
     */
    private void playSongFromLiked(int index) {
        List<Song> source = (displayedSongs == null || displayedSongs.isEmpty()) ? likedSongs : displayedSongs;
        if (index >= 0 && index < source.size()) {
            Song selectedSong = source.get(index);

            // Send full liked songs list and selected index to PlaybackManager
            PlaybackManager.getInstance().setPlaylist(source, index, PlaySource.LIKED_SONGS);

            // Open Player UI with correct play source
            Player playerWindow = Player.getInstance();
            playerWindow.setPlaySource(PlaySource.LIKED_SONGS);
            playerWindow.setVisible(true);

            logger.info("Playing from Liked Songs: " + selectedSong.getTitle());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
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
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new likedsong().setVisible(true));
    }

    @Override
    public void dispose() {
        LikedSongsNotifier.unregister(refreshCallback);
        searchExecutor.shutdownNow();
        super.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Backbtn;
    private javax.swing.JTable LikedSongTable;
    private javax.swing.JPanel NowPlaying;
    private javax.swing.JTextField SearchBar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton searchBtn;
    private javax.swing.JPanel searchPanel;
    // End of variables declaration//GEN-END:variables
}
