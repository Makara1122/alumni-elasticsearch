package org.example.attributeconverter18052024.feature.file;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.attributeconverter18052024.feature.file.dto.FileResponse;
import org.example.attributeconverter18052024.utils.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileRestController {

    private final FileService fileService;

    @GetMapping
    public BaseResponse<List<String>> getAllFileNames() {
        return BaseResponse
                .<List<String>>ok()
                .setPayload(fileService.getAllFileNames());
    }

    @PostMapping(value = "", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<FileResponse> uploadSingleFile(
            @RequestPart("file") MultipartFile file, HttpServletRequest request
    ) throws IOException {
        return BaseResponse
                .<FileResponse>ok()
                .setPayload(fileService.uploadSingleFile(file, request));
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        return fileService.serveFile(fileName, request);
    }

}
