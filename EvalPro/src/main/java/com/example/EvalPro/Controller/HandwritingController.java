package com.example.EvalPro.Controller;


import com.example.EvalPro.Service.SynonymChecker;
import net.sourceforge.tess4j.*;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.rendering.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.List;
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
            // Convert PDFs to Images
            BufferedImage studentImage = convertPdfToImage(saveFile(studentFile));
            BufferedImage modelImage = convertPdfToImage(saveFile(modelAnswerFile));

            // Extract text using OCR
            String studentText = extractTextFromImage(studentImage);
            String modelText = extractTextFromImage(modelImage);

            // Compare and Calculate Marks
            double similarityScore = calculateSimilarity(studentText, modelText);
            int marks = (int) (similarityScore * 70 / 100); // Assuming marks out of 10

            return "Extracted Student Answer:\n" + studentText +
                    "\n\nExtracted Model Answer:\n" + modelText +
                    "\n\nSimilarity Score: " + similarityScore + "%" +
                    "\nMarks: " + marks + "/70";
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

        // Set DPI (Dots Per Inch) for High-Quality OCR
        int dpi = 300; // Use 300 DPI for better text clarity

        BufferedImage image = renderer.renderImageWithDPI(0, dpi); // Convert first page to image with DPI
        document.close();
        return image;
    }




    private BufferedImage preprocessImage(BufferedImage image) {
        BufferedImage processedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        processedImage.getGraphics().drawImage(image, 0, 0, null);
        return processedImage;
    }

    private String extractTextFromImage(BufferedImage image) {
        ITesseract tesseract = new Tesseract();
        File tessdataFolder = new File("D:\\Code\\Innovative_things\\Spring Project\\EvalPro\\EvalPro\\src\\main\\resources\\static\\tessdata\\data");
        tesseract.setDatapath(tessdataFolder.getAbsolutePath());
        tesseract.setLanguage("eng");

        // Set OCR engine mode & page segmentation mode
        tesseract.setPageSegMode(6);  // Assume a single uniform block of text
        tesseract.setOcrEngineMode(1);  // Use Tesseract’s legacy mode for better structure

        try {
            BufferedImage processedImage = preprocessImage(image);
            String text = tesseract.doOCR(processedImage);

            // Post-processing: Remove unwanted characters & extra spaces
            return text.replaceAll("[^a-zA-Z0-9 ]", "").replaceAll("\\s+", " ").trim();
        } catch (TesseractException e) {
            return "OCR Error: " + e.getMessage();
        }
    }



    private double calculateSimilarity(String studentText, String modelText) {
        studentText = cleanAndProcessText(studentText);
        modelText = cleanAndProcessText(modelText);

        String[] studentWords = studentText.split("\\s+");
        String[] modelWords = modelText.split("\\s+");

        int matchedWords = 0;
        int totalWords = Math.max(studentWords.length, modelWords.length);

        for (int i = 0; i < studentWords.length && i < modelWords.length; i++) {
            if (studentWords[i].equalsIgnoreCase(modelWords[i])) {
                matchedWords++;
            } else {
                List<String> synonyms = SynonymChecker.getSynonyms(modelWords[i]);
                if (synonyms.contains(studentWords[i].toLowerCase())) {
                    matchedWords++;
                } else {
                    // Use Levenshtein Distance to allow minor variations
                    if (levenshteinDistance(studentWords[i], modelWords[i]) <= 2) {
                        matchedWords++;
                    }
                }
            }
        }

        return ((double) matchedWords / totalWords) * 100;
    }

    private String cleanAndProcessText(String text) {
        text = text.toLowerCase()
                .replaceAll("[^a-z0-9 ]", "") // Remove special characters
                .replaceAll("\\s+", " ") // Remove extra spaces
                .trim();

        return text;
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


    private List<String> preprocessText(String text) {
        // Tokenization
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(text.toLowerCase());

        // Stopword removal
        Set<String> stopWords = Set.of("the", "is", "in", "on", "at", "a", "an", "and", "of", "for");
        List<String> filteredTokens = Arrays.stream(tokens)
                .filter(word -> !stopWords.contains(word))
                .collect(Collectors.toList());

        // Stemming (convert words to base form)
        PorterStemmer stemmer = new PorterStemmer();
        return filteredTokens.stream()
                .map(stemmer::stem)
                .collect(Collectors.toList());
    }

    private Map<CharSequence, Integer> getWordFrequencies(List<String> words) {
        Map<CharSequence, Integer> freqMap = new HashMap<>();
        for (String word : words) {
            freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
        }
        return freqMap;
    }




}

