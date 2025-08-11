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

    /**
     * 获取当前会话的所有消息记录，并将其转换为MemoryMessage对象列表。
     *
     * 该方法通过sessionId查询ChatMemory表中相关的聊天记录，按照ID升序排列，
     * 然后将每条记录转换为MemoryMessage对象，其中角色和内容从ChatMemory中获取，
     * 时间戳使用系统当前时间。
     *
     * @return 包含所有消息的MemoryMessage列表，每个元素包含角色、内容和时间戳
     */
    @Override
    public List<MemoryMessage> getMessages() {
        // 构造查询条件，根据sessionId查找聊天记录并按ID升序排序
        ChatMemoryExample example = new ChatMemoryExample();
        example.createCriteria().andSessionIdEqualTo(sessionId);
        example.setOrderByClause("id ASC"); // 假设存在自增ID列用于排序
        List<ChatMemory> chatMemories = chatMemoryMapper.selectByExampleWithBLOBs(example);

        // 转换ChatMemory实体为MemoryMessage对象
        List<MemoryMessage> result = new ArrayList<>();
        for (ChatMemory chatMemory : chatMemories) {
            MemoryMessage msg = new MemoryMessage();
            msg.setRole(chatMemory.getRole());
            msg.setContent(chatMemory.getContent());
            // 当前ChatMemory实体没有时间戳字段，因此使用系统当前时间作为替代
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