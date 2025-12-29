/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Dao.SongDAO;
import Model.Song;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import utils.MetadataReader;
import utils.SongCache;

/**
 * SongController manages song loading, caching, and metadata retrieval.
 * Playback is handled by PlaybackManager and PlayerController (single source of
 * truth).
 * 
 * @author oakin
 */
public class SongController {
    private final SongDAO songDAO;
    private final MetadataReader metadataReader;
    private List<Song> cachedSongs = new ArrayList<>();

    public SongController() {
        this.songDAO = new SongDAO();
        this.metadataReader = new MetadataReader();
    }

    /**
     * Load songs from a user-selected root folder.
     * This method delegates scanning to the DAO and caches results for views.
     */
    public void loadSongs(Path rootFolder) {
        cachedSongs = songDAO.scanSongs(rootFolder, metadataReader);
    }

    /**
     * Load local songs once from the specified root path and store in SongCache.
     * This method scans local audio files via SongDAO and stores the result in
     * SongCache.
     * Should only be called once at application startup or when refreshing the song
     * library.
     * 
     * @param rootPath The root directory path to scan for audio files
     */
    public void loadLocalSongsOnce(String rootPath) {
        if (rootPath == null || rootPath.trim().isEmpty()) {
            return;
        }

        Path root = Paths.get(rootPath);
        List<Song> scannedSongs = songDAO.scanSongs(root, metadataReader);
        SongCache.setSongs(scannedSongs);
    }

    /**
     * Get all songs from SongCache.
     * 
     * @return List of all songs stored in the cache
     */
    public List<Song> getAllSongs() {
        return SongCache.getAllSongs();
    }

    /**
     * Provide cached songs to views for rendering.
     */
    public List<Song> getSongs() {
        return cachedSongs;
    }
}
