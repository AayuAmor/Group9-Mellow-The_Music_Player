package utils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.SwingUtilities;

/**
 * Simple observer to notify open views when liked songs change.
 * Views register a Runnable that refreshes their UI; Player notifies after
 * like/unlike.
 */
public final class LikedSongsNotifier {

    private static final List<Runnable> listeners = new CopyOnWriteArrayList<>();

    private LikedSongsNotifier() {
    }

    public static void register(Runnable listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public static void unregister(Runnable listener) {
        listeners.remove(listener);
    }

    public static void notifyChanged() {
        SwingUtilities.invokeLater(() -> listeners.forEach(Runnable::run));
    }
}
