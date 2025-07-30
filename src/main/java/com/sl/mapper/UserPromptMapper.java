package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.UserPrompt;
import com.sl.entity.UserPromptExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserPromptMapper extends BaseMapper<UserPrompt> {
    int deleteByExample(UserPromptExample example);

    int deleteByPrimaryKey(@Param("userId") String userId, @Param("idPrompt") String idPrompt);

    int insert(UserPrompt record);

    int insertSelective(UserPrompt record);

    List<UserPrompt> selectByExample(UserPromptExample example);

    UserPrompt selectByPrimaryKey(@Param("userId") String userId, @Param("idPrompt") String idPrompt);

    int updateByPrimaryKeySelective(UserPrompt record);

    int updateByPrimaryKey(UserPrompt record);
}