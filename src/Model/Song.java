/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author oakin
 */
public class Song {
    private String title;
    private String artist;
    private String path;
    private String imagePath;

    public Song(String title, String artist, String path, String imagePath) {
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.imagePath = imagePath;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getPath() { return path; }
    public String getImagePath() { return imagePath; }
}
