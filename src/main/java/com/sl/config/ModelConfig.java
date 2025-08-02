package com.sl.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig implements ApplicationContextAware {

    public static ApplicationContext appcationContext;

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        ChatMemoryProvider chatMemoryProvider =new ChatMemoryProvider() {
            @Override
            public ChatMemory get(Object memoryId) {
                return MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .chatMemoryStore(new InMemoryChatMemoryStore())
                        .maxMessages(10)
                        .build();
            }
        };
        return chatMemoryProvider;
    }

    /**
     * 基于milvus的向量存储对象
     * @return
     */

    public static MilvusEmbeddingStore milvusEmbeddingStore(String collectionName, Integer dimension){
        MilvusEmbeddingStore store = MilvusEmbeddingStore.builder()

                .host(appcationContext.getEnvironment().getProperty("working.ai.milvus.host"))                         // Host for Milvus instance
                .port(Integer.parseInt(appcationContext.getEnvironment().getProperty("working.ai.milvus.port")))                               // Port for Milvus instance
                .collectionName(collectionName)      // Name of the collection
                .dimension(dimension)                            // Dimension of vectors
                .indexType(IndexType.FLAT)                 // Index type
                .metricType(MetricType.COSINE)             // Metric type
                .username(appcationContext.getEnvironment().getProperty("working.ai.milvus.username"))                      // Username for Milvus
                .password(appcationContext.getEnvironment().getProperty("working.ai.milvus.password"))                      // Password for Milvus
                .consistencyLevel(ConsistencyLevelEnum.EVENTUALLY)  // Consistency level
                .autoFlushOnInsert(true)                   // Auto flush after insert
                .idFieldName("id")                         // ID field name
                .textFieldName("text")                     // Text field name
                .metadataFieldName("metadata")             // Metadata field name
                .vectorFieldName("vector")                 // Vector field name
                .build();                                  // Build the MilvusEmbeddingStore instance
        return store;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appcationContext = applicationContext;
    }
}
