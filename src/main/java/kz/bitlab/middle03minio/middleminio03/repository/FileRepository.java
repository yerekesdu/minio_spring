package kz.bitlab.middle03minio.middleminio03.repository;

import kz.bitlab.middle03minio.middleminio03.model.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<UploadFile, Long> {
}
