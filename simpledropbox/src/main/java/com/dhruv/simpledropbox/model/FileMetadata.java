package com.dhruv.simpledropbox.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "file_metadata")
public class FileMetadata {
    @Id
    @GeneratedValue
    private UUID id;
    private String fileName;
    private long fileSize;
    private LocalDateTime uploadedAt;
    private String mimetype;

    public FileMetadata() {
    }

    public FileMetadata(String fileName, long fileSize, LocalDateTime uploadedAt, String mimetype) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt;
        this.mimetype = mimetype;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getMimeType() {
        return mimetype;
    }

    public void setMimeType(String mimetype) {
        this.mimetype = mimetype;
    }
}
