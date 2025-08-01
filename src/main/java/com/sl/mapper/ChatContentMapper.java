package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.ChatContent;
import com.sl.entity.ChatContentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChatContentMapper extends BaseMapper<ChatContent> {
    int deleteByExample(ChatContentExample example);

    int deleteByPrimaryKey(@Param("userId") String userId, @Param("sessionId") String sessionId);

    int insert(ChatContent record);

    int insertSelective(ChatContent record);


    List<ChatContent> selectByExample(ChatContentExample example);

    ChatContent selectByPrimaryKey(@Param("userId") String userId, @Param("sessionId") String sessionId);

    int updateByPrimaryKeySelective(ChatContent record);

    int updateByPrimaryKey(ChatContent record);
}