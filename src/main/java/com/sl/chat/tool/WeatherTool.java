package com.sl.chat.tool;


import dev.langchain4j.agent.tool.Tool;

@MyTool(name = "weather_tool", description = "A tool for getting weather information")
public class WeatherTool{

    @Tool(name = "Get weather information for a given city")
    public String getWeather(String city) {
        return city +" 当前天气晴，25°";
    }

}