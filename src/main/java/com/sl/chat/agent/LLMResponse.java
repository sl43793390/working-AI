package com.sl.chat.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class LLMResponse {
    @JsonProperty("content")
    private String content;

    @JsonProperty("tool_calls")
    private List<ToolCall> tool_calls;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<ToolCall> getToolCalls() { return tool_calls; }
    public void setToolCalls(List<ToolCall> tool_calls) { this.tool_calls = tool_calls; }

    public boolean hasToolCalls() {
        return tool_calls != null && !tool_calls.isEmpty();
    }
}