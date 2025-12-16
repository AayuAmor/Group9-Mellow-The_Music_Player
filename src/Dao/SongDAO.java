/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Model.Song;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author oakin
 */
public class SongDAO {
        private static final String MUSIC_DIR = "C:/Users/Public/Music"; // change if needed
    private static final String DEFAULT_IMAGE = "/Images/default_song.png";

    public List<Song> fetchLocalSongs() {
        List<Song> songs = new ArrayList<>();
        scanFolder(new File(MUSIC_DIR), songs);
        return songs;
    }

    private void scanFolder(File folder, List<Song> songs) {
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanFolder(file, songs);
            } else if (file.getName().endsWith(".mp3")) {
                String title = file.getName().replace(".mp3", "");
                String artist = "Unknown";
                String path = file.getAbsolutePath();
                String image = DEFAULT_IMAGE; // Default image for now

                songs.add(new Song(title, artist, path, image));
            }
        }
    }
}

    

