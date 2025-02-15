package com.example.EvalPro.Controller;

import com.example.EvalPro.Dao.RegistrationRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RegistrationRepo registrationRepo;

    @PostMapping("/upload/{studentId}")
    public ResponseEntity<String> uploadFiles(@PathVariable String studentId, @RequestParam("files") MultipartFile[] files) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            StringBuilder uploadedFiles = new StringBuilder();
            for (MultipartFile file : files) {
                String originalFileName = file.getOriginalFilename();
                if (originalFileName == null || !originalFileName.contains("_")) {
                    return ResponseEntity.badRequest().body("Invalid file format. Expected format: studentId_subjectName.pdf");
                }

                // Extract subject name (Example: "S12345_Math.pdf" → "Math")
                String subjectName = originalFileName.split("_")[1].replace(".pdf", "");
                String newFileName = studentId + "_" + subjectName + ".pdf";
                Path filePath = uploadPath.resolve(newFileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                uploadedFiles.append(newFileName).append(", ");
            }

            // Get student email from database
            String emailId = registrationRepo.findEmailByStudentId(studentId);
            if (emailId != null && !emailId.isEmpty()) {
                sendEmailWithAttachments(emailId, studentId);
            }

            return ResponseEntity.ok("Files uploaded successfully: " + uploadedFiles.toString());
        } catch (IOException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }

    private void sendEmailWithAttachments(String toEmail, String studentId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Your Photocopies Are Ready");

        // HTML Email Content
        String emailContent = "<html><body>"
                + "<h2 style='color: #2d89ef;'>Dear Student,</h2>"
                + "<p>Your requested photocopies are ready. Please find the attached PDFs.</p>"
                + "<p>If you have any queries, please contact us.</p>"
                + "<br><hr>"
                + "<p style='color: gray; font-size: 12px;'>Best regards,<br><b>EvalPro Chat bot OR Your College Administration</b></p>"
                + "<a href='http://localhost:8080/user/rechecking/apply?seatNumber=" + studentId + "'>Apply for rechecking</a><hr>"
                + "</body></html>";

        helper.setText(emailContent, true);

        // Attach all PDFs for the student
        Path uploadPath = Paths.get(UPLOAD_DIR);
        File folder = new File(uploadPath.toString());
        for (File file : folder.listFiles()) {
            if (file.getName().startsWith(studentId)) {
                helper.addAttachment(file.getName(), file);
            }
        }

        mailSender.send(message);
    }
}
