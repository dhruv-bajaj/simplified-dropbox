package com.dhruv.simpledropbox.service;

import com.dhruv.simpledropbox.model.FileUploadResponse;
import com.dhruv.simpledropbox.model.FileMetadata;
import com.dhruv.simpledropbox.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import java.util.UUID;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class DropboxUploadService {
    @Autowired
    private S3Client amazonS3Client;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Value("${s3.bucket}")
    private String bucketName;

    private final String[] validExtensions = { "png", "txt", "jpg", "json", "pdf"};

    private boolean validateFileType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        return Arrays.stream(validExtensions)
                .anyMatch(ext -> ext.equalsIgnoreCase(extension));
    }

    public FileUploadResponse uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (!validateFileType(fileName)) {
            String msg = String.format("File:  %s is not supported. Valid extensions are: %s", fileName,
                    String.join(",", validExtensions));
            return new FileUploadResponse(fileName, msg);
        }
        String mimeType = file.getContentType();
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        FileMetadata metadata = new FileMetadata(fileName, file.getSize(), LocalDateTime.now(), mimeType);
        metadata = fileMetadataRepository.save(metadata); // Save and get generated ID
        String s3Key = metadata.getId().toString();
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();
            amazonS3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (Exception e) {
            fileMetadataRepository.deleteById(metadata.getId());
            String msg = String.format("File: %s upload failed due to S3 error.", fileName);
            return new FileUploadResponse(fileName, msg);
        }
        String msg = String.format("File: %s uploaded successfully", fileName);
        return new FileUploadResponse(fileName, msg);
    }

    public List<FileMetadata> listFiles() {
        return fileMetadataRepository.findAll();
    }

    public ResponseEntity<byte[]> downloadFileById(UUID id) {
        Optional<FileMetadata> metadataOpt = fileMetadataRepository.findById(id);
        if (metadataOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        FileMetadata metadata = metadataOpt.get();
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(metadata.getId().toString())
                    .build();
            ResponseInputStream<GetObjectResponse> s3Object = amazonS3Client.getObject(getObjectRequest);
            byte[] fileBytes = inputStreamToByteArray(s3Object);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getFileName() + "\"");
            return ResponseEntity.ok().headers(headers).body(fileBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<byte[]> viewFileById(UUID id) {
        Optional<FileMetadata> metadataOpt = fileMetadataRepository.findById(id);
        if (metadataOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        FileMetadata metadata = metadataOpt.get();
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(metadata.getId().toString())
                    .build();
            ResponseInputStream<GetObjectResponse> s3Object = amazonS3Client.getObject(getObjectRequest);
            byte[] fileBytes = inputStreamToByteArray(s3Object);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + metadata.getFileName() + "\"");
            headers.setContentType(MediaType.parseMediaType(metadata.getMimeType()));
            return ResponseEntity.ok().headers(headers).body(fileBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384]; // 16kb chunks
        int bytesRead;
        while (true) {
            bytesRead = inputStream.read(data, 0, data.length);
            if (bytesRead == -1) {
                break;
            }
            buffer.write(data, 0, bytesRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}
