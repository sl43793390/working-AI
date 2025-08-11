package com.sl.entity;

public class UserAgent {
    private String userId;

    private String idAgent;

    private String nameAgent;

    private String systemPrompt;

    private String cdDesc;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(String idAgent) {
        this.idAgent = idAgent == null ? null : idAgent.trim();
    }

    public String getNameAgent() {
        return nameAgent;
    }

    public void setNameAgent(String nameAgent) {
        this.nameAgent = nameAgent == null ? null : nameAgent.trim();
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt == null ? null : systemPrompt.trim();
    }

    public String getCdDesc() {
        return cdDesc;
    }

    public void setCdDesc(String cdDesc) {
        this.cdDesc = cdDesc == null ? null : cdDesc.trim();
    }
}