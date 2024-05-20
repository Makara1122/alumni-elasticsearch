package org.example.attributeconverter18052024.feature.file;

import io.opencensus.resource.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.attributeconverter18052024.feature.file.dto.FileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    FileResponse uploadSingleFile(MultipartFile file, HttpServletRequest request) throws IOException;
    List<String> getAllFileNames();
    ResponseEntity<Resource> serveFile(String filename, HttpServletRequest request);
}
