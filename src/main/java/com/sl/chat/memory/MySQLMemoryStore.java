package com.sl.chat.memory;

import com.sl.config.ModelConfig;
import com.sl.entity.AgentMemory;
import com.sl.entity.AgentMemoryExample;
import com.sl.entity.User;
import com.sl.mapper.AgentMemoryMapper;
import com.vaadin.flow.server.VaadinSession;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MySQLMemoryStore implements ChatMemoryStore {
    private AgentMemoryMapper chatMemoryMapper;
    private int maxMessages = 10;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        // TODO: Implement getting all messages from the persistent store by memory ID.
        // ChatMessageDeserializer.messageFromJson(String) and
        // ChatMessageDeserializer.messagesFromJson(String) helper methods can be used to
        // easily deserialize chat messages from JSON.
        // 构造查询条件，根据sessionId查找聊天记录并按ID升序排序
        ApplicationContext context = ModelConfig.appcationContext;
        this.chatMemoryMapper = context.getBean(AgentMemoryMapper.class);
        AgentMemoryExample example = new AgentMemoryExample();
        example.createCriteria().andSessionIdEqualTo(memoryId.toString());
        example.setOrderByClause("session_id ASC"); // 假设存在自增ID列用于排序
        List<AgentMemory> chatMemories = chatMemoryMapper.selectByExampleWithBLOBs(example);
        if(chatMemories.isEmpty()){
            return new ArrayList<>();
        }
        AgentMemory chatMemory = chatMemories.getFirst();
        // 转换ChatMemory实体为MemoryMessage对象
        return ChatMessageDeserializer.messagesFromJson(chatMemory.getContent());
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        // TODO: Implement updating all messages in the persistent store by memory ID.
        // ChatMessageSerializer.messageToJson(ChatMessage) and
        // ChatMessageSerializer.messagesToJson(List<ChatMessage>) helper methods can be used to
        // easily serialize chat messages into JSON.
        ApplicationContext context = ModelConfig.appcationContext;
        this.chatMemoryMapper = context.getBean(AgentMemoryMapper.class);
        User currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        String json = ChatMessageSerializer.messagesToJson(messages);
        AgentMemory chatMemory = new AgentMemory();
        chatMemory.setSessionId(memoryId.toString());
        chatMemory.setContent(json);
        chatMemory.setUserId(currentUser.getUserId());
        chatMemoryMapper.updateByPrimaryKeyWithBLOBs(chatMemory);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        ApplicationContext context = ModelConfig.appcationContext;
        this.chatMemoryMapper = context.getBean(AgentMemoryMapper.class);
        // TODO: Implement deleting all messages in the persistent store by memory ID.
        User currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        AgentMemoryExample example = new AgentMemoryExample();
        example.createCriteria().andUserIdEqualTo(currentUser.getUserId()).andSessionIdEqualTo(memoryId.toString());
        chatMemoryMapper.deleteByExample(example);
    }

}