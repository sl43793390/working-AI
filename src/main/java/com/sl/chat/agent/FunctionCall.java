package com.sl.chat.agent;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FunctionCall {
    @JsonProperty("name")
    private String name;

    @JsonProperty("arguments")
    private String arguments; // JSON string

    // 构造函数
    public FunctionCall() {}

    public FunctionCall(String name, String arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getArguments() { return arguments; }
    public void setArguments(String arguments) { this.arguments = arguments; }
}