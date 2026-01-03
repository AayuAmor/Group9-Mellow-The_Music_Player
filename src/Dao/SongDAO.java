/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Database.MySqlConnection;
import Model.Song;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
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
    private static final Logger logger = Logger.getLogger(SongDAO.class.getName());
    private final MySqlConnection dbConnection = new MySqlConnection();

    /**
     * Recursively scans the provided root folder for supported audio files.
     * Uses java.nio.file for efficient traversal and MetadataReader for tags.
     * 
     * @param root   user-selected root folder
     * @param reader metadata reader utility
     * @return List of discovered songs
     */
    public List<Song> scanSongs(Path root, MetadataReader reader) {
        if (root == null)
            return List.of();
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
        int idx = name.lastIndexOf('.');
        if (idx <= 0)
            return false;
        String ext = name.substring(idx + 1).toLowerCase();
        return SUPPORTED_EXTS.contains(ext);
    }

    /**
     * Check if song exists in database by file path
     * 
     * @param filePath The song file path
     * @return song_id if exists, -1 if not found
     */
    public int getSongIdByFilePath(String filePath) {
        String query = "SELECT song_id FROM songs WHERE file_path = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, filePath);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("song_id");
            }
        } catch (SQLException e) {
            System.err.println("Error checking song existence: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    dbConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    /**
     * Insert song into database if it doesn't exist
     * 
     * @param song The song to insert
     * @return The song_id (existing or newly created), or -1 if failed
     */
    public int insertSongIfNotExists(Song song) {
        // First check if song exists
        int existingId = getSongIdByFilePath(song.getFilePath());
        if (existingId > 0) {
            return existingId;
        }

        // Insert new song
        String query = "INSERT INTO songs (song_name, artist, album, duration, file_path) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, song.getTitle());
            stmt.setString(2, song.getArtist());
            stmt.setString(3, song.getAlbum() != null ? song.getAlbum() : "Unknown");
            stmt.setInt(4, song.getDurationSeconds());
            stmt.setString(5, song.getFilePath());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    System.out.println("Inserted new song: " + song.getTitle() + " with ID: " + newId);
                    return newId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting song: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    dbConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    /**
     * Search songs by name or artist
     * 
     * @param searchTerm The search term
     * @return List of matching songs
     */
    public List<Song> searchSongs(String searchTerm) {
        List<Song> songs = new ArrayList<>();
        String term = searchTerm == null ? "" : searchTerm.trim().toLowerCase();
        if (term.isEmpty()) {
            return songs;
        }

        logger.fine(() -> "DAO search term='" + term + "'");

        String[] words = term.split("\\s+");
        List<String> filtered = new ArrayList<>();
        for (String w : words) {
            if (!w.isBlank()) {
                filtered.add(w);
            }
        }
        if (filtered.isEmpty()) {
            return songs;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT song_id, song_name, artist, album, duration, file_path FROM songs WHERE ");
        for (int i = 0; i < filtered.size(); i++) {
            if (i > 0) {
                sql.append(" AND ");
            }
            sql.append("(LOWER(song_name) LIKE ? OR LOWER(artist) LIKE ? OR LOWER(album) LIKE ?)");
        }
        sql.append(" ORDER BY song_name");

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            for (String w : filtered) {
                String like = "%" + w + "%";
                stmt.setString(paramIndex++, like);
                stmt.setString(paramIndex++, like);
                stmt.setString(paramIndex++, like);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Song song = new Song(
                        rs.getString("song_name"),
                        rs.getString("artist"),
                        rs.getString("album"),
                        rs.getInt("duration"),
                        rs.getString("file_path"));
                song.setSongId(rs.getInt("song_id"));
                songs.add(song);
            }
            logger.fine(() -> "DAO rows fetched=" + songs.size());
        } catch (SQLException e) {
            System.err.println("Error searching songs: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    dbConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return songs;
    }
}