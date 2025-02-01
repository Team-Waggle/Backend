package com.waggle.global.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.waggle.global.exception.S3Exception;
import com.waggle.global.response.ApiStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Set<String> ALLOWED_FILE_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif"
    );

    @Value("${AWS_BUCKET}")
    private String bucket;

    public String uploadFile(MultipartFile file, String fileName) {
        validateFile(file);

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
            return amazonS3.getUrl(bucket, fileName).toString();
        } catch (IOException | AmazonS3Exception e) {
            throw new S3Exception(ApiStatus._S3_UPLOAD_FAILED, e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            String fileName = extractfilenamefromurl(fileUrl);
//            if (!amazonS3.doesObjectExist(bucket, fileName)) {
//                throw new S3Exception(ApiStatus._S3_FILE_NOT_FOUND);
//            }
            amazonS3.deleteObject(bucket, fileName);
        } catch (Exception e) {
            log.info("deleteFile: {}", e);
            //throw new S3Exception(ApiStatus._S3_DELETE_FAILED, e);
        }
    }

    public boolean isFileExist(String fileUrl) {
        try {
            String fileName = extractfilenamefromurl(fileUrl);
            return amazonS3.doesObjectExist(bucket, fileName);
        } catch (AmazonS3Exception e) {
            throw new S3Exception(ApiStatus._S3_FILE_NOT_FOUND);
        }
    }

    public String getUrlFromFileName(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new S3Exception(ApiStatus._S3_FILE_NOT_FOUND);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new S3Exception(ApiStatus._S3_FILE_SIZE_EXCEEDED);
        }

        if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
            throw new S3Exception(ApiStatus._S3_INVALID_FILE_TYPE);
        }
    }

    private String extractfilenamefromurl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            return url.getPath().substring(1);
        } catch (StringIndexOutOfBoundsException e) {
            throw new S3Exception(ApiStatus._S3_FILE_NOT_FOUND);
        } catch (MalformedURLException e) {
            throw new S3Exception(ApiStatus._S3_INVALID_URL);
        }
    }
}
