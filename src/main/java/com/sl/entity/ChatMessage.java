package com.sl.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sl.config.LocalDateTimeDeserializer;
import com.sl.config.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class ChatMessage {
    private String text;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;
    private String userName;

    public ChatMessage() {
    }

    public ChatMessage(String text, LocalDateTime time, String userName) {
        this.text = text;
        this.time = time;
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}