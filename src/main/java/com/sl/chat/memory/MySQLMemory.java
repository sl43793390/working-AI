package com.sl.chat.memory;

import java.sql.*;
import java.util.*;

import com.sl.config.ModelConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.activation.DataSource;

public class MySQLMemory implements Memory {
    private HikariDataSource dataSource;
    private String sessionId;
    private int maxMessages = 10;

    public MySQLMemory() {
        this.dataSource  = ModelConfig.appcationContext.getBean(HikariDataSource.class);
    }

    public MySQLMemory(String sessionId) {
        this.sessionId = sessionId;
        this.dataSource  = ModelConfig.appcationContext.getBean(HikariDataSource.class);
    }

    @Override
    public void add(String role, String content) {
        String sql = "INSERT INTO agent_memory (session_id, role, content, timestamp) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            long timestamp = System.currentTimeMillis();
            ps.setString(1, sessionId);
            ps.setString(2, role);
            ps.setString(3, content);
            ps.setLong(4, timestamp);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add memory", e);
        }
        pruneToMaxSize();
    }

    @Override
    public List<MemoryMessage> getMessages() {
        String sql = "SELECT role, content, timestamp FROM agent_memory WHERE session_id = ? ORDER BY timestamp ASC";
        List<MemoryMessage> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MemoryMessage msg = new MemoryMessage();
                    msg.setRole(rs.getString("role"));
                    msg.setContent(rs.getString("content"));
                    msg.setTimestamp(rs.getLong("timestamp"));
                    result.add(msg);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to query memory", e);
        }
        return result;
    }

    @Override
    public String format() {
        return getMessages().stream()
                .map(MemoryMessage::toString)
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");
    }

    @Override
    public void clear() {
        String sql = "DELETE FROM agent_memory WHERE session_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear memory", e);
        }
    }

    @Override
    public void setMaxMessages(int maxMessages) {
        this.maxMessages = maxMessages;
        pruneToMaxSize();
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM agent_memory WHERE session_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count memory", e);
        }
        return 0;
    }

    private void pruneToMaxSize() {
        if (maxMessages <= 0) return;

        List<Long> idsToDelete = new ArrayList<>();
        String selectSql = """
            SELECT id FROM (
                SELECT id FROM agent_memory 
                WHERE session_id = ? 
                ORDER BY timestamp DESC 
                LIMIT ? OFFSET ?
            ) AS to_delete ORDER BY id
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(selectSql)) {
            ps.setString(1, sessionId);
            ps.setInt(2, 100); // 最多查100条旧记录
            ps.setInt(3, maxMessages); // 跳过最新的 N 条

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    idsToDelete.add(rs.getLong("id"));
                }
            }

            if (!idsToDelete.isEmpty()) {
                StringBuilder deleteSql = new StringBuilder("DELETE FROM agent_memory WHERE id IN (");
                for (int i = 0; i < idsToDelete.size(); i++) {
                    if (i > 0) deleteSql.append(",");
                    deleteSql.append("?");
                }
                deleteSql.append(")");
                try (PreparedStatement deletePs = conn.prepareStatement(deleteSql.toString())) {
                    for (int i = 0; i < idsToDelete.size(); i++) {
                        deletePs.setLong(i + 1, idsToDelete.get(i));
                    }
                    deletePs.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to prune memory", e);
        }
    }

    @Override
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}