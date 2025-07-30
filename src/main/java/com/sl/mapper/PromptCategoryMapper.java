package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.PromptCategory;
import com.sl.entity.PromptCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PromptCategoryMapper extends BaseMapper<PromptCategory> {
    int deleteByExample(PromptCategoryExample example);

    int deleteByPrimaryKey(@Param("userId") String userId, @Param("idCategory") String idCategory);

    int insert(PromptCategory record);

    int insertSelective(PromptCategory record);

    List<PromptCategory> selectByExample(PromptCategoryExample example);

    PromptCategory selectByPrimaryKey(@Param("userId") String userId, @Param("idCategory") String idCategory);

    int updateByPrimaryKeySelective(PromptCategory record);

    int updateByPrimaryKey(PromptCategory record);
}