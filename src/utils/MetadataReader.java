package utils;

import Model.Song;
import java.io.File;
import java.nio.file.Path;

// jaudiotagger imports
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * Utility to read audio metadata using jaudiotagger.
 * Falls back to filename when fields are missing.
 */
public class MetadataReader {

    /**
     * Extracts metadata and builds a Song instance.
     * @param path audio file path
     * @return Song populated with metadata; minimal fields if extraction fails
     */
    public Song extract(Path path) {
        File file = path.toFile();
        String fileName = file.getName();
        String title = stripExtension(fileName);
        String artist = "Unknown";
        String album = "Unknown";
        int durationSeconds = 0;

        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            AudioHeader header = audioFile.getAudioHeader();
            if (header != null) {
                durationSeconds = Math.max(0, header.getTrackLength());
            }
            if (tag != null) {
                String t = tag.getFirst(FieldKey.TITLE);
                String ar = tag.getFirst(FieldKey.ARTIST);
                String al = tag.getFirst(FieldKey.ALBUM);
                if (t != null && !t.isBlank()) title = t.trim();
                if (ar != null && !ar.isBlank()) artist = ar.trim();
                if (al != null && !al.isBlank()) album = al.trim();
            }
        } catch (Exception e) {
            // Silent fallback to filename-based title
        }

        return new Song(title, artist, album, durationSeconds, path.toAbsolutePath().toString());
    }

    private String stripExtension(String name) {
        int idx = name.lastIndexOf('.')
                ;
        return idx > 0 ? name.substring(0, idx) : name;
    }
}
