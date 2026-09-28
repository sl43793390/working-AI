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
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.parser.apache.poi.ApachePoiDocumentParser;
import dev.langchain4j.data.document.parser.markdown.MarkdownDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
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

    Logger logger = org.slf4j.LoggerFactory.getLogger(RagService.class);
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
        return chatMapper.selectList(
                new LambdaQueryWrapper<ChatContent>()
                        .eq(ChatContent::getUserId, userId)
        );
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
        DocumentByParagraphSplitter documentSplitter = null;
        if (knowledgeBase.getSegmentLength() != null && knowledgeBase.getSegmentOverlap() != null){
            documentSplitter = new DocumentByParagraphSplitter(knowledgeBase.getSegmentLength(), knowledgeBase.getSegmentOverlap());
        }else{
            documentSplitter = new DocumentByParagraphSplitter(1000, 100);
        }
        //3. 创建向量存储
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(ModelConfig.milvusEmbeddingStore(selectedKnowledgeBase.getNameCollection()
                        ,selectedKnowledgeBase.getDimension()))
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .textSegmentTransformer(textSegment -> TextSegment.from(
                        textSegment.metadata().getString("file_name") + "\n" + textSegment.text(),
                        textSegment.metadata()
                ))
//                .textSplitter(new CharacterTextSplitter("\\n"))
                .build();
        //此处还需要做一些精细的处理，针对不同类型的文件使用不同的解析器，目前统一使用DocumentSplitters，TODO
        logger.info("file path:"+file.getFilePath());
        //加载单个文档
        Document loadDocument = null;
        String fileName = file.getFilePath();
        if (fileName.endsWith(".txt") || fileName.endsWith(".html")){
            loadDocument = FileSystemDocumentLoader.loadDocument(fileName, new TextDocumentParser());
        }else if (fileName.endsWith(".pdf")){
            loadDocument = FileSystemDocumentLoader.loadDocument(fileName,new ApachePdfBoxDocumentParser( true));
        }else if (fileName.endsWith(".doc") || fileName.endsWith(".docx") || fileName.endsWith(".ppt") || fileName.endsWith(".pptx")
                || fileName.endsWith(".xls") || fileName.endsWith(".xlsx")){
            loadDocument = FileSystemDocumentLoader.loadDocument(fileName,new ApachePoiDocumentParser());
        }else if (fileName.endsWith(".md")){
            loadDocument = FileSystemDocumentLoader.loadDocument(fileName,new MarkdownDocumentParser());
        }else {
            logger.error("不支持的文件类型:"+fileName);
            return null;
        }

        List<Document> documentList = Collections.singletonList(loadDocument);
        file.setFlagEmbedding("Y");
        IngestionResult ingestionResult = embeddingStoreIngestor.ingest(documentList);
        knowledgeBaseFileMapper.updateByPrimaryKey(file);
        return ingestionResult;
    }


    public List<AgentMemory> getAgentMemoryByUserId(String userId){
        return agentMemoryMapper.selectList(
                new LambdaQueryWrapper<AgentMemory>()
                        .eq(AgentMemory::getUserId, userId)
        );
    }

    public int insertAgentMemory(AgentMemory agentMemory){
        return agentMemoryMapper.insert(agentMemory);
    }

    public int updateAgentMemory(AgentMemory agentMemory){
        return agentMemoryMapper.update(agentMemory,
                new LambdaQueryWrapper<AgentMemory>()
                        .eq(AgentMemory::getUserId, agentMemory.getUserId())
                        .eq(AgentMemory::getSessionId, agentMemory.getSessionId())
        );
    }

    public int deleteAgentMemory(String userId, String sessionId){
       return agentMemoryMapper.deleteByPrimaryKey(userId, sessionId);
    }
}