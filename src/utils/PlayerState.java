package utils;

import Model.PlaySource;
import Model.Song;

/**
 * Global PlayerState - Singleton Pattern
 * 
 * Maintains SINGLE source of truth for playback state across entire
 * application.
 * Prevents song restart when navigating between UI screens.
 * 
 * Key Features:
 * - currentSong: The song currently playing/paused
 * - currentPosition: Playback position in milliseconds
 * - isPlaying: Current playback state
 * - isLoop: Loop mode flag
 * - source: Where playback was initiated (DASHBOARD/PLAYLIST/LIKED/SEARCH)
 */
public class PlayerState {
    private static PlayerState instance;

    private Song currentSong;
    private long currentPosition = 0; // milliseconds
    private boolean isPlaying = false;
    private boolean isLoop = false;
    private PlaySource source = PlaySource.DASHBOARD;

    private PlayerState() {
    }

    /**
     * Get singleton instance
     */
    public static synchronized PlayerState getInstance() {
        if (instance == null) {
            instance = new PlayerState();
        }
        return instance;
    }

    /**
     * Get current song
     */
    public synchronized Song getCurrentSong() {
        return currentSong;
    }

    /**
     * Set current song
     */
    public synchronized void setCurrentSong(Song song) {
        this.currentSong = song;
    }

    /**
     * Get current playback position in milliseconds
     */
    public synchronized long getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Set current playback position in milliseconds
     */
    public synchronized void setCurrentPosition(long position) {
        this.currentPosition = position;
    }

    /**
     * Check if currently playing
     */
    public synchronized boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Set playing state
     */
    public synchronized void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    /**
     * Check if loop mode is enabled
     */
    public synchronized boolean isLoop() {
        return isLoop;
    }

    /**
     * Set loop mode
     */
    public synchronized void setLoop(boolean loop) {
        this.isLoop = loop;
    }

    /**
     * Get playback source
     */
    public synchronized PlaySource getSource() {
        return source;
    }

    /**
     * Set playback source
     */
    public synchronized void setSource(PlaySource source) {
        this.source = source;
    }

    /**
     * Reset state (use when stopping playback completely)
     */
    public synchronized void reset() {
        this.currentSong = null;
        this.currentPosition = 0;
        this.isPlaying = false;
        // Keep loop and source as they are user preferences
    }
}
