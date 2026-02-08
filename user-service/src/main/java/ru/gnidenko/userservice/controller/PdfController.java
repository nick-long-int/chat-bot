package ru.gnidenko.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnidenko.userservice.service.PdfService;

@RestController
@RequestMapping("/api/v1/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final PdfService pdfService;

    @GetMapping
    public ResponseEntity<Resource> getPdfDoc() {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        byte[] pdfInBytes = pdfService.getPdf(jwt.getClaim("userId"));
        Resource resource = new ByteArrayResource(pdfInBytes);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;" +
                    " filename=user-info.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .contentLength(pdfInBytes.length)
            .body(resource);
    }

}
