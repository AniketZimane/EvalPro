package com.example.EvalPro.Controller;

import net.sourceforge.tess4j.*;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class HandwritingController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping(value = "/compare", consumes = "multipart/form-data")
    public String compareAnswers(
            @RequestParam("studentFile") MultipartFile studentFile,
            @RequestParam("modelAnswerFile") MultipartFile modelAnswerFile) {

        try {
            // 1. Convert PDFs to Images with Preprocessing
            List<BufferedImage> studentImages = convertPdfToImages(saveFile(studentFile));
            List<BufferedImage> modelImages = convertPdfToImages(saveFile(modelAnswerFile));

            // 2. Extract Text with Enhanced OCR
            String studentText = extractTextFromImages(studentImages);
            String modelText = extractTextFromImages(modelImages);

            // 3. Debug: Print Raw Extracted Text
            System.out.println("Raw Student Text: " + studentText);
            System.out.println("Raw Model Text: " + modelText);

            // 4. Clean and Process Text
            studentText = cleanAndProcessText(studentText);
            modelText = cleanAndProcessText(modelText);

            // 5. Calculate Similarity
            double similarityScore = calculateCosineSimilarity(studentText, modelText);
            int marks = (int) (similarityScore * 70 / 100);

            return "Extracted Student Answer:\n" + studentText +
                    "\n\nExtracted Model Answer:\n" + modelText +
                    "\n\nSimilarity Score: " + similarityScore + "%" +
                    "\nMarks: " + marks + "/70";
        } catch (Exception e) {
            return "Error processing files: " + e.getMessage();
        }
    }

    private String extractTextFromImages(List<BufferedImage> images) {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("D:/Code/Innovative_things/Spring Project/EvalPro/EvalPro/src/main/resources/static/tessdata/data");
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(3);  // PSM_AUTO
        tesseract.setOcrEngineMode(1);  // Legacy engine

        StringBuilder text = new StringBuilder();
        for (BufferedImage image : images) {
            try {
                BufferedImage processedImage = preprocessImage(image);
                text.append(tesseract.doOCR(processedImage)).append(" ");
            } catch (TesseractException e) {
                System.out.println("OCR Error: " + e.getMessage());
            }
        }
        return text.toString();
    }

    private BufferedImage preprocessImage(BufferedImage image) {
        // Convert to grayscale
        BufferedImage grayscale = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY
        );
        grayscale.getGraphics().drawImage(image, 0, 0, null);

        // Binarization
        BufferedImage binary = new BufferedImage(
                grayscale.getWidth(), grayscale.getHeight(), BufferedImage.TYPE_BYTE_BINARY
        );
        binary.getGraphics().drawImage(grayscale, 0, 0, null);

        return binary;
    }

    private String cleanAndProcessText(String text) {
        // Basic cleaning
        text = text.toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", " ")
                .trim();

        // Tokenization and stemming
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        PorterStemmer stemmer = new PorterStemmer();

        return Arrays.stream(tokenizer.tokenize(text))
                .filter(word -> !Set.of("the", "is", "a", "an").contains(word))
                .map(stemmer::stem)
                .collect(Collectors.joining(" "));
    }

    private double calculateCosineSimilarity(String text1, String text2) {
        // Simple TF-IDF cosine similarity
        Map<String, Integer> vector1 = createVector(text1);
        Map<String, Integer> vector2 = createVector(text2);

        Set<String> vocabulary = new HashSet<>();
        vocabulary.addAll(vector1.keySet());
        vocabulary.addAll(vector2.keySet());

        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;

        for (String word : vocabulary) {
            int v1 = vector1.getOrDefault(word, 0);
            int v2 = vector2.getOrDefault(word, 0);
            dotProduct += v1 * v2;
            norm1 += Math.pow(v1, 2);
            norm2 += Math.pow(v2, 2);
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2)) * 100;
    }

    private Map<String, Integer> createVector(String text) {
        Map<String, Integer> vector = new HashMap<>();
        for (String word : text.split(" ")) {
            vector.put(word, vector.getOrDefault(word, 0) + 1);
        }
        return vector;
    }

    private List<BufferedImage> convertPdfToImages(File pdfFile) throws IOException {
        List<BufferedImage> images = new ArrayList<>();
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300); // High DPI for better quality
                images.add(image);
            }
        }
        return images;
    }

    private File saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toFile();
    }


}