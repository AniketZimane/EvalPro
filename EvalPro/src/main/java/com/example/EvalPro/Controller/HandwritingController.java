package com.example.EvalPro.Controller;


import net.sourceforge.tess4j.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.rendering.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api")
public class HandwritingController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping(value = "/compare", consumes = "multipart/form-data")
    public String compareAnswers(
            @RequestParam("studentFile") MultipartFile studentFile,
            @RequestParam("modelAnswerFile") MultipartFile modelAnswerFile) {

        try {
            // Convert PDFs to Images
            BufferedImage studentImage = convertPdfToImage(saveFile(studentFile));
            BufferedImage modelImage = convertPdfToImage(saveFile(modelAnswerFile));

            // Extract text using OCR
            String studentText = extractTextFromImage(studentImage);
            String modelText = extractTextFromImage(modelImage);

            // Compare and Calculate Marks
            double similarityScore = calculateSimilarity(studentText, modelText);
            int marks = (int) (similarityScore * 10 / 100); // Assuming marks out of 10

            return "Extracted Student Answer:\n" + studentText +
                    "\n\nExtracted Model Answer:\n" + modelText +
                    "\n\nSimilarity Score: " + similarityScore + "%" +
                    "\nMarks: " + marks + "/10";
        } catch (Exception e) {
            return "Error processing files: " + e.getMessage();
        }
    }

    private File saveFile(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        Path filePath = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
        Files.write(filePath, file.getBytes());
        return filePath.toFile();
    }

    private BufferedImage convertPdfToImage(File pdfFile) throws IOException {
        PDDocument document = PDDocument.load(pdfFile);
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImage(0); // Convert first page to image
        document.close();
        return image;
    }

    private String extractTextFromImage(BufferedImage image) {
        ITesseract tesseract = new Tesseract();
        File tessdataFolder = new File("D:\\Code\\Innovative_things\\Spring Project\\EvalPro\\EvalPro\\src\\main\\resources\\static\\tessdata\\data"); // Ensure this path exists
        System.out.println("absolute path: "+tessdataFolder.getAbsolutePath());
        tesseract.setDatapath(tessdataFolder.getAbsolutePath());
        tesseract.setLanguage("eng"); // Use English for OCR
        try {
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            return "OCR Error: " + e.getMessage();
        }
    }

    private double calculateSimilarity(String studentText, String modelText) {
        int distance = levenshteinDistance(studentText, modelText);
        int maxLength = Math.max(studentText.length(), modelText.length());
        return (1.0 - ((double) distance / maxLength)) * 100;
    }

    private int levenshteinDistance(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + (str1.charAt(i - 1) == str2.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }
        return dp[str1.length()][str2.length()];
    }
}

