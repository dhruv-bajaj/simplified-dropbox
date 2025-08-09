package com.dhruv.simpledropbox.controller;

import com.dhruv.simpledropbox.model.FileMetadata;
import com.dhruv.simpledropbox.model.FileUploadResponse;
import com.dhruv.simpledropbox.service.DropboxUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping(value = "/dropbox")
public class DropboxController {

    @Autowired
    private DropboxUploadService dropboxUploadService;

    @GetMapping("/listAllFiles")
    public List<FileMetadata> listAllFiles() {
        return dropboxUploadService.listFiles();
    }

    @PostMapping("/uploadFile")
    public FileUploadResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return dropboxUploadService.uploadFile(file);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") UUID id) {
        return dropboxUploadService.downloadFileById(id);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable("id") UUID id) {
        return dropboxUploadService.viewFileById(id);
    }
}
