package com.sl.mapper;

import com.sl.entity.ChatMemory;
import com.sl.entity.ChatMemoryExample;
import java.util.List;

public interface ChatMemoryMapper {
    int deleteByExample(ChatMemoryExample example);

    int deleteByPrimaryKey(String sessionId);

    int insert(ChatMemory record);

    int insertSelective(ChatMemory record);

    List<ChatMemory> selectByExampleWithBLOBs(ChatMemoryExample example);

    List<ChatMemory> selectByExample(ChatMemoryExample example);

    ChatMemory selectByPrimaryKey(String sessionId);

    int updateByPrimaryKeySelective(ChatMemory record);

    int updateByPrimaryKeyWithBLOBs(ChatMemory record);

    int updateByPrimaryKey(ChatMemory record);
}