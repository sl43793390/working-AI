package com.sl.entity;

public class KnowledgeBase {
    private String userId;

    private String nameBase;

    private String idBase;

    private String descBase;

    private String namePath;

    private String nameCollection;

    private String dbType;

    private String segmentedMode;

    private String embeddingModel;

    private String searchType;

    private Integer segmentLength;

    private Integer segmentOverlap;

    private String flagRerank;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getNameBase() {
        return nameBase;
    }

    public void setNameBase(String nameBase) {
        this.nameBase = nameBase == null ? null : nameBase.trim();
    }

    public String getIdBase() {
        return idBase;
    }

    public void setIdBase(String idBase) {
        this.idBase = idBase == null ? null : idBase.trim();
    }

    public String getDescBase() {
        return descBase;
    }

    public void setDescBase(String descBase) {
        this.descBase = descBase == null ? null : descBase.trim();
    }

    public String getNamePath() {
        return namePath;
    }

    public void setNamePath(String namePath) {
        this.namePath = namePath == null ? null : namePath.trim();
    }

    public String getNameCollection() {
        return nameCollection;
    }

    public void setNameCollection(String nameCollection) {
        this.nameCollection = nameCollection == null ? null : nameCollection.trim();
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType == null ? null : dbType.trim();
    }

    public String getSegmentedMode() {
        return segmentedMode;
    }

    public void setSegmentedMode(String segmentedMode) {
        this.segmentedMode = segmentedMode == null ? null : segmentedMode.trim();
    }

    public String getEmbeddingModel() {
        return embeddingModel;
    }

    public void setEmbeddingModel(String embeddingModel) {
        this.embeddingModel = embeddingModel == null ? null : embeddingModel.trim();
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType == null ? null : searchType.trim();
    }

    public Integer getSegmentLength() {
        return segmentLength;
    }

    public void setSegmentLength(Integer segmentLength) {
        this.segmentLength = segmentLength;
    }

    public Integer getSegmentOverlap() {
        return segmentOverlap;
    }

    public void setSegmentOverlap(Integer segmentOverlap) {
        this.segmentOverlap = segmentOverlap;
    }

    public String getFlagRerank() {
        return flagRerank;
    }

    public void setFlagRerank(String flagRerank) {
        this.flagRerank = flagRerank == null ? null : flagRerank.trim();
    }
}