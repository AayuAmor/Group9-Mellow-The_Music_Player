package view;

import Model.Song;
import java.awt.*;
import javax.swing.*;
import utils.NowPlayingState;
import utils.PlayerState;

/**
 * NowPlayingCard - Reusable Component for Now Playing Display
 * 
 * Reads song info from PlayerState singleton via NowPlayingState observer
 * pattern.
 * Shows "Now Playing: <song_name> - <artist>"
 * Clicking opens Player.java without disrupting timestamp
 * 
 * AUTO-REFRESHES when PlayerState changes via NowPlayingListener
 * 
 * Usage: Simply add to any panel with:
 * panel.add(new NowPlayingCard(), BorderLayout.CENTER);
 */
public class NowPlayingCard extends JPanel implements NowPlayingState.NowPlayingListener {
    private final JLabel songInfoLabel;

    /**
     * Creates a new NowPlayingCard instance
     */
    public NowPlayingCard() {

        // Panel setup - MUST be opaque and have proper sizing
        setLayout(new BorderLayout(10, 0));
        setBackground(new Color(89, 141, 193));
        setOpaque(true); // CRITICAL: must be opaque
        setPreferredSize(new Dimension(800, 70));
        setMinimumSize(new Dimension(100, 70));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Now Playing icon/label
        JLabel nowPlayingIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Images/nowplaying.PNG"));
            Image scaledImg = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            nowPlayingIcon.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            nowPlayingIcon.setText("♪");
            nowPlayingIcon.setFont(new Font("Segoe UI", Font.BOLD, 24));
            nowPlayingIcon.setForeground(new Color(255, 255, 255));
        }
        add(nowPlayingIcon, BorderLayout.WEST);

        // Song info label (clickable to open player)
        songInfoLabel = new JLabel("No song playing");
        songInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        songInfoLabel.setForeground(new Color(255, 255, 255));

        songInfoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Song currentSong = PlayerState.getInstance().getCurrentSong();
                if (currentSong != null) {
                    Player playerWindow = Player.getInstance();
                    playerWindow.setVisible(true);
                    playerWindow.toFront();
                }
            }
        });

        add(songInfoLabel, BorderLayout.CENTER);

        // Register as listener for now playing changes
        NowPlayingState.getInstance().addListener(this);

        // Initial refresh from NowPlayingState
        refreshNowPlaying();
    }

    /**
     * Observer callback - auto-refresh when NowPlayingState changes
     */
    @Override
    public void onNowPlayingChanged(Song song, boolean isPlaying) {
        refreshNowPlaying();
    }

    /**
     * Public method to refresh the now playing display
     * Called via NowPlayingListener when song changes
     */
    public void refreshNowPlaying() {
        SwingUtilities.invokeLater(() -> {
            // Read from NowPlayingState (which is updated by PlaybackManager)
            Song currentSong = NowPlayingState.getInstance().getCurrentSong();

            if (currentSong == null) {
                songInfoLabel.setText("No song playing");
            } else {
                String displayText = "Now Playing: " + currentSong.getTitle() + " - " + currentSong.getArtist();
                songInfoLabel.setText(displayText);

                // DEBUG: Log the song being displayed
                System.out.println("[NowPlayingCard] Displaying: " + displayText);
            }

            revalidate();
            repaint();
        });
    }
}
