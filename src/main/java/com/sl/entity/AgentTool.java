package com.sl.entity;

public class AgentTool {
    private String idAgent;

    private String nameTool;

    private String nameMcp;

    private String nameToolClass;

    private String mcpConfigContent;

    private String mcpDesc;

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

    public String getNameToolClass() {
        return nameToolClass;
    }

    public void setNameToolClass(String nameToolClass) {
        this.nameToolClass = nameToolClass == null ? null : nameToolClass.trim();
    }

    public String getMcpConfigContent() {
        return mcpConfigContent;
    }

    public void setMcpConfigContent(String mcpConfigContent) {
        this.mcpConfigContent = mcpConfigContent == null ? null : mcpConfigContent.trim();
    }

    public String getMcpDesc() {
        return mcpDesc;
    }

    public void setMcpDesc(String mcpDesc) {
        this.mcpDesc = mcpDesc == null ? null : mcpDesc.trim();
    }
}