package com.sl.mapper;

import com.sl.entity.KnowledgeBaseFile;
import com.sl.entity.KnowledgeBaseFileExample;
import java.util.List;

public interface KnowledgeBaseFileMapper {
    int deleteByExample(KnowledgeBaseFileExample example);

    int deleteByPrimaryKey(String idBase);

    int insert(KnowledgeBaseFile record);

    int insertSelective(KnowledgeBaseFile record);

    List<KnowledgeBaseFile> selectByExample(KnowledgeBaseFileExample example);

    KnowledgeBaseFile selectByPrimaryKey(String idBase);

    int updateByPrimaryKeySelective(KnowledgeBaseFile record);

    int updateByPrimaryKey(KnowledgeBaseFile record);
}