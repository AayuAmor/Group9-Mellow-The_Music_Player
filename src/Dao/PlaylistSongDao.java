package Dao;

import Database.MySqlConnection;
import Model.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Playlist-Song relationships
 * Handles operations on playlist_songs table
 */
public class PlaylistSongDao {

    private final MySqlConnection dbConnection = new MySqlConnection();

    /**
     * Get all songs in a specific playlist
     * 
     * @param playlistId The playlist ID
     * @return List of songs with full metadata
     */
    public List<Song> getSongsByPlaylistId(int playlistId) {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT s.song_id, s.song_name, s.artist, s.album, " +
                "s.file_path, s.duration " +
                "FROM playlist_songs ps " +
                "INNER JOIN songs s ON ps.song_id = s.song_id " +
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
                song.setSongId(rs.getInt("song_id"));
                songs.add(song);
            }

            System.out.println("[PlaylistSongDao] Loaded " + songs.size() + " songs for playlist " + playlistId);

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
     * Remove a song from a playlist
     * 
     * @param playlistId The playlist ID
     * @param songId     The song ID
     * @return true if deleted successfully
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

            if (affectedRows > 0) {
                System.out.println("[PlaylistSongDao] Removed song " + songId + " from playlist " + playlistId);
                return true;
            }

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
     * Add a song to a playlist
     * 
     * @param playlistId The playlist ID
     * @param songId     The song ID
     * @return true if added successfully
     */
    public boolean addSongToPlaylist(int playlistId, int songId) {
        String query = "INSERT INTO playlist_songs (playlist_id, song_id, added_at) VALUES (?, ?, NOW())";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("[PlaylistSongDao] Added song " + songId + " to playlist " + playlistId);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error adding song to playlist: " + e.getMessage());
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
     * Check if a song already exists in a playlist
     * 
     * @param playlistId The playlist ID
     * @param songId     The song ID
     * @return true if song is in playlist
     */
    public boolean isSongInPlaylist(int playlistId, int songId) {
        String query = "SELECT COUNT(*) FROM playlist_songs WHERE playlist_id = ? AND song_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking song in playlist: " + e.getMessage());
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
}
