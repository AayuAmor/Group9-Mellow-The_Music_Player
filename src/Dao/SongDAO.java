/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import Model.Song;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import utils.MetadataReader;
/**
 *
 * @author oakin
 */
public class SongDAO {
    private static final String DEFAULT_IMAGE = "/Images/default_song.png";
    private static final Set<String> SUPPORTED_EXTS = Set.of("mp3", "wav", "flac");

    /**
     * Recursively scans the provided root folder for supported audio files.
     * Uses java.nio.file for efficient traversal and MetadataReader for tags.
     * @param root user-selected root folder
     * @param reader metadata reader utility
     * @return List of discovered songs
     */
    public List<Song> scanSongs(Path root, MetadataReader reader) {
        if (root == null) return List.of();
        List<Song> songs = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(root)) {
            List<Path> audioFiles = stream
                .filter(Files::isRegularFile)
                .filter(p -> isAudio(p))
                .collect(Collectors.toList());

            for (Path p : audioFiles) {
                try {
                    Song s = reader.extract(p);
                    // preserve default image for existing UI
                    s.setImagePath(DEFAULT_IMAGE);
                    songs.add(s);
                } catch (Exception e) {
                    // Skip problematic files without failing the entire scan
                }
            }
        } catch (IOException e) {
            // Return what we have; caller can decide how to report/handle
        }
        return songs;
    }

    private boolean isAudio(Path p) {
        String name = p.getFileName().toString();
        int idx = name.lastIndexOf('.')
                ;
        if (idx <= 0) return false;
        String ext = name.substring(idx + 1).toLowerCase();
        return SUPPORTED_EXTS.contains(ext);
    }
}

    

