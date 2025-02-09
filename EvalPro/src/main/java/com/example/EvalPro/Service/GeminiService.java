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

        // Check if the user query is about the bot's creator
        if (isCreatorQuery(userMessage)) {
            return "I am under the guidance of Insight Visioners.";
        }

        String apiUrlWithKey = GEMINI_API_URL + "?key=" + API_KEY;

        // Determine whether a short or long answer is needed
        String responseType = needsLongAnswer(userMessage) ? 
                "Provide a **detailed** education-related response with steps, explanations, and examples." : 
                "Provide a **concise** and **short** education-related response.";

        String educationPrompt = responseType + " Question: " + userMessage;

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

    private boolean isCreatorQuery(String userMessage) {
        String lowerCaseMessage = userMessage.toLowerCase();
        return lowerCaseMessage.contains("who developed you") ||
               lowerCaseMessage.contains("who created you") ||
               lowerCaseMessage.contains("who designed you") ||
               lowerCaseMessage.contains("who made you") ||
               lowerCaseMessage.contains("your creator") ||
               lowerCaseMessage.contains("who built you");
    }

    // Determine if a long answer is needed based on keywords
    private boolean needsLongAnswer(String userMessage) {
        String lowerCaseMessage = userMessage.toLowerCase();
        return lowerCaseMessage.contains("steps") ||
               lowerCaseMessage.contains("procedure") ||
               lowerCaseMessage.contains("process") ||
               lowerCaseMessage.contains("how to") ||
               lowerCaseMessage.contains("explain")||
               lowerCaseMessage.contains("I want")||
               lowerCaseMessage.contains("hi");

    }

    private String formatResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);

            JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");

            if (textNode.isMissingNode()) {
                return "No response from EduBOt.";
            }

            // Get the response text
            String responseText = textNode.asText();

            // Remove markdown-style formatting
            responseText = responseText.replaceAll("\\*\\*([^*]+)\\*\\*", "$1");  // Removing **bold**
            responseText = responseText.replaceAll("_([^_]+)_", "$1");  // Removing _italic_
            responseText = responseText.replaceAll("\\*", "");  // Removing extra asterisks

            // Ensure the response is education-related
            responseText = filterEducationContent(responseText);

            // Extract and append relevant links from the response
            responseText = appendRelevantLinks(responseText);

            // Convert text response into a user-friendly format
            return responseText.replace("\n", "\n\n");  //  line breaker

        } catch (Exception e) {
            return "Error parsing AI response.";
        }
    }

    private String filterEducationContent(String responseText) {
        if (responseText.toLowerCase().contains("education") ||
            responseText.toLowerCase().contains("learn") ||
            responseText.toLowerCase().contains("teach") ||
            responseText.toLowerCase().contains("school") ||
            responseText.toLowerCase().contains("student") ||
            responseText.toLowerCase().contains("result") ||
            responseText.toLowerCase().contains("marking") ||
            responseText.toLowerCase().contains("system") ||
            responseText.toLowerCase().contains("programming") ||
            responseText.toLowerCase().contains("revaluation") ||
            responseText.toLowerCase().contains("teacher")) {
            return responseText;
        } else {
            return "The response does not contain education-related content.";
        }
    }

    private String appendRelevantLinks(String responseText) {
        StringBuilder finalResponse = new StringBuilder(responseText);

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
