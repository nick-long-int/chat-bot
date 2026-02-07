package ru.gnidenko.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.gnidenko.userservice.service.S3Service;

@RestController
@RequestMapping("/api/v1/users/avatar")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping
    public void upload(@RequestBody MultipartFile file) {
        Long currentId = getUserId();
        s3Service.upload(file, currentId);
    }

    @GetMapping
    public byte[] download() {
        Long currentId = getUserId();
        return s3Service.download(currentId);
    }

    @DeleteMapping
    public void delete() {
        Long currentId = getUserId();
        s3Service.delete(currentId);
    }

    private Long getUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaim("userId");
    }
}
