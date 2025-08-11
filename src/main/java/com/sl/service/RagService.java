package com.sl.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sl.config.ModelConfig;
import com.sl.entity.*;
import com.sl.mapper.AgentMemoryMapper;
import com.sl.mapper.ChatContentMapper;
import com.sl.mapper.KnowledgeBaseFileMapper;
import com.sl.mapper.KnowledgeBaseMapper;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.IngestionResult;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Service
public class RagService {

    @Resource
    private KnowledgeBaseMapper knowledgeBaseMapper;

    @Resource
    private KnowledgeBaseFileMapper knowledgeBaseFileMapper;
    @Resource
    private ChatContentMapper chatMapper;
    @Resource
    private AgentMemoryMapper agentMemoryMapper;
    @Resource
    private OpenAiEmbeddingModel embeddingModel;
    Logger logger = org.slf4j.LoggerFactory.getLogger(RagService.class);
    /**
     * 根据用户ID获取知识库列表
     * @param userId 用户ID
     * @return 知识库列表
     */
    public List<KnowledgeBase> getKnowledgeBasesByUserId(String userId) {
        KnowledgeBaseExample example = new KnowledgeBaseExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return knowledgeBaseMapper.selectByExample(example);
    }

    /**
     * 根据用户ID和名称搜索知识库
     * @param userId 用户ID
     * @param name 搜索名称
     * @return 匹配的知识库列表
     */
    public List<KnowledgeBase> searchKnowledgeBasesByName(String userId, String name) {
        KnowledgeBaseExample example = new KnowledgeBaseExample();
        example.createCriteria()
                .andUserIdEqualTo(userId)
                .andNameBaseLike("%" + name + "%");
        return knowledgeBaseMapper.selectByExample(example);
    }

    /**
     * 创建新的知识库
     * @param knowledgeBase 知识库对象
     * @return 是否创建成功
     */
    public boolean createKnowledgeBase(KnowledgeBase knowledgeBase) {
        int result = knowledgeBaseMapper.insertSelective(knowledgeBase);
        return result > 0;
    }

    /**
     * 更新知识库
     * @param knowledgeBase 知识库对象
     * @return 是否更新成功
     */
    public boolean updateKnowledgeBase(KnowledgeBase knowledgeBase) {
        int result = knowledgeBaseMapper.updateByPrimaryKeySelective(knowledgeBase);
        return result > 0;
    }

    /**
     * 删除知识库
     * @param userId 用户ID
     * @param nameBase 知识库名称
     * @return 是否删除成功
     */
    public boolean deleteKnowledgeBase(String userId, String nameBase) {
        int result = knowledgeBaseMapper.deleteByPrimaryKey(userId, nameBase);
        return result > 0;
    }

    /**
     * 根据知识库ID获取文件列表
     * @param idBase 知识库ID
     * @return 文件列表
     */
    public List<KnowledgeBaseFile> getFilesByKnowledgeBaseId(String idBase) {
        KnowledgeBaseFileExample example = new KnowledgeBaseFileExample();
        example.createCriteria().andIdBaseEqualTo(idBase);
        return knowledgeBaseFileMapper.selectByExample(example);
    }

    /**
     * 添加文件到知识库
     * @param file 文件对象
     * @return 是否添加成功
     */
    public boolean addFileToKnowledgeBase(KnowledgeBaseFile file) {
        int result = knowledgeBaseFileMapper.insertSelective(file);
        return result > 0;
    }

    /**
     * 从知识库删除文件
     * @param idBase 知识库ID
     * @return 是否删除成功
     */
    public boolean deleteFileFromKnowledgeBase(String idBase,String fileName) {
        int result = knowledgeBaseFileMapper.deleteByPrimaryKey(idBase,fileName);
        return result > 0;
    }

    public List<ChatContent> getChatContentByUserId(String userId){
        List<ChatContent> chatContents = chatMapper.selectList(
                new LambdaQueryWrapper<ChatContent>()
                        .eq(ChatContent::getUserId, userId)
        );
        return chatContents;
    }

    public List<AgentMemory> getAgentMemoryByUserId(String userId){
        List<AgentMemory> agentMemories = agentMemoryMapper.selectList(
                new LambdaQueryWrapper<AgentMemory>()
                        .eq(AgentMemory::getUserId, userId)
        );
        return agentMemories;
    }

    public int deleteChatContent(String userId, String sessionId){
       return chatMapper.deleteByPrimaryKey(userId, sessionId);
    }

    public int insertChatContent(ChatContent chatContent){
        return chatMapper.insert(chatContent);
    }
    public int updateChatContent(ChatContent chatContent){
        return chatMapper.update(chatContent,
                new LambdaQueryWrapper<ChatContent>()
                        .eq(ChatContent::getUserId, chatContent.getUserId())
                        .eq(ChatContent::getSessionId, chatContent.getSessionId())
        );
    }

    /**
     * 1. 使用DocumentSplitters对文档进行分割，maxsegmentSize和maxoverlapSize参数来设置分割参数。这两个参数来自knowledge_base表中的参数。
     *
     * @param userId
     * @param file
     * @param selectedKnowledgeBase
     * @return
     */
    public IngestionResult embedFile(String userId, KnowledgeBaseFile file, KnowledgeBase selectedKnowledgeBase) {
        KnowledgeBase knowledgeBase = knowledgeBaseMapper.selectByPrimaryKey(userId, selectedKnowledgeBase.getNameBase());
        DocumentSplitter documentSplitter = null;
        if (knowledgeBase.getSegmentLength() != null && knowledgeBase.getSegmentOverlap() != null){
            documentSplitter = DocumentSplitters.recursive(knowledgeBase.getSegmentLength(), knowledgeBase.getSegmentOverlap());
        }else{
            documentSplitter = DocumentSplitters.recursive(2000, 200);
        }
        //3. 创建向量存储
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(ModelConfig.milvusEmbeddingStore(selectedKnowledgeBase.getNameCollection(),1024))
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
//                .textSplitter(new CharacterTextSplitter("\\n"))
                .build();
        //此处还需要做一些精细的处理，针对不同类型的文件使用不同的解析器，目前统一使用DocumentSplitters，TODO
        logger.info("file path:"+file.getFilePath());
        String text = null;
        Document document = null;
        if (file.getFilePath().endsWith(".txt") || file.getFilePath().endsWith(".md")){
            try {
                text = Files.readString(Paths.get(file.getFilePath()), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            document = Document.from(text);
        }else {
            document = FileSystemDocumentLoader.loadDocument(file.getFilePath());
        }
        List<Document> documentList = Collections.singletonList(document);
        file.setFlagEmbedding("Y");
        IngestionResult ingestionResult = embeddingStoreIngestor.ingest(documentList);
        knowledgeBaseFileMapper.updateByPrimaryKey(file);
        return ingestionResult;
    }
}