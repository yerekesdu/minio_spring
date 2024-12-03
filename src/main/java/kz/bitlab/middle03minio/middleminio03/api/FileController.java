package kz.bitlab.middle03minio.middleminio03.api;

import kz.bitlab.middle03minio.middleminio03.dto.FileDto;
import kz.bitlab.middle03minio.middleminio03.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

    @GetMapping(value = "/download")
    public ResponseEntity<ByteArrayResource> download(@RequestParam("file") String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(fileService.downloadFile(fileName));
    }

    @GetMapping(value = "/list")
    public List<FileDto> getFiles() {
        return fileService.getFileList();
    }
}