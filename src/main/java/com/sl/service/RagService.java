package com.sl.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sl.entity.*;
import com.sl.mapper.ChatContentMapper;
import com.sl.mapper.KnowledgeBaseFileMapper;
import com.sl.mapper.KnowledgeBaseMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {

    @Resource
    private KnowledgeBaseMapper knowledgeBaseMapper;

    @Resource
    private KnowledgeBaseFileMapper knowledgeBaseFileMapper;
    @Resource
    private ChatContentMapper chatMapper;
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

    public int deleteChatContent(String userId, String sessionId){
        return chatMapper.delete(
                new LambdaQueryWrapper<ChatContent>()
                        .eq(ChatContent::getUserId, userId)
                        .eq(ChatContent::getSessionId, sessionId)
        );
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
    //删除单独一条会话记录
    public int deleteChatContentBySessionId(String userId, String sessionId){
        return chatMapper.delete(
                new LambdaQueryWrapper<ChatContent>()
                        .eq(ChatContent::getUserId, userId)
                        .eq(ChatContent::getSessionId, sessionId)
        );
    }
}