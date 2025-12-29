package Controller;

import Model.Song;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import utils.NowPlayingState;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PlayerController handles all audio playback logic.
 * Single responsibility: manage MediaPlayer and provide play/pause/stop/resume methods.
 * Singleton pattern ensures only one audio stream plays at a time.
 */
public class PlayerController {
    private static final Logger logger = Logger.getLogger(PlayerController.class.getName());
    private static PlayerController instance;
    
    private MediaPlayer mediaPlayer;
    private boolean fxInitialized = false;
    private Song currentSong;
    
    private PlayerController() {
        ensureJavaFxInitialized();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized PlayerController getInstance() {
        if (instance == null) {
            instance = new PlayerController();
        }
        return instance;
    }
    
    /**
     * Initialize JavaFX on first use
     */
    private void ensureJavaFxInitialized() {
        if (!fxInitialized) {
            new JFXPanel();
            fxInitialized = true;
        }
    }
    
    /**
     * Load and play a song
     */
    public void playSong(Song song) {
        if (song == null || song.getFilePath() == null) {
            logger.warning("Cannot play null song or song without file path");
            return;
        }
        
        // Stop previous playback if any
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        
        this.currentSong = song;
        File audioFile = new File(song.getFilePath());
        
        if (!audioFile.exists()) {
            logger.warning("Audio file not found: " + song.getFilePath());
            return;
        }
        
        Platform.runLater(() -> {
            try {
                String mediaUri = audioFile.toURI().toString();
                Media media = new Media(mediaUri);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setVolume(1.0);
                
                // Set end of media listener for loop/next song functionality
                mediaPlayer.setOnEndOfMedia(() -> {
                    logger.info("Song ended: " + song.getTitle());
                    // Notify PlaybackManager that song ended (it will handle loop or next)
                    service.PlaybackManager.getInstance().onSongEnded();
                });
                
                mediaPlayer.play();
                
                // Update global now playing state
                NowPlayingState.getInstance().setCurrentSong(song, true);
                
                logger.info("Playing: " + song.getTitle());
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error playing song: " + song.getTitle(), e);
            }
        });
    }
    
    /**
     * Pause current playback
     */
    public void pauseSong() {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                NowPlayingState.getInstance().setPlaying(false);
                logger.info("Paused");
            }
        });
    }
    
    /**
     * Resume playback
     */
    public void resumeSong() {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.play();
                NowPlayingState.getInstance().setPlaying(true);
                logger.info("Resumed");
            }
        });
    }
    
    /**
     * Stop playback completely
     */
    public void stopSong() {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                NowPlayingState.getInstance().setCurrentSong(null, false);
                logger.info("Stopped");
            }
        });
    }
    
    /**
     * Get currently playing song
     */
    public Song getCurrentSong() {
        return currentSong;
    }
    
    /**
     * Check if something is currently playing
     */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
    
    /**
     * Set playback volume (0.0 to 1.0)
     */
    public void setVolume(double volume) {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(Math.max(0.0, Math.min(1.0, volume)));
            }
        });
    }
}
