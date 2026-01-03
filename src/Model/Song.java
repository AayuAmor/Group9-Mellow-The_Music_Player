/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 * Song model class
 * Represents a song with metadata
 */
public class Song {
    private int songId; // Database ID for persistence operations
    private String title;
    private String artist;
    private String album;
    private int durationSeconds;
    private String filePath;
    // Retain optional imagePath to avoid breaking existing UI
    private String imagePath;
    // Track like state to keep song metadata in sync with UI
    private boolean isLiked = false;

    public Song(String title, String artist, String filePath, String imagePath) {
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.imagePath = imagePath;
    }

    public Song(String title, String artist, String album, int durationSeconds, String filePath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.durationSeconds = durationSeconds;
        this.filePath = filePath;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setLiked(boolean liked) {
        this.isLiked = liked;
    }
}
