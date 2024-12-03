package kz.bitlab.middle03minio.middleminio03.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import kz.bitlab.middle03minio.middleminio03.dto.FileDto;
import kz.bitlab.middle03minio.middleminio03.mapper.FileMapper;
import kz.bitlab.middle03minio.middleminio03.model.UploadFile;
import kz.bitlab.middle03minio.middleminio03.repository.FileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    private final MinioClient minioClient;

    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    public FileService(MinioClient minioClient, FileRepository fileRepository, FileMapper fileMapper) {
        this.minioClient = minioClient;
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    @Value("${minio.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file) {
        try {
            LOGGER.info("Starting to upload file: " + file.getContentType());

            UploadFile uploadFile = new UploadFile();
            uploadFile.setFileName(file.getOriginalFilename());
            uploadFile.setFileSize(file.getSize());
            uploadFile.setMimeType(file.getContentType());
            uploadFile.setInitialName(file.getOriginalFilename());
            uploadFile.setCreatedDate(new Date());

            uploadFile = fileRepository.save(uploadFile);

            if (uploadFile.getId() != null) {

                String fileName = DigestUtils.sha1Hex(uploadFile.getId() + "_My_file");
                uploadFile.setFileName(fileName);
                minioClient.putObject(
                        PutObjectArgs
                                .builder()
                                .bucket(bucket)
                                .object(fileName)
                                .stream(file.getInputStream(), file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );

                fileRepository.save(uploadFile);

                return "File uploaded successfully";
            }

        } catch (Exception e) {
            LOGGER.error("error happened while uploading file: {} ", e.getMessage());
        }
        return "Some error on file upload";
    }

    public ByteArrayResource downloadFile(String fileName) {
        try {
            GetObjectArgs getObjectArgs =  GetObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(fileName)
                    .build();
            InputStream inputStream = minioClient.getObject(getObjectArgs);
            byte[] byteArray = IOUtils.toByteArray(inputStream);
            inputStream.close();
            return new ByteArrayResource(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FileDto> getFileList() {
        return fileMapper.toDtoList(fileRepository.findAll());
    }
}
