package Dao;

import Database.MySqlConnection;
import java.sql.*;
import java.time.Instant;

public class PasswordResetDao {
    private final MySqlConnection mysql = new MySqlConnection() {};

    public PasswordResetDao() {
        ensureTable();
    }

    private void ensureTable() {
        String sql = "CREATE TABLE IF NOT EXISTS password_reset_tokens (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "email VARCHAR(100) NOT NULL, " +
                "otp VARCHAR(10) NOT NULL, " +
                "expires_at DATETIME NOT NULL, " +
                "used TINYINT(1) DEFAULT 0, " +
                "attempts INT DEFAULT 0, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "INDEX idx_email_created (email, created_at)" +
                ")";
        try (Connection conn = mysql.openconnection(); Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException ignored) {
        }
    }

    public boolean createToken(String email, String otp, Instant expiresAt) {
        String sql = "INSERT INTO password_reset_tokens (email, otp, expires_at, used, attempts) VALUES (?, ?, ?, 0, 0)";
        try (Connection conn = mysql.openconnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, otp);
            ps.setTimestamp(3, Timestamp.from(expiresAt));
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean verifyToken(String email, String otp) {
        String select = "SELECT id, expires_at, used, attempts FROM password_reset_tokens WHERE email = ? AND otp = ? ORDER BY created_at DESC LIMIT 1";
        try (Connection conn = mysql.openconnection(); PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setString(1, email);
            ps.setString(2, otp);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    incrementLatestAttempts(conn, email);
                    return false;
                }
                int id = rs.getInt("id");
                Timestamp expiresAt = rs.getTimestamp("expires_at");
                boolean used = rs.getInt("used") == 1;
                if (used || expiresAt.toInstant().isBefore(Instant.now())) {
                    incrementAttempts(conn, id);
                    return false;
                }
                try (PreparedStatement upd = conn.prepareStatement("UPDATE password_reset_tokens SET used = 1 WHERE id = ?")) {
                    upd.setInt(1, id);
                    upd.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    private void incrementAttempts(Connection conn, int id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE password_reset_tokens SET attempts = attempts + 1 WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ignored) {}
    }

    private void incrementLatestAttempts(Connection conn, String email) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE password_reset_tokens SET attempts = attempts + 1 WHERE id = (SELECT id FROM password_reset_tokens WHERE email = ? ORDER BY created_at DESC LIMIT 1)")) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (SQLException ignored) {}
    }

    public void deleteExpired() {
        String sql = "DELETE FROM password_reset_tokens WHERE used = 1 OR expires_at < NOW()";
        try (Connection conn = mysql.openconnection(); Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException ignored) {}
    }
}
