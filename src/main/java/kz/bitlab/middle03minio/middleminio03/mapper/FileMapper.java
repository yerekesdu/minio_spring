package kz.bitlab.middle03minio.middleminio03.mapper;

import kz.bitlab.middle03minio.middleminio03.dto.FileDto;
import kz.bitlab.middle03minio.middleminio03.model.UploadFile;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileMapper {

    public FileDto toDto(UploadFile uploadFile) {
        FileDto fileDto = new FileDto();
        fileDto.setId(uploadFile.getId());
        fileDto.setFileName(uploadFile.getFileName());
        fileDto.setFileSize(uploadFile.getFileSize());
        fileDto.setMimeType(uploadFile.getMimeType());
        fileDto.setInitialName(uploadFile.getInitialName());
        fileDto.setCreatedDate(uploadFile.getCreatedDate());

        return fileDto;
    };

    public List<FileDto> toDtoList(List<UploadFile> uploadFiles) {
        List<FileDto> fileDtos = new ArrayList<>();
        for (UploadFile uploadFile : uploadFiles) {
            fileDtos.add(toDto(uploadFile));
        }
        return fileDtos;
    };
}
