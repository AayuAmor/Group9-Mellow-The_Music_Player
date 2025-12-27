/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import Controller.PlayerController;
import Model.PlaySource;
import Model.Song;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.ImageIcon;
import service.PlaybackManager;
import utils.NowPlayingState;
import utils.NowPlayingState.NowPlayingListener;

/**
 * Player UI - Singleton pattern ensures only one player window exists
 * Displays metadata and controls for the currently playing song
 * Observes NowPlayingState for automatic UI updates
 */
public class Player extends javax.swing.JFrame implements NowPlayingListener {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Player.class.getName());
    private static Player instance;
    
    private final PlayerController playerController;
    private final PlaybackManager playbackManager;
    private boolean isPlaying = false;
    private PlaySource currentPlaySource = PlaySource.DASHBOARD;

    /**
     * Get singleton instance - creates if not exists
     */
    public static synchronized Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    /**
     * Set the play source (where playback was initiated from)
     * Used by back button to navigate to correct screen
     */
    public void setPlaySource(PlaySource source) {
        if (source != null) {
            this.currentPlaySource = source;
            NowPlayingState.getInstance().setPlaySource(source);
            logger.info(() -> "Player source set to: " + source.name());
        }
    }

    /**
     * Private constructor - use getInstance() instead
     */
    private Player() {
        initComponents();
        playerController = PlayerController.getInstance();
        playbackManager = PlaybackManager.getInstance();
        
        // Initialize PlayPausebtn with play icon
        setPlayPauseIcon(false);
        isPlaying = false;
        
        // Set window properties
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        
        // Listen for now playing changes
        NowPlayingState.getInstance().addListener(this);
    }

    /**
     * Called when now playing song changes
     */
    @Override
    public void onNowPlayingChanged(Song song, boolean playing) {
        if (song == null) {
            return;
        }
        
        // Update metadata display
        updateMetadata(song);
        
        // Update button state
        isPlaying = playing;
        setPlayPauseIcon(playing);
        
        // Reset Like button to default state when song changes
        // TODO: Check database if song is actually liked and set accordingly
        likeSongBtn.setBackground(new java.awt.Color(164, 183, 203)); // Default color
        likeSongBtn.setOpaque(true);
        likeSongBtn.setBorderPainted(false);
        
        // Update loop button state
        boolean looping = playbackManager.isLooping();
        if (looping) {
            loopSongBtn.setBackground(new java.awt.Color(100, 100, 50)); // Dark when active
            loopSongBtn.setOpaque(true);
            loopSongBtn.setBorderPainted(false);
        } else {
            loopSongBtn.setBackground(new java.awt.Color(164, 183, 203)); // Default
            loopSongBtn.setOpaque(true);
            loopSongBtn.setBorderPainted(false);
        }
    }
    
    /**
     * Update metadata display for the given song
     */
    private void updateMetadata(Song song) {
        albumNameMetadata.setText(song.getAlbum() != null ? song.getAlbum() : "Unknown Album");
        artistMetadata.setText(song.getArtist());
        
        int durationSeconds = song.getDurationSeconds();
        String duration = String.format("%d:%02d", durationSeconds / 60, durationSeconds % 60);
        durationMetadata.setText(duration);
        
        // Load album art if available
        if (song.getImagePath() != null) {
            loadAlbumArt(song.getImagePath());
        } else {
            loadDefaultAlbumArt();
        }
    }
    
    /**
     * Safely load album art from path
     */
    private void loadAlbumArt(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                URL imageUrl = imageFile.toURI().toURL();
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    int width = songImagePanel.getWidth();
                    int height = songImagePanel.getHeight();
                    if (width > 0 && height > 0) {
                        // Scale image to fit panel
                        icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
                    }
                } else {
                    loadDefaultAlbumArt();
                }
            } else {
                loadDefaultAlbumArt();
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.WARNING, "Failed to load album art", e);
            loadDefaultAlbumArt();
        }
    }
    
    /**
     * Load default placeholder album art
     */
    private void loadDefaultAlbumArt() {
        // Default placeholder - can be enhanced
    }
    
    /**
     * Safely set play/pause button icon based on state
     */
    private void setPlayPauseIcon(boolean playing) {
        try {
            URL iconUrl;
            if (playing) {
                iconUrl = getClass().getResource("/Images/pause.png");
            } else {
                iconUrl = getClass().getResource("/Images/icon-park-outline_play.png");
            }
            
            if (iconUrl != null) {
                PlayPausebtn.setIcon(new ImageIcon(iconUrl));
            } else {
                logger.warning("Icon resource not found");
            }
        } catch (Exception e) {
            logger.warning(() -> "Failed to load icon: " + e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        backBtn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        songsMetadataPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        songImagePanel = new javax.swing.JPanel();
        albumNameMetadata = new javax.swing.JLabel();
        durationMetadata = new javax.swing.JLabel();
        artistMetadata = new javax.swing.JLabel();
        buttonsHolderPanel = new javax.swing.JPanel();
        addToPlaylistBtn = new javax.swing.JButton();
        loopSongBtn = new javax.swing.JButton();
        PlayPausebtn = new javax.swing.JButton();
        nextBtn = new javax.swing.JButton();
        previousBtn = new javax.swing.JButton();
        likeSongBtn = new javax.swing.JButton();
        playingSongImagePanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(164, 183, 203));

        jPanel2.setBackground(new java.awt.Color(197, 215, 234));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 3, true));

        backBtn.setBackground(new java.awt.Color(162, 189, 197));
        backBtn.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        backBtn.setForeground(new java.awt.Color(255, 255, 255));
        backBtn.setText("←Back");
        backBtn.addActionListener(this::backBtnActionPerformed);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Player");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(backBtn)
                .addGap(38, 38, 38)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backBtn)
                    .addComponent(jLabel5))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        songsMetadataPanel.setBackground(new java.awt.Color(164, 183, 203));
        songsMetadataPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(135, 164, 193), 3, true));

        jPanel4.setBackground(new java.awt.Color(175, 197, 220));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(158, 185, 211), 3, true));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Details");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(137, 137, 137))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Album:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Duration:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Artist:");

        javax.swing.GroupLayout songImagePanelLayout = new javax.swing.GroupLayout(songImagePanel);
        songImagePanel.setLayout(songImagePanelLayout);
        songImagePanelLayout.setHorizontalGroup(
            songImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 111, Short.MAX_VALUE)
        );
        songImagePanelLayout.setVerticalGroup(
            songImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 112, Short.MAX_VALUE)
        );

        albumNameMetadata.setText("song name");

        durationMetadata.setText("duration");

        artistMetadata.setText("artist");

        javax.swing.GroupLayout songsMetadataPanelLayout = new javax.swing.GroupLayout(songsMetadataPanel);
        songsMetadataPanel.setLayout(songsMetadataPanelLayout);
        songsMetadataPanelLayout.setHorizontalGroup(
            songsMetadataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(songsMetadataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(songsMetadataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(songsMetadataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(songsMetadataPanelLayout.createSequentialGroup()
                        .addComponent(durationMetadata, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 87, Short.MAX_VALUE))
                    .addComponent(artistMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(songsMetadataPanelLayout.createSequentialGroup()
                        .addComponent(albumNameMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, songsMetadataPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(songImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(119, 119, 119))
        );
        songsMetadataPanelLayout.setVerticalGroup(
            songsMetadataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(songsMetadataPanelLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(songImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(songsMetadataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(albumNameMetadata, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(songsMetadataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(durationMetadata, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(songsMetadataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(artistMetadata, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(0, 15, Short.MAX_VALUE))
        );

        buttonsHolderPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(147, 176, 206), 10, true));

        addToPlaylistBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/addtoplaylist.png"))); // NOI18N

        loopSongBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/fad_loop.png"))); // NOI18N

        PlayPausebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/pause.png"))); // NOI18N
        PlayPausebtn.addActionListener(this::PlayPausebtnActionPerformed);

        nextBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/right arrow.png"))); // NOI18N

        previousBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/backarrow.png"))); // NOI18N

        likeSongBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/likedSongsBtn_icon.png"))); // NOI18N
        likeSongBtn.addActionListener(this::likeSongBtnActionPerformed);

        javax.swing.GroupLayout buttonsHolderPanelLayout = new javax.swing.GroupLayout(buttonsHolderPanel);
        buttonsHolderPanel.setLayout(buttonsHolderPanelLayout);
        buttonsHolderPanelLayout.setHorizontalGroup(
            buttonsHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsHolderPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(likeSongBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(previousBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PlayPausebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(nextBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(addToPlaylistBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(loopSongBtn)
                .addGap(11, 11, 11))
        );
        buttonsHolderPanelLayout.setVerticalGroup(
            buttonsHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsHolderPanelLayout.createSequentialGroup()
                .addGroup(buttonsHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(buttonsHolderPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(likeSongBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(buttonsHolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(addToPlaylistBtn)
                        .addComponent(loopSongBtn)
                        .addComponent(nextBtn)
                        .addComponent(previousBtn)
                        .addComponent(PlayPausebtn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout playingSongImagePanelLayout = new javax.swing.GroupLayout(playingSongImagePanel);
        playingSongImagePanel.setLayout(playingSongImagePanelLayout);
        playingSongImagePanelLayout.setHorizontalGroup(
            playingSongImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        playingSongImagePanelLayout.setVerticalGroup(
            playingSongImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 269, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonsHolderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(playingSongImagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addComponent(songsMetadataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(playingSongImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(buttonsHolderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 235, Short.MAX_VALUE)
                        .addComponent(songsMetadataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // Just hide the Player window - don't create new windows or dispose
        // The parent view (Dashboard, AllSongs, etc.) is already open in background
        this.setVisible(false);
    }//GEN-LAST:event_backBtnActionPerformed

    private void PlayPausebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayPausebtnActionPerformed
        // Toggle play/pause through PlaybackManager
        playbackManager.togglePlayPause();
        logger.info("Play/Pause toggled via UI button");
    }//GEN-LAST:event_PlayPausebtnActionPerformed

    private void likeSongBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_likeSongBtnActionPerformed
        // Like current song - add to liked songs
        Song currentSong = playbackManager.getCurrentSong();
        if (currentSong != null) {
            logger.info("Liked song: " + currentSong.getTitle());
            
            // Toggle like state by changing button background color
            boolean isLiked = likeSongBtn.getBackground().equals(new java.awt.Color(100, 50, 100));
            if (!isLiked) {
                // Like the song - set darker background
                likeSongBtn.setBackground(new java.awt.Color(100, 50, 100)); // Dark purple
                likeSongBtn.setOpaque(true);
                likeSongBtn.setBorderPainted(false);
            } else {
                // Unlike the song - restore default background
                likeSongBtn.setBackground(new java.awt.Color(164, 183, 203)); // Default
                likeSongBtn.setOpaque(true);
                likeSongBtn.setBorderPainted(false);
            }
            
            // TODO: Persist like to database or liked songs list
        } else {
            logger.warning("No song currently playing to like");
        }
    }//GEN-LAST:event_likeSongBtnActionPerformed
    
    /**
     * Handle next button click - play next song in playlist
     */
    private void nextBtnActionPerformed(java.awt.event.ActionEvent evt) {
        playbackManager.playNext();
        logger.info("Next button clicked");
    }
    
    /**
     * Handle previous button click - play previous song in playlist
     */
    private void previousBtnActionPerformed(java.awt.event.ActionEvent evt) {
        playbackManager.playPrevious();
        logger.info("Previous button clicked");
    }
    
    /**
     * Handle loop button click - toggle loop mode
     */
    private void loopSongBtnActionPerformed(java.awt.event.ActionEvent evt) {
        playbackManager.toggleLoop();
        boolean looping = playbackManager.isLooping();
        logger.info("Loop mode: " + (looping ? "ON" : "OFF"));
        
        // Update button appearance dynamically - change background color when active
        if (looping) {
            // Loop is ON - set darker background color
            loopSongBtn.setBackground(new java.awt.Color(100, 100, 50)); // Dark yellow/brown
            loopSongBtn.setOpaque(true);
            loopSongBtn.setBorderPainted(false);
        } else {
            // Loop is OFF - restore default background
            loopSongBtn.setBackground(new java.awt.Color(164, 183, 203)); // Default color
            loopSongBtn.setOpaque(true);
            loopSongBtn.setBorderPainted(false);
        }
    }
    
    /**
     * Handle add to playlist button click
     */
    private void addToPlaylistBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Song currentSong = playbackManager.getCurrentSong();
        if (currentSong != null) {
            logger.info("Adding to playlist: " + currentSong.getTitle());
            // TODO: Open playlist selection dialog or add to default playlist
        } else {
            logger.warning("No song currently playing to add");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Player().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton PlayPausebtn;
    private javax.swing.JButton addToPlaylistBtn;
    private javax.swing.JLabel albumNameMetadata;
    private javax.swing.JLabel artistMetadata;
    private javax.swing.JButton backBtn;
    private javax.swing.JPanel buttonsHolderPanel;
    private javax.swing.JLabel durationMetadata;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton likeSongBtn;
    private javax.swing.JButton loopSongBtn;
    private javax.swing.JButton nextBtn;
    private javax.swing.JPanel playingSongImagePanel;
    private javax.swing.JButton previousBtn;
    private javax.swing.JPanel songImagePanel;
    private javax.swing.JPanel songsMetadataPanel;
    // End of variables declaration//GEN-END:variables
}
