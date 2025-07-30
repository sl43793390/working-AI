package com.sl.mapper;

import com.sl.entity.KnowledgeBaseFile;
import com.sl.entity.KnowledgeBaseFileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeBaseFileMapper {
    int deleteByExample(KnowledgeBaseFileExample example);

    int deleteByPrimaryKey(@Param("idBase") String idBase, @Param("fileName") String fileName);

    int insert(KnowledgeBaseFile record);

    int insertSelective(KnowledgeBaseFile record);

    List<KnowledgeBaseFile> selectByExample(KnowledgeBaseFileExample example);

    KnowledgeBaseFile selectByPrimaryKey(@Param("idBase") String idBase, @Param("fileName") String fileName);

    int updateByPrimaryKeySelective(KnowledgeBaseFile record);

    int updateByPrimaryKey(KnowledgeBaseFile record);
}