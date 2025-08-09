package com.dhruv.simpledropbox.model;


public class FileUploadResponse {
    private String fileName;
    private String message;

    public FileUploadResponse(String fileName, String message) {
        this.fileName = fileName;
        this.message = message;
    }
    public String getFileName() {
        return fileName;
    }

    public String getMessage() {
        return message;
    }
}
