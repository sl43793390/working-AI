package com.sl.chat.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.annotation.Tool;

@MyTool(name = "weather_tool", description = "A tool for getting weather information")
public class WeatherTool{

    @Tool(name = "get_weather", description = "Get weather information for a given city")
    public String getWeather(String city) {
        return city +" 当前天气晴，25°";
    }

}