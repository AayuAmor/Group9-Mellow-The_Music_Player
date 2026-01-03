package service;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel; // Ensures JavaFX runtime is initialized in Swing apps
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * DEPRECATED - Use PlayerController and PlaybackManager instead.
 * 
 * This class was part of the old architecture and should not be used.
 * All playback logic has been consolidated into:
 * - PlayerController: Handles actual audio playback via MediaPlayer
 * - PlaybackManager: Manages playlist, state, and playback flow
 * - Player UI: Displays metadata and forwards button clicks
 * 
 * Keeping this file only for reference during migration. Will be removed in
 * final cleanup.
 * 
 * Service for audio playback using JavaFX MediaPlayer.
 * All public methods are thread-safe and marshal to JavaFX Application Thread.
 */
@Deprecated(since = "2.0", forRemoval = true)
public class PlayerService {
    private volatile MediaPlayer mediaPlayer;
    private static final Object lock = new Object();
    private static boolean initialized = false;

    public PlayerService() {
        ensureInitialized();
    }

    /**
     * Play the provided local file path. Stops previous playback if active.
     * 
     * @param filePath
     */
    public void play(String filePath) {
        if (filePath == null || filePath.isBlank())
            return;
        ensureInitialized();
        Platform.runLater(() -> {
            synchronized (lock) {
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.stop();
                    } catch (Exception ignored) {
                    }
                    try {
                        mediaPlayer.dispose();
                    } catch (Exception ignored) {
                    }
                    mediaPlayer = null;
                }
                File f = new File(filePath);
                Media media = new Media(f.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnError(() -> {
                });
                mediaPlayer.play();
            }
        });
    }

    /** Pause current playback. */
    public void pause() {
        Platform.runLater(() -> {
            synchronized (lock) {
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.pause();
                    } catch (Exception ignored) {
                    }
                }
            }
        });
    }

    /** Stop and release current player. */
    public void stop() {
        Platform.runLater(() -> {
            synchronized (lock) {
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.stop();
                    } catch (Exception ignored) {
                    }
                    try {
                        mediaPlayer.dispose();
                    } catch (Exception ignored) {
                    }
                    mediaPlayer = null;
                }
            }
        });
    }

    private void ensureInitialized() {
        if (!initialized) {
            synchronized (PlayerService.class) {
                if (!initialized) {
                    // JFXPanel bootstrap initializes JavaFX runtime in Swing apps
                    new JFXPanel();
                    initialized = true;
                }
            }
        }
    }
}
