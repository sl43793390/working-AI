package com.sl.chat.agent;

import com.sl.chat.tool.WeatherTool;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAgentFactory {

    @Resource
    private OpenAiChatModel openAiChatModel;
    @Resource
    private ChatMemoryProvider chatMemoryProvider;

    public UntypedAgent createAgent(String userMessage) {
        return AgenticServices.agentBuilder().chatModel(openAiChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .systemMessage("你是一个专业的助手，能够帮助用户完美的解决他的问题。")
                .userMessage(userMessage)
                .tools(new WeatherTool())
                .name("MyChatAgent")
                .build();
    }
}
