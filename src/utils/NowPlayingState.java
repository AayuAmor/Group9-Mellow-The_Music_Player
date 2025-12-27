package utils;

import Model.PlaySource;
import Model.Song;
import java.util.ArrayList;
import java.util.List;

/**
 * Global observable state for the currently playing song.
 * Uses observer pattern to notify UI components when song changes.
 * Singleton pattern ensures only one instance exists.
 * Tracks PlaySource for navigation context preservation.
 */
public class NowPlayingState {
    private static NowPlayingState instance;
    private Song currentSong;
    private boolean isPlaying = false;
    private PlaySource playSource = PlaySource.DASHBOARD;
    private List<NowPlayingListener> listeners = new ArrayList<>();
    
    private NowPlayingState() {
    }
    
    /**
     * Get the singleton instance
     */
    public static synchronized NowPlayingState getInstance() {
        if (instance == null) {
            instance = new NowPlayingState();
        }
        return instance;
    }
    
    /**
     * Set the currently playing song and notify all listeners
     */
    public synchronized void setCurrentSong(Song song, boolean playing) {
        this.currentSong = song;
        this.isPlaying = playing;
        notifyListeners();
    }
    
    /**
     * Get the currently playing song
     */
    public synchronized Song getCurrentSong() {
        return currentSong;
    }
    
    /**
     * Convenience method for setting song without changing play state
     */
    public synchronized void setCurrentSong(Song song) {
        this.currentSong = song;
        notifyListeners();
    }
    
    /**
     * Get the current play state
     */
    public synchronized boolean isPlaying() {
        return isPlaying;
    }
    
    /**
     * Set the play state
     */
    public synchronized void setPlaying(boolean playing) {
        this.isPlaying = playing;
        notifyListeners();
    }
    
    /**
     * Get the current PlaySource (navigation context)
     */
    public synchronized PlaySource getPlaySource() {
        return playSource;
    }
    
    /**
     * Set the PlaySource (where playback was initiated from)
     */
    public synchronized void setPlaySource(PlaySource playSource) {
        this.playSource = playSource;
    }
    
    /**
     * Register a listener for now playing changes
     */
    public synchronized void addListener(NowPlayingListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Unregister a listener
     */
    public synchronized void removeListener(NowPlayingListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notify all listeners of change
     */
    private void notifyListeners() {
        for (NowPlayingListener listener : new ArrayList<>(listeners)) {
            listener.onNowPlayingChanged(currentSong, isPlaying);
        }
    }
    
    /**
     * Interface for listening to now playing changes
     */
    public interface NowPlayingListener {
        void onNowPlayingChanged(Song song, boolean isPlaying);
    }
}
