package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.AgentMemory;
import com.sl.entity.AgentMemoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AgentMemoryMapper extends BaseMapper<AgentMemory> {
    int deleteByExample(AgentMemoryExample example);

    int deleteByPrimaryKey(@Param("userId") String userId, @Param("sessionId") String sessionId);

    int insert(AgentMemory record);

    int insertSelective(AgentMemory record);

    List<AgentMemory> selectByExampleWithBLOBs(AgentMemoryExample example);

    List<AgentMemory> selectByExample(AgentMemoryExample example);

    AgentMemory selectByPrimaryKey(@Param("userId") String userId, @Param("sessionId") String sessionId);

    int updateByPrimaryKeySelective(AgentMemory record);

    int updateByPrimaryKeyWithBLOBs(AgentMemory record);

    int updateByPrimaryKey(AgentMemory record);
}