package com.example.EvalPro.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SynonymChecker {
    public static List<String> getSynonyms(String word) {
        List<String> synonyms = new ArrayList<>();
        try {
            URL url = new URL("https://api.datamuse.com/words?rel_syn=" + word);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // Parse JSON response to extract synonyms
            // This is a simplified example; you may want to use a JSON library like Jackson or Gson.
            String[] words = content.toString().split("\"word\":\"");
            for (int i = 1; i < words.length; i++) {
                synonyms.add(words[i].split("\"")[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return synonyms;
    }
}