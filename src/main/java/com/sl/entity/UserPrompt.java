package com.sl.entity;

import java.util.Date;

public class UserPrompt {
    private String userId;

    private String idPrompt;

    private String namePrompt;

    private String cdCategory;

    private Date dtCreate;

    private String content;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getIdPrompt() {
        return idPrompt;
    }

    public void setIdPrompt(String idPrompt) {
        this.idPrompt = idPrompt == null ? null : idPrompt.trim();
    }

    public String getNamePrompt() {
        return namePrompt;
    }

    public void setNamePrompt(String namePrompt) {
        this.namePrompt = namePrompt == null ? null : namePrompt.trim();
    }

    public String getCdCategory() {
        return cdCategory;
    }

    public void setCdCategory(String cdCategory) {
        this.cdCategory = cdCategory == null ? null : cdCategory.trim();
    }

    public Date getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(Date dtCreate) {
        this.dtCreate = dtCreate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}