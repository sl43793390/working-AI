package com.sl.mapper;

import com.sl.entity.KnowledgeBase;
import com.sl.entity.KnowledgeBaseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeBaseMapper {
    int deleteByExample(KnowledgeBaseExample example);

    int deleteByPrimaryKey(@Param("userId") String userId, @Param("nameBase") String nameBase);

    int insert(KnowledgeBase record);

    int insertSelective(KnowledgeBase record);

    List<KnowledgeBase> selectByExample(KnowledgeBaseExample example);

    KnowledgeBase selectByPrimaryKey(@Param("userId") String userId, @Param("nameBase") String nameBase);

    int updateByPrimaryKeySelective(KnowledgeBase record);

    int updateByPrimaryKey(KnowledgeBase record);
}