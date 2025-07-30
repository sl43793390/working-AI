package com.sl.service;

import com.sl.entity.KnowledgeBase;
import com.sl.entity.KnowledgeBaseExample;
import com.sl.entity.KnowledgeBaseFile;
import com.sl.entity.KnowledgeBaseFileExample;
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
}