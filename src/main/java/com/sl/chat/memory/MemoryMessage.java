package com.sl.chat.memory;

public class MemoryMessage {
    private String role;      // e.g., "user", "assistant", "system"
    private String content;   // message content
    private long timestamp;   // for ordering and pruning

    public MemoryMessage() {
        this.timestamp = System.currentTimeMillis();
    }

    public MemoryMessage(String role, String content) {
        this.role = role;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return role + ": " + content;
    }
}