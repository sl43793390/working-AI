package com.sl.config;

import com.sl.chat.memory.MySQLMemoryStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import jakarta.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig implements ApplicationContextAware {

    public static ApplicationContext appcationContext;

    @Resource
    private MySQLMemoryStore ChatMemoryStore;
//    @Bean
//    public ChatMemoryProvider chatMemoryProvider() {
//        ChatMemoryProvider chatMemoryProvider =new ChatMemoryProvider() {
//            @Override
//            public ChatMemory get(Object memoryId) {
//                return MessageWindowChatMemory.builder()
//                        .id(memoryId)
//                        .chatMemoryStore(new InMemoryChatMemoryStore())
//                        .maxMessages(10)
//                        .build();
//            }
//        };
//        return chatMemoryProvider;
//    }
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId ->
                MessageWindowChatMemory.builder()
                .id(memoryId)
                .chatMemoryStore(ChatMemoryStore)
                .maxMessages(10)
                .build();
    }

    /**
     * 基于milvus的向量存储对象
     * @return
     */

    public static MilvusEmbeddingStore milvusEmbeddingStore(String collectionName,Integer  dimension){
        MilvusEmbeddingStore store = MilvusEmbeddingStore.builder()

                .host("192.168.80.152")                         // Host for Milvus instance
                .port(19530)                               // Port for Milvus instance
                .collectionName(collectionName)      // Name of the collection
                .dimension(dimension)                            // Dimension of vectors
                .indexType(IndexType.FLAT)                 // Index type
                .metricType(MetricType.COSINE)             // Metric type
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
