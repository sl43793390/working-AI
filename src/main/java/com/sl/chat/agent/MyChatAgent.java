package com.sl.chat.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface MyChatAgent {

//    @UserMessage("""
//            You are a creative writer.
//            Generate a draft of a story no more than
//            3 sentences long around the given topic.
//            Return only the story and nothing else.
//            The topic is {{topic}}.
//            """)
//    @Agent("Generates a story based on the given topic")
//    @Agent(outputKey = "story", description = "Generates a story based on the given topic")
    String chat(@MemoryId String memoryId, @V("topic") String topic);
}
