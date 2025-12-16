/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Dao.SongDAO;
import Model.Song;
import java.util.List;

/**
 *
 * @author oakin
 */
public class SongController {
    private SongDAO songDAO;

    public SongController() {
        songDAO = new SongDAO();
    }

    public List<Song> getAllLocalSongs() {
        return songDAO.fetchLocalSongs();
    }

    
}
