package service;

import Controller.PlayerController;
import Model.PlaySource;
import Model.Song;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import utils.NowPlayingState;
import utils.PlayerState;

/**
 * Global Playback Manager - Singleton pattern
 * 
 * Centralized management of audio playback with:
 * - Current song tracking
 * - Playlist management
 * - Playback controls (play, pause, next, previous)
 * - Loop mode toggle
 * - Navigation context (PlaySource) for back button
 * 
 * Persists across UI changes - music continues in background
 * Works with PlayerController for actual audio playback
 */
public class PlaybackManager {
    private static final Logger logger = Logger.getLogger(PlaybackManager.class.getName());
    private static PlaybackManager instance;

    private final PlayerController playerController;
    private Song currentSong;
    private List<Song> playlist = new ArrayList<>();
    private int currentIndex = -1;
    private boolean isLooping = false;
    private PlaySource playSource = PlaySource.DASHBOARD;

    /**
     * Get singleton instance
     */
    public static synchronized PlaybackManager getInstance() {
        if (instance == null) {
            instance = new PlaybackManager();
        }
        return instance;
    }

    /**
     * Private constructor - use getInstance()
     */
    private PlaybackManager() {
        this.playerController = PlayerController.getInstance();
    }

    /**
     * Set playlist and start playing song at given index
     * Updates NowPlayingState and PlayerState
     * 
     * @param songs         Full list of songs (playlist)
     * @param selectedIndex Index of song to start playing
     * @param source        Where playback was initiated from (for back button
     *                      navigation)
     */
    public synchronized void setPlaylist(List<Song> songs, int selectedIndex, PlaySource source) {
        if (songs == null || songs.isEmpty() || selectedIndex < 0 || selectedIndex >= songs.size()) {
            logger.warning("Invalid playlist or index");
            return;
        }

        this.playlist = new ArrayList<>(songs);
        this.currentIndex = selectedIndex;
        this.playSource = source;
        this.currentSong = playlist.get(currentIndex);

        // Start playback
        playerController.playSong(currentSong);

        // Update both NowPlayingState (for UI listeners) and PlayerState (for direct
        // access)
        NowPlayingState.getInstance().setCurrentSong(currentSong, true);
        PlayerState.getInstance().setCurrentSong(currentSong);
        PlayerState.getInstance().setPlaying(true);
        PlayerState.getInstance().setCurrentPosition(0);

        System.out.println(
                "[PlaybackManager] Song set to play: " + currentSong.getTitle() + " (index " + selectedIndex + ")");
        logger.info("Playlist set: " + songs.size() + " songs, starting at index " + selectedIndex +
                " from source: " + source.name());
    }

    /**
     * Play or resume the currently set song
     * Only restarts if song changed - otherwise resumes from pause position
     */
    public synchronized void play() {
        if (currentSong == null) {
            logger.warning("No song set for playback");
            return;
        }

        // If already playing this song, just resume
        if (playerController.getCurrentSong() != null &&
                playerController.getCurrentSong().equals(currentSong)) {
            playerController.resumeSong();
        } else {
            // New song - start from beginning
            playerController.playSong(currentSong);
        }

        NowPlayingState.getInstance().setPlaying(true);
        PlayerState.getInstance().setPlaying(true);
        System.out.println("[PlaybackManager] Playing: " + currentSong.getTitle());
        logger.info("Playing: " + currentSong.getTitle());
    }

    /**
     * Pause current playback
     */
    public synchronized void pause() {
        playerController.pauseSong();
        NowPlayingState.getInstance().setPlaying(false);
        PlayerState.getInstance().setPlaying(false);
        System.out.println("[PlaybackManager] Paused");
        logger.info("Paused");
    }

    /**
     * Toggle between play and pause
     */
    public synchronized void togglePlayPause() {
        if (NowPlayingState.getInstance().isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    /**
     * Play next song in playlist
     */
    public synchronized void playNext() {
        if (playlist.isEmpty() || currentIndex < 0) {
            logger.warning("Playlist empty or no current index");
            return;
        }

        // Move to next, or loop to start if at end
        currentIndex = (currentIndex + 1) % playlist.size();
        currentSong = playlist.get(currentIndex);

        play();
        logger.info("Playing next: " + currentSong.getTitle() + " (index " + currentIndex + ")");
    }

    /**
     * Play previous song in playlist
     */
    public synchronized void playPrevious() {
        if (playlist.isEmpty() || currentIndex < 0) {
            logger.warning("Playlist empty or no current index");
            return;
        }

        // Move to previous, or loop to end if at start
        currentIndex = (currentIndex - 1 + playlist.size()) % playlist.size();
        currentSong = playlist.get(currentIndex);

        play();
        logger.info("Playing previous: " + currentSong.getTitle() + " (index " + currentIndex + ")");
    }

    /**
     * Toggle loop mode on/off
     */
    public synchronized void toggleLoop() {
        isLooping = !isLooping;
        logger.info("Loop mode: " + (isLooping ? "ON" : "OFF"));
    }

    /**
     * Get current song
     */
    public synchronized Song getCurrentSong() {
        return currentSong;
    }

    /**
     * Get current playlist
     */
    public synchronized List<Song> getPlaylist() {
        return new ArrayList<>(playlist);
    }

    /**
     * Get current index in playlist
     */
    public synchronized int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Check if loop is enabled
     */
    public synchronized boolean isLooping() {
        return isLooping;
    }

    /**
     * Get the source/origin of current playback (for back button navigation)
     */
    public synchronized PlaySource getPlaySource() {
        return playSource;
    }

    /**
     * Check if given song is currently playing
     */
    public synchronized boolean isPlaying(Song song) {
        return currentSong != null && currentSong.equals(song) &&
                NowPlayingState.getInstance().isPlaying();
    }

    /**
     * Called by PlayerController when song ends
     * Handles loop or auto-play next song
     */
    public synchronized void onSongEnded() {
        if (isLooping && currentSong != null) {
            // Loop mode: replay the same song
            logger.info("Looping song: " + currentSong.getTitle());
            playerController.playSong(currentSong);
            NowPlayingState.getInstance().setPlaying(true);
        } else if (!playlist.isEmpty() && currentIndex >= 0) {
            // Auto-play next song in playlist
            playNext();
        }
    }
}
