package ru.gnidenko.userservice.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gnidenko.userservice.exception.NotFoundException;
import ru.gnidenko.userservice.model.User;
import ru.gnidenko.userservice.repo.UserRepo;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final UserRepo userRepo;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public byte[] getPdf(Long userId) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User userToDoc = user.get();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter pdfWriter = new PdfWriter(baos);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("User info:")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12));
            document.add(new Paragraph("\n"));

            Table table = new Table(UnitValue.createPercentArray(2));
            table.setWidth(UnitValue.createPercentValue(100));

            table.addCell("Username:");
            table.addCell(userToDoc.getUsername());

            table.addCell("Email:");
            table.addCell(userToDoc.getEmail());

            table.addCell("First name:");
            table.addCell(userToDoc.getFirstName());

            table.addCell("Last name:");
            table.addCell(userToDoc.getLastName());

            table.addCell("birthday:");
            table.addCell(userToDoc.getBirthday().toString());

            document.add(table);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Photo:\n"));
            try {
                byte[] imageBytes = s3Service.download(userId);

                ImageData imageData = ImageDataFactory.create(imageBytes);
                Image image = new Image(imageData)
                    .setAutoScale(true)
                    .setHorizontalAlignment(
                        HorizontalAlignment.CENTER
                    );
                document.add(image);
            } catch (NotFoundException e) {
                document.add(new Paragraph("Photo not found"));
            }
            document.close();
            pdfDocument.close();
            pdfWriter.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}