package Model;

/**
 * PlaySource enum tracks which view/screen initiated playback.
 * Enables navigation context preservation - player can return to correct screen.
 * 
 * Values:
 * - DASHBOARD: User clicked song from UserDashboard
 * - ALL_SONGS: User clicked song from AllSongs view
 * - PLAYLIST: User clicked song from Playlist view
 * - LIKED_SONGS: User clicked song from LikedSong view
 * - RECOMMENDATION: User clicked from recommendation section (dashboard)
 */
public enum PlaySource {
    DASHBOARD("UserDashboard"),
    ALL_SONGS("AllSongs"),
    PLAYLIST("Playlist"),
    LIKED_SONGS("likedsong"),
    RECOMMENDATION("UserDashboard");
    
    private final String screenName;
    
    PlaySource(String screenName) {
        this.screenName = screenName;
    }
    
    /**
     * Get the associated screen/view name for this source
     */
    public String getScreenName() {
        return screenName;
    }
}
