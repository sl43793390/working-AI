package com.sl.chat.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherTool implements Tool {
    private static final ObjectMapper mapper = new ObjectMapper(); // 共享实例

    @Override
    public String getName() {
        return "get_weather";
    }

    @Override
    public String getDescription() {
        return "获取指定城市的天气信息";
    }

    @Override
    public String getParametersSchema() {
        return """
        {
          "type": "object",
          "properties": {
            "location": {
              "type": "string",
              "description": "城市名称"
            }
          },
          "required": ["location"]
        }
        """;
    }
    @Override
    public String execute(String arguments) {
        try {
            JsonNode node = mapper.readTree(arguments);
            if (!node.has("location")) {
                return "{\"error\": \"Missing required field: location\"}";
            }
            String location = node.get("location").asText();

            // 模拟天气数据
            return String.format("{\"location\": \"%s\", \"temperature\": \"28°C\", \"condition\": \"Sunny\"}", location);
        } catch (Exception e) {
            return String.format("{\"error\": \"Invalid JSON: %s\"}", e.getMessage());
        }
    }

}