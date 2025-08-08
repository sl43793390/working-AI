package com.sl.chat.tool;

public interface Tool {
    String getName();
    String getDescription();
    String getParametersSchema(); // JSON Schema
    String execute(String arguments); // arguments 是 JSON 字符串
}