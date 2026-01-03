package Model;

import java.sql.Timestamp;

/**
 * Model class representing a playlist
 * Maps to 'playlists' table in database
 */
public class PlaylistModel {
    private int playlistId;
    private int userId;
    private String playlistName;
    private Timestamp createdAt;
    private int songCount; // Derived field for display

    public PlaylistModel() {
    }

    public PlaylistModel(int playlistId, int userId, String playlistName, Timestamp createdAt) {
        this.playlistId = playlistId;
        this.userId = userId;
        this.playlistName = playlistName;
        this.createdAt = createdAt;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }
}
