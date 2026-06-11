package com.sl.chat.tool;

import dev.langchain4j.agent.tool.Tool;

import java.time.LocalDate;
import java.time.LocalDateTime;

@MyTool(name = "dateTime", description = "日期时间工具类")
public class DateTimeTool {

    @Tool(name = " 获取当前日期和时间")
    public String getDateTime() {
        return LocalDateTime.now().toString();
    }

    @Tool(name = " 获取当前日期")
    public String getDate() {
        return LocalDate.now().toString();
    }
}
