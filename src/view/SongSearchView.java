package view;

import Model.Song;
import java.util.List;

/**
 * Common contract for views that render song search results.
 */
public interface SongSearchView {
    void updateSongTable(List<Song> songs);

    void clearSongTable();

    void showMessage(String message);
}
