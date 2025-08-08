package com.sl.chat.agent;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ToolCall {
    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("function")
    private FunctionCall function;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public FunctionCall getFunction() { return function; }
    public void setFunction(FunctionCall function) { this.function = function; }
}