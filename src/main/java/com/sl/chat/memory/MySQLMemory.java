package com.sl.chat.memory;

import java.util.*;
import java.sql.Timestamp;

import com.sl.config.ModelConfig;
import com.sl.entity.ChatMemory;
import com.sl.entity.ChatMemoryExample;
import com.sl.mapper.ChatMemoryMapper;
import org.springframework.context.ApplicationContext;

public class MySQLMemory implements Memory {
    private ChatMemoryMapper chatMemoryMapper;
    private String sessionId;
    private int maxMessages = 10;

    public MySQLMemory() {
        ApplicationContext context = ModelConfig.appcationContext;
        this.chatMemoryMapper = context.getBean(ChatMemoryMapper.class);
    }

    public MySQLMemory(String sessionId) {
        this.sessionId = sessionId;
        ApplicationContext context = ModelConfig.appcationContext;
        this.chatMemoryMapper = context.getBean(ChatMemoryMapper.class);
    }

    @Override
    public void add(String role, String content) {
        ChatMemory chatMemory = new ChatMemory();
        chatMemory.setSessionId(sessionId);
        chatMemory.setRole(role);
        chatMemory.setContent(content);
        chatMemoryMapper.insert(chatMemory);
        pruneToMaxSize();
    }

    @Override
    public List<MemoryMessage> getMessages() {
        ChatMemoryExample example = new ChatMemoryExample();
        example.createCriteria().andSessionIdEqualTo(sessionId);
        example.setOrderByClause("id ASC"); // Assuming there's an auto-incrementing ID column for ordering
        List<ChatMemory> chatMemories = chatMemoryMapper.selectByExampleWithBLOBs(example);
        
        List<MemoryMessage> result = new ArrayList<>();
        for (ChatMemory chatMemory : chatMemories) {
            MemoryMessage msg = new MemoryMessage();
            msg.setRole(chatMemory.getRole());
            msg.setContent(chatMemory.getContent());
            // Using a simple approach for timestamp, as the current ChatMemory entity doesn't have timestamp field
            msg.setTimestamp(System.currentTimeMillis());
            result.add(msg);
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
        ChatMemoryExample example = new ChatMemoryExample();
        example.createCriteria().andSessionIdEqualTo(sessionId);
        chatMemoryMapper.deleteByExample(example);
    }

    @Override
    public void setMaxMessages(int maxMessages) {
        this.maxMessages = maxMessages;
        pruneToMaxSize();
    }

    @Override
    public int size() {
        ChatMemoryExample example = new ChatMemoryExample();
        example.createCriteria().andSessionIdEqualTo(sessionId);
        List<ChatMemory> chatMemories = chatMemoryMapper.selectByExample(example);
        return chatMemories.size();
    }

    /**
     * Prunes the memory to the maximum allowed size.
     */
    private void pruneToMaxSize() {
        if (maxMessages <= 0) return;
//        去除旧的消息
                ChatMemoryExample example = new ChatMemoryExample();
                example.createCriteria().andSessionIdEqualTo(sessionId);
                List<ChatMemory> chatMemories = chatMemoryMapper.selectByExample(example);
                if (chatMemories.size() > maxMessages) {
                    chatMemoryMapper.deleteByExample(example);
//                    将最新的maxMessages条消息插入数据库
                        for (int i = chatMemories.size() - maxMessages; i < chatMemories.size(); i++) {
                            chatMemoryMapper.insert(chatMemories.get(i));
                        }
                }
        // Note: This implementation assumes there's an auto-incrementing ID column in the chat_memory table
        // which is not present in the current ChatMemory entity. 
        // A proper implementation would require either:
        // 1. Adding an ID field to the ChatMemory entity
        // 2. Or using a different approach to track and limit message count
    }

    @Override
    public void close() {
        // No need to close resources as MyBatis handles connection management
    }
}