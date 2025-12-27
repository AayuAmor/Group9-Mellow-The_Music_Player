/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Dao.SongDAO;
import Model.Song;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import service.PlayerService;
import utils.MetadataReader;

/**
 *
 * @author oakin
 */
public class SongController {
    private final SongDAO songDAO;
    private final MetadataReader metadataReader;
    private final PlayerService playerService;
    private List<Song> cachedSongs = new ArrayList<>();

    public SongController() {
        this.songDAO = new SongDAO();
        this.metadataReader = new MetadataReader();
        this.playerService = new PlayerService();
    }

    /**
     * Load songs from a user-selected root folder.
     * This method delegates scanning to the DAO and caches results for views.
     */
    public void loadSongs(Path rootFolder) {
        cachedSongs = songDAO.scanSongs(rootFolder, metadataReader);
    }

    /**
     * Provide cached songs to views for rendering.
     */
    public List<Song> getSongs() {
        return cachedSongs;
    }

    /**
     * Handle song item click in views; starts playback via PlayerService.
     */
    public void onSongSelected(Song song) {
        if (song != null && song.getFilePath() != null) {
            playerService.play(song.getFilePath());
        }
    }

    public void pause() {
        playerService.pause();
    }

    public void stop() {
        playerService.stop();
    }
}
