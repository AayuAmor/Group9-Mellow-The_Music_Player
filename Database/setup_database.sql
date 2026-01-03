-- =====================================================
-- COMPLETE DATABASE SETUP FOR MELLOW MUSIC PLAYER
-- =====================================================

DROP DATABASE IF EXISTS mellow;
CREATE DATABASE mellow;
USE mellow;

-- =====================================================
-- USERS TABLE
-- =====================================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Sample users
INSERT INTO users (username, email, password, role) VALUES
('admin', 'admin@mellow.com', 'admin123', 'admin'),
('testuser', 'user@mellow.com', 'user123', 'user');

-- =====================================================
-- SONGS TABLE
-- =====================================================
CREATE TABLE songs (
    song_id INT AUTO_INCREMENT PRIMARY KEY,
    song_name VARCHAR(150) NOT NULL,
    artist VARCHAR(100),
    album VARCHAR(100),
    duration INT, -- duration in seconds
    file_path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Sample songs
INSERT INTO songs (song_name, artist, album, duration, file_path) VALUES
('Blinding Lights', 'The Weeknd', 'After Hours', 200, '/music/blinding_lights.mp3'),
('Believer', 'Imagine Dragons', 'Evolve', 204, '/music/believer.mp3'),
('Perfect', 'Ed Sheeran', 'Divide', 263, '/music/perfect.mp3');

-- =====================================================
-- PLAYLISTS TABLE
-- =====================================================
CREATE TABLE playlists (
    playlist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    playlist_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE,

    INDEX idx_user_playlists (user_id)
) ENGINE=InnoDB;

-- Sample playlist
INSERT INTO playlists (user_id, playlist_name)
VALUES (2, 'My Favorites');

-- =====================================================
-- PLAYLIST_SONGS (JUNCTION TABLE)
-- =====================================================
CREATE TABLE playlist_songs (
    playlist_id INT NOT NULL,
    song_id INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (playlist_id, song_id),

    FOREIGN KEY (playlist_id) REFERENCES playlists(playlist_id)
        ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id)
        ON DELETE CASCADE,

    INDEX idx_playlist_songs (playlist_id),
    INDEX idx_song_playlists (song_id)
) ENGINE=InnoDB;

-- Add sample songs to playlist
INSERT INTO playlist_songs (playlist_id, song_id) VALUES
(1, 1),
(1, 2);

-- =====================================================
-- LIKED SONGS TABLE
-- =====================================================
CREATE TABLE liked_songs (
    like_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    song_id INT NOT NULL,
    liked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE KEY unique_user_song (user_id, song_id),

    FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id)
        ON DELETE CASCADE,

    INDEX idx_user_likes (user_id),
    INDEX idx_song_likes (song_id)
) ENGINE=InnoDB;

-- Sample liked song
INSERT INTO liked_songs (user_id, song_id)
VALUES (2, 3);

-- =====================================================
-- VERIFY DATA
-- =====================================================
SELECT * FROM users;
SELECT * FROM songs;
SELECT * FROM playlists;
SELECT * FROM playlist_songs;
SELECT * FROM liked_songs;

SELECT 'Mellow Database Setup Completed Successfully!' AS STATUS;
