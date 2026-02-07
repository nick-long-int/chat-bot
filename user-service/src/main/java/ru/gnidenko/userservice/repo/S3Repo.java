package ru.gnidenko.userservice.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;

@Repository
@RequiredArgsConstructor
public class S3Repo {

    private final S3Client s3Client;
    @Value("${minio.bucketName}")
    private String bucketName;

    public String save(byte[] bytes, String fileName, Long userId) {
        String key = generateKey(fileName, userId);
        s3Client.putObject(req ->
            req
                .bucket(bucketName)
                .key(key)
                .ifNoneMatch("*"),
            RequestBody.fromBytes(bytes));
        return key;
    }

    public byte[] get(String key) throws IOException {
        return s3Client.getObject(req ->
            req
                .bucket(bucketName)
                .key(key),
            ResponseTransformer.toInputStream()
        ).readAllBytes();
    }

    public void delete(String key) {
        s3Client.deleteObject(req -> req.bucket(bucketName).key(key));
    }

    private String generateKey(String fileName, Long userId){
        return userId + "-" + fileName;
    }

}
