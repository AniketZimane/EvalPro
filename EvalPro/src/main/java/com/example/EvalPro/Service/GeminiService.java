package com.example.EvalPro.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class GeminiService {

    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
    private final String API_KEY = "AIzaSyAjlGNLiD7x-gcxqs3zuvn4i5Lwc1WQZ7k"; // Replace with your actual API key

    public String getGeminiResponse(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrlWithKey = GEMINI_API_URL + "?key=" + API_KEY;

        // Modify the userMessage to explicitly request education-related content
        String educationPrompt = "Provide an education-related response to the following: " + userMessage;

        Map<String, Object> requestBody = Map.of(
                "contents", Collections.singletonList(Map.of(
                        "parts", Collections.singletonList(Map.of(
                                "text", educationPrompt
                        ))
                ))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrlWithKey, HttpMethod.POST, entity, String.class
            );

            return formatResponse(response.getBody()); // Format response before returning

        } catch (HttpClientErrorException e) {
            return "API Error: " + e.getResponseBodyAsString();
        }
    }

    private String formatResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);

            JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");

            if (textNode.isMissingNode()) {
                return "No response from Gemini AI.";
            }

            // Get the response text
            String responseText = textNode.asText();

            // Remove any markdown-style formatting (like **bold** or _italic_)
            responseText = responseText.replaceAll("\\*\\*([^*]+)\\*\\*", "$1");  // Removing **bold**
            responseText = responseText.replaceAll("_([^_]+)_", "$1");  // Removing _italic_

            // You can further remove asterisks or any other characters if needed
            responseText = responseText.replaceAll("\\*", "");

            // Ensure the response is education-related
            responseText = filterEducationContent(responseText);

            // Extract and append relevant links from the response
            responseText = appendRelevantLinks(responseText);

            // Convert text response into a user-friendly format
            return responseText.replace("\n", "\n\n");  // Ensure proper line breaks

        } catch (Exception e) {
            return "Error parsing AI response.";
        }
    }

    private String filterEducationContent(String responseText) {
        // Add logic to filter out non-education-related content
        // For example, you can check for keywords related to education
        if (responseText.toLowerCase().contains("education") ||
                responseText.toLowerCase().contains("learn") ||
                responseText.toLowerCase().contains("teach") ||
                responseText.toLowerCase().contains("school") ||
                responseText.toLowerCase().contains("student") ||
                responseText.toLowerCase().contains("result") ||
                responseText.toLowerCase().contains("revaluation") ||
                responseText.toLowerCase().contains("teacher")) {
            return responseText;
        } else {
            return "The response does not contain education-related content.";
        }
    }

    private String appendRelevantLinks(String responseText) {
        StringBuilder finalResponse = new StringBuilder(responseText);

        // Add relevant links based on the context of the response
        if (responseText.toLowerCase().contains("education")) {
            finalResponse.append("\n\nFor more information, visit: https://www.education.gov/");
        }
        if (responseText.toLowerCase().contains("learn")) {
            finalResponse.append("\n\nCheck out online learning resources: https://www.khanacademy.org/");
        }
        if (responseText.toLowerCase().contains("school")) {
            finalResponse.append("\n\nFind school-related resources: https://www.schools.com/");
        }
        if (responseText.toLowerCase().contains("student")) {
            finalResponse.append("\n\nStudent resources: https://www.students.com/");
        }
        if (responseText.toLowerCase().contains("teacher")) {
            finalResponse.append("\n\nTeacher resources: https://www.teachers.com/");
        }

        return finalResponse.toString();
    }
}