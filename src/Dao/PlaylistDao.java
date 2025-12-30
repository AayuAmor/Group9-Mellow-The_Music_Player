package Dao;

import Database.MySqlConnection;
import Model.PlaylistModel;
import Model.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Playlist operations
 * Handles all database interactions for playlists and playlist_songs tables
 */
public class PlaylistDao {

    private final MySqlConnection dbConnection = new MySqlConnection();

    /**
     * Get all playlists for a specific user
     * 
     * @param userId The user's ID
     * @return List of playlists with song counts
     */
    public List<PlaylistModel> getUserPlaylists(int userId) {
        List<PlaylistModel> playlists = new ArrayList<>();
        String query = "SELECT p.playlist_id, p.user_id, p.playlist_name, p.created_at, " +
                "COUNT(ps.song_id) AS song_count " +
                "FROM playlists p " +
                "LEFT JOIN playlist_songs ps ON p.playlist_id = ps.playlist_id " +
                "WHERE p.user_id = ? " +
                "GROUP BY p.playlist_id, p.user_id, p.playlist_name, p.created_at " +
                "ORDER BY p.created_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PlaylistModel playlist = new PlaylistModel();
                playlist.setPlaylistId(rs.getInt("playlist_id"));
                playlist.setUserId(rs.getInt("user_id"));
                playlist.setPlaylistName(rs.getString("playlist_name"));
                playlist.setCreatedAt(rs.getTimestamp("created_at"));
                playlist.setSongCount(rs.getInt("song_count"));
                playlists.add(playlist);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user playlists: " + e.getMessage());
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

        return playlists;
    }

    /**
     * Create a new playlist for a user
     * 
     * @param userId       The user's ID
     * @param playlistName The name of the playlist
     * @return The newly created playlist ID, or -1 if failed
     */
    public int createPlaylist(int userId, String playlistName) {
        String query = "INSERT INTO playlists (user_id, playlist_name, created_at) VALUES (?, ?, NOW())";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, userId);
            stmt.setString(2, playlistName);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating playlist: " + e.getMessage());
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
     * Get all songs in a specific playlist
     * 
     * @param playlistId The playlist ID
     * @return List of songs in the playlist
     */
    public List<Song> getPlaylistSongs(int playlistId) {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT s.song_id, s.song_name, s.artist, s.album, s.duration, s.file_path " +
                "FROM songs s " +
                "INNER JOIN playlist_songs ps ON s.song_id = ps.song_id " +
                "WHERE ps.playlist_id = ? " +
                "ORDER BY ps.added_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Song song = new Song(
                        rs.getString("song_name"),
                        rs.getString("artist"),
                        rs.getString("album"),
                        rs.getInt("duration"),
                        rs.getString("file_path"));
                song.setSongId(rs.getInt("song_id")); // Set database ID
                songs.add(song);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching playlist songs: " + e.getMessage());
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

    /**
     * Add a song to a playlist
     * 
     * @param playlistId The playlist ID
     * @param songId     The song ID
     * @return true if successful, false otherwise
     */
    public boolean addSongToPlaylist(int playlistId, int songId) {
        // Check if song already exists in playlist
        String checkQuery = "SELECT COUNT(*) FROM playlist_songs WHERE playlist_id = ? AND song_id = ?";
        String insertQuery = "INSERT INTO playlist_songs (playlist_id, song_id, added_at) VALUES (?, ?, NOW())";

        Connection conn = null;
        try {
            conn = dbConnection.openconnection();
            // Check if already exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, playlistId);
                checkStmt.setInt(2, songId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Song already exists in playlist");
                    return false;
                }
            }

            // Insert the song
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, playlistId);
                insertStmt.setInt(2, songId);
                int affectedRows = insertStmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error adding song to playlist: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                dbConnection.closeConnection(conn);
            }
        }

        return false;
    }

    /**
     * Remove a song from a playlist
     * 
     * @param playlistId The playlist ID
     * @param songId     The song ID
     * @return true if successful, false otherwise
     */
    public boolean removeSongFromPlaylist(int playlistId, int songId) {
        String query = "DELETE FROM playlist_songs WHERE playlist_id = ? AND song_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error removing song from playlist: " + e.getMessage());
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

        return false;
    }

    /**
     * Delete a playlist
     * 
     * @param playlistId The playlist ID
     * @return true if successful, false otherwise
     */
    public boolean deletePlaylist(int playlistId) {
        String query = "DELETE FROM playlists WHERE playlist_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, playlistId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting playlist: " + e.getMessage());
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

        return false;
    }

    /**
     * Get playlist by ID
     * 
     * @param playlistId The playlist ID
     * @return The playlist or null if not found
     */
    public PlaylistModel getPlaylistById(int playlistId) {
        String query = "SELECT p.playlist_id, p.user_id, p.playlist_name, p.created_at, " +
                "COUNT(ps.song_id) AS song_count " +
                "FROM playlists p " +
                "LEFT JOIN playlist_songs ps ON p.playlist_id = ps.playlist_id " +
                "WHERE p.playlist_id = ? " +
                "GROUP BY p.playlist_id, p.user_id, p.playlist_name, p.created_at";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                PlaylistModel playlist = new PlaylistModel();
                playlist.setPlaylistId(rs.getInt("playlist_id"));
                playlist.setUserId(rs.getInt("user_id"));
                playlist.setPlaylistName(rs.getString("playlist_name"));
                playlist.setCreatedAt(rs.getTimestamp("created_at"));
                playlist.setSongCount(rs.getInt("song_count"));
                return playlist;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching playlist: " + e.getMessage());
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

        return null;
    }
}
