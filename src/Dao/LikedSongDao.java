package Dao;

import Database.MySqlConnection;
import Model.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Liked Songs operations
 * Handles all database interactions for liked_songs table
 */
public class LikedSongDao {

    private final MySqlConnection dbConnection = new MySqlConnection();

    /**
     * Add a song to user's liked songs
     * 
     * @param userId The user's ID
     * @param songId The song ID
     * @return true if successful, false otherwise
     */
    public boolean likeSong(int userId, int songId) {
        // Check if already liked
        if (isSongLiked(userId, songId)) {
            System.out.println("Song already liked by user");
            return false;
        }

        String query = "INSERT INTO liked_songs (user_id, song_id, liked_at) VALUES (?, ?, NOW())";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, userId);
            stmt.setInt(2, songId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error liking song: " + e.getMessage());
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
     * Remove a song from user's liked songs
     * 
     * @param userId The user's ID
     * @param songId The song ID
     * @return true if successful, false otherwise
     */
    public boolean unlikeSong(int userId, int songId) {
        String query = "DELETE FROM liked_songs WHERE user_id = ? AND song_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, userId);
            stmt.setInt(2, songId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error unliking song: " + e.getMessage());
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
     * Check if a song is liked by user
     * 
     * @param userId The user's ID
     * @param songId The song ID
     * @return true if liked, false otherwise
     */
    public boolean isSongLiked(int userId, int songId) {
        String query = "SELECT COUNT(*) FROM liked_songs WHERE user_id = ? AND song_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, userId);
            stmt.setInt(2, songId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if song is liked: " + e.getMessage());
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
     * Get all liked songs for a user
     * 
     * @param userId The user's ID
     * @return List of liked songs
     */
    public List<Song> getLikedSongs(int userId) {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT s.song_id, s.song_name, s.artist, s.album, s.duration, s.file_path " +
                "FROM songs s " +
                "INNER JOIN liked_songs ls ON s.song_id = ls.song_id " +
                "WHERE ls.user_id = ? " +
                "ORDER BY ls.liked_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Song song = new Song(
                        rs.getString("song_name"),
                        rs.getString("artist"),
                        rs.getString("album"),
                        rs.getInt("duration"),
                        rs.getString("file_path"));
                song.setSongId(rs.getInt("song_id")); // Set database ID
                song.setLiked(true); // Mark as liked
                songs.add(song);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching liked songs: " + e.getMessage());
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
     * Get count of liked songs for a user
     * 
     * @param userId The user's ID
     * @return Number of liked songs
     */
    public int getLikedSongsCount(int userId) {
        String query = "SELECT COUNT(*) FROM liked_songs WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.openconnection();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting liked songs: " + e.getMessage());
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

        return 0;
    }
}
