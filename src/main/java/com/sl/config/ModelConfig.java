package com.sl.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {


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

                .host("192.168.80.152")                         // Host for Milvus instance
                .port(19530)                               // Port for Milvus instance
                .collectionName(collectionName)      // Name of the collection
                .dimension(dimension)                            // Dimension of vectors
                .indexType(IndexType.FLAT)                 // Index type
                .metricType(MetricType.COSINE)             // Metric type
                .username("username")                      // Username for Milvus
                .password("password")                      // Password for Milvus
                .consistencyLevel(ConsistencyLevelEnum.EVENTUALLY)  // Consistency level
                .autoFlushOnInsert(true)                   // Auto flush after insert
                .idFieldName("id")                         // ID field name
                .textFieldName("text")                     // Text field name
                .metadataFieldName("metadata")             // Metadata field name
                .vectorFieldName("vector")                 // Vector field name
                .build();                                  // Build the MilvusEmbeddingStore instance
        return store;
    }
}
