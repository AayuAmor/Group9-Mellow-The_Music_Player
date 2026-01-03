package service;

import Dao.PlaylistDao;
import Model.PlaylistModel;
import Model.UserSession;
import java.util.ArrayList;
import java.util.List;

/**
 * PlaylistManager - Singleton cache for user playlists
 * 
 * Manages playlist data in memory to avoid redundant DB queries.
 * Fetches playlists ONCE per session and refreshes cache only after CRUD
 * operations.
 * 
 * Usage:
 * - PlaylistManager.getInstance().getPlaylists() - returns cached playlists
 * - PlaylistManager.getInstance().refreshCache() - reloads from DB
 */
public class PlaylistManager {

    private static PlaylistManager instance;
    private final PlaylistDao playlistDao;
    private List<PlaylistModel> cachedPlaylists;
    private boolean isCacheLoaded = false;

    private PlaylistManager() {
        this.playlistDao = new PlaylistDao();
        this.cachedPlaylists = new ArrayList<>();
    }

    /**
     * Get singleton instance
     */
    public static synchronized PlaylistManager getInstance() {
        if (instance == null) {
            instance = new PlaylistManager();
        }
        return instance;
    }

    /**
     * Get all playlists for current user (from cache)
     * Loads from DB if cache is empty
     * 
     * @return List of user's playlists
     */
    public List<PlaylistModel> getPlaylists() {
        if (!isCacheLoaded) {
            refreshCache();
        }
        return new ArrayList<>(cachedPlaylists); // Return copy to prevent external modification
    }

    /**
     * Refresh cache by fetching playlists from database
     * Call this after create/update/delete operations
     */
    public void refreshCache() {
        int userId = UserSession.getInstance().getUserId();
        cachedPlaylists = playlistDao.getUserPlaylists(userId);
        isCacheLoaded = true;
        System.out.println("[PlaylistManager] Cache refreshed: " + cachedPlaylists.size() + " playlists loaded");
    }

    /**
     * Add a newly created playlist to cache
     * 
     * @param playlist The playlist to cache
     */
    public void cachePlaylist(PlaylistModel playlist) {
        cachedPlaylists.add(playlist);
        System.out.println("[PlaylistManager] Playlist cached: " + playlist.getPlaylistName());
    }

    /**
     * Clear cache (useful for logout or user switch)
     */
    public void clearCache() {
        cachedPlaylists.clear();
        isCacheLoaded = false;
        System.out.println("[PlaylistManager] Cache cleared");
    }

    /**
     * Get a specific playlist by ID
     * 
     * @param playlistId The playlist ID
     * @return The playlist, or null if not found
     */
    public PlaylistModel getPlaylistById(int playlistId) {
        for (PlaylistModel playlist : getPlaylists()) {
            if (playlist.getPlaylistId() == playlistId) {
                return playlist;
            }
        }
        return null;
    }
}
