package com.sl.service;

import com.sl.entity.AgentTool;
import com.sl.entity.AgentToolExample;
import com.sl.mapper.AgentToolMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class AgentService {

    @Resource
    private AgentToolMapper agentToolMapper;
    public List<AgentTool> getAgentTool(String idAgent) {
        AgentToolExample example = new AgentToolExample();
        example.createCriteria().andIdAgentEqualTo(idAgent);
        List<AgentTool> agentTools = agentToolMapper.selectByExample(example);
        return agentTools;
    }

    public List<Object> transferTool(List<AgentTool> agentTools){
        return agentTools.stream().map(tool -> {
            try {
                Class<?> aClass = Class.forName(tool.getNameToolClass());
                Constructor<?> constructor = aClass.getConstructor();
                Object o = constructor.newInstance();
                return o;
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
