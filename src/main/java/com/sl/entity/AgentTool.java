package com.sl.entity;

public class AgentTool {
    private String idAgent;

    private String nameTool;

    private String nameMcp;

    public String getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(String idAgent) {
        this.idAgent = idAgent == null ? null : idAgent.trim();
    }

    public String getNameTool() {
        return nameTool;
    }

    public void setNameTool(String nameTool) {
        this.nameTool = nameTool == null ? null : nameTool.trim();
    }

    public String getNameMcp() {
        return nameMcp;
    }

    public void setNameMcp(String nameMcp) {
        this.nameMcp = nameMcp == null ? null : nameMcp.trim();
    }
}