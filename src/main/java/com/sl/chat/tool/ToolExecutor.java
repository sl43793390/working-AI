package com.sl.chat.tool;

import com.sl.chat.agent.ToolCall;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ToolExecutor {
    private final Map<String, Tool> toolRegistry;

    public ToolExecutor(List<Tool> tools) {
        this.toolRegistry = new HashMap<>();
        for (Tool tool : tools) {
            toolRegistry.put(tool.getName(), tool);
        }
    }

    public String execute(ToolCall toolCall) {
        String toolName = toolCall.getFunction().getName();
        String arguments = toolCall.getFunction().getArguments();

        Tool tool = toolRegistry.get(toolName);
        if (tool == null) {
            return "{\"error\": \"Tool not found: " + toolName + "\"}";
        }

        return tool.execute(arguments);
    }
}