package com.sl.chat;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",//如果只有一个模型，此处可以不写
        chatMemoryProvider="chatMemoryProvider"
)
public interface ChatServiceGeneral {
    @SystemMessage("You are a polite assistant")//系统消息
    @UserMessage("请回答问题：{{msg}}")//用户消息：模板参数此处必须要@V中的名字一样
    String chat(@MemoryId String memoryId, @V("msg") String userMessage);
}
