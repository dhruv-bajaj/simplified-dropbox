package com.dhruv.simpledropbox.repository;

import com.dhruv.simpledropbox.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, UUID> {
}
