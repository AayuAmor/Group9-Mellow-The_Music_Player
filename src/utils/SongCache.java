package utils;

import Model.Song;
import java.util.ArrayList;
import java.util.List;

/**
 * SongCache - Utility class to store all scanned local songs in memory
 * Acts as a shared in-memory model for all views
 * This class does not scan files or touch UI
 */
public class SongCache {
    private static List<Song> songs = new ArrayList<>();

    /**
     * Get all songs currently stored in cache
     * @return List of all songs in cache
     */
    public static List<Song> getAllSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Set all songs in the cache (replaces existing songs)
     * @param songList List of songs to store
     */
    public static void setSongs(List<Song> songList) {
        if (songList != null) {
            songs = new ArrayList<>(songList);
        } else {
            songs.clear();
        }
    }

    /**
     * Add a single song to the cache
     * @param song Song to add
     */
    public static void addSong(Song song) {
        if (song != null && !songs.contains(song)) {
            songs.add(song);
        }
    }

    /**
     * Add multiple songs to the cache
     * @param songList List of songs to add
     */
    public static void addSongs(List<Song> songList) {
        if (songList != null) {
            for (Song song : songList) {
                if (!songs.contains(song)) {
                    songs.add(song);
                }
            }
        }
    }

    /**
     * Remove a song from the cache
     * @param song Song to remove
     * @return true if song was removed, false otherwise
     */
    public static boolean removeSong(Song song) {
        return songs.remove(song);
    }

    /**
     * Clear all songs from the cache
     */
    public static void clearCache() {
        songs.clear();
    }

    /**
     * Get the total number of songs in cache
     * @return Number of songs
     */
    public static int getSize() {
        return songs.size();
    }

    /**
     * Check if cache is empty
     * @return true if cache is empty, false otherwise
     */
    public static boolean isEmpty() {
        return songs.isEmpty();
    }

    /**
     * Check if a song exists in the cache
     * @param song Song to check
     * @return true if song exists, false otherwise
     */
    public static boolean contains(Song song) {
        return songs.contains(song);
    }
}
