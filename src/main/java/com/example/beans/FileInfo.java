package com.example.beans;

public class FileInfo {
    String message;
    String fileName;
    String fileContent;

    public FileInfo(String message) {
        this.message = message;
    }

    public FileInfo(String message, String fileName, String fileContent) {
        this.message = message;
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
}
