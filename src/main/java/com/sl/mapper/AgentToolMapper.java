package com.sl.mapper;

import com.sl.entity.AgentTool;
import com.sl.entity.AgentToolExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AgentToolMapper {
    int deleteByExample(AgentToolExample example);

    int deleteByPrimaryKey(@Param("idAgent") String idAgent, @Param("nameTool") String nameTool);

    int insert(AgentTool record);

    int insertSelective(AgentTool record);

    List<AgentTool> selectByExample(AgentToolExample example);

    AgentTool selectByPrimaryKey(@Param("idAgent") String idAgent, @Param("nameTool") String nameTool);

    int updateByPrimaryKeySelective(AgentTool record);

    int updateByPrimaryKey(AgentTool record);
}