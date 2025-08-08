package com.sl.chat.memory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryMemory implements Memory {
    private final List<MemoryMessage> messages;
    private int maxMessages = 10; // 默认保留最近10条

    public InMemoryMemory() {
        this.messages = new CopyOnWriteArrayList<>(); // 线程安全
    }

    public InMemoryMemory(int maxMessages) {
        this.messages = new CopyOnWriteArrayList<>();
        this.maxMessages = maxMessages;
    }

    @Override
    public void add(String role, String content) {
        messages.add(new MemoryMessage(role, content));
        pruneToMaxSize();
    }

    @Override
    public List<MemoryMessage> getMessages() {
        return new ArrayList<>(messages); // 返回副本
    }

    @Override
    public String format() {
        return messages.stream()
                .map(MemoryMessage::toString)
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");
    }

    @Override
    public void clear() {
        messages.clear();
    }

    @Override
    public void setMaxMessages(int maxMessages) {
        this.maxMessages = maxMessages;
        pruneToMaxSize();
    }

    @Override
    public int size() {
        return messages.size();
    }

    // 保留最近 maxMessages 条
    private void pruneToMaxSize() {
        if (maxMessages <= 0) return;
        if (messages.size() <= maxMessages) return;

        // 按时间排序（升序），保留最新的 N 条
        messages.sort(Comparator.comparingLong(MemoryMessage::getTimestamp));
        int removeCount = messages.size() - maxMessages;
        for (int i = 0; i < removeCount; i++) {
            messages.remove(0);
        }
    }
}