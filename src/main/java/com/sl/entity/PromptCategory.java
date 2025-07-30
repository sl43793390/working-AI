package com.sl.entity;

public class PromptCategory {
    private String userId;

    private String idCategory;

    private String nameCategory;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory == null ? null : idCategory.trim();
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory == null ? null : nameCategory.trim();
    }
}