package Controller;

import Model.Song;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import view.SongSearchView;

/**
 * Controller to orchestrate song searches for any view implementing
 * SongSearchView.
 * Keeps UI code out of the controller and uses a background executor for DB
 * work.
 */
public class SongSearchController {

    private static final Logger logger = Logger.getLogger(SongSearchController.class.getName());
    private final ExecutorService executor;

    public SongSearchController() {
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "song-search-controller");
            t.setDaemon(true);
            return t;
        });
    }

    public void searchLocal(String rawTerm, List<Song> masterSongs, SongSearchView view) {
        if (view == null) {
            return;
        }
        final String term = rawTerm == null ? "" : rawTerm.trim();
        logger.fine(() -> "Local search invoked with raw='" + rawTerm + "' normalized='" + term + "'");
        if (term.isEmpty() || "Search".equalsIgnoreCase(term)) {
            SwingUtilities.invokeLater(() -> view.showMessage("Please enter a search term"));
            return;
        }

        final List<Song> source = masterSongs == null ? List.of() : masterSongs;

        executor.submit(() -> {
            String lowerTerm = term.toLowerCase();
            List<Song> matches = new ArrayList<>();
            for (Song s : source) {
                if (s == null || s.getTitle() == null) {
                    continue;
                }
                String title = s.getTitle().toLowerCase();
                if (title.contains(lowerTerm)) {
                    matches.add(s);
                }
            }

            SwingUtilities.invokeLater(() -> {
                logger.fine(() -> "Local search matches=" + matches.size());
                if (matches.isEmpty()) {
                    view.clearSongTable();
                    view.showMessage("No matching songs found");
                } else {
                    view.updateSongTable(matches);
                }
            });
        });
    }
}
