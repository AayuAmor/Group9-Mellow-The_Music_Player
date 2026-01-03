package service;

import Dao.SongDAO;
import Model.Song;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;

/**
 * Shared search service for song lookups across all views.
 * - Uses a single-threaded executor to avoid hammering the DB with concurrent
 * requests.
 * - Caches recent queries to prevent duplicate calls for the same term.
 * - Always marshals UI callbacks onto the EDT via SwingUtilities.invokeLater.
 */
public class SearchService {

    private static final SearchService INSTANCE = new SearchService();

    private final SongDAO songDao = new SongDAO();
    private final Map<String, List<Song>> cache = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "song-search-thread");
        t.setDaemon(true);
        return t;
    });

    private SearchService() {
    }

    public static SearchService getInstance() {
        return INSTANCE;
    }

    /**
     * Perform an async search and deliver results back on the EDT.
     * Empty or blank terms return all songs from the DB.
     */
    public void searchSongs(String rawTerm, Consumer<List<Song>> onSuccess, Consumer<Exception> onError) {
        final String term = rawTerm == null ? "" : rawTerm.trim();

        // Serve cached result if available to avoid duplicate DB calls
        List<Song> cached = cache.get(term.toLowerCase());
        if (cached != null) {
            SwingUtilities.invokeLater(() -> onSuccess.accept(cached));
            return;
        }

        executor.submit(() -> {
            try {
                List<Song> results = songDao.searchSongs(term);
                cache.put(term.toLowerCase(), results);
                SwingUtilities.invokeLater(() -> onSuccess.accept(results));
            } catch (Exception ex) {
                if (onError != null) {
                    SwingUtilities.invokeLater(() -> onError.accept(ex));
                }
            }
        });
    }

    /**
     * Clear cached search results (useful after significant DB changes).
     */
    public void clearCache() {
        cache.clear();
    }

    /**
     * Synchronous fallback for callers that already run off the EDT.
     */
    public List<Song> searchSongsSync(String rawTerm) {
        final String term = rawTerm == null ? "" : rawTerm.trim();
        List<Song> cached = cache.get(term.toLowerCase());
        if (cached != null) {
            return cached;
        }
        List<Song> results = songDao.searchSongs(term);
        cache.put(term.toLowerCase(), results);
        return Collections.unmodifiableList(results);
    }
}
