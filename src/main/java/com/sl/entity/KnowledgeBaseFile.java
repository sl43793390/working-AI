package com.sl.entity;

import java.util.Date;

public class KnowledgeBaseFile {
    private String idBase;

    private String filePath;

    private String fileName;

    private String fileSize;

    private Date dtUpload;

    public String getIdBase() {
        return idBase;
    }

    public void setIdBase(String idBase) {
        this.idBase = idBase == null ? null : idBase.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize == null ? null : fileSize.trim();
    }

    public Date getDtUpload() {
        return dtUpload;
    }

    public void setDtUpload(Date dtUpload) {
        this.dtUpload = dtUpload;
    }
}