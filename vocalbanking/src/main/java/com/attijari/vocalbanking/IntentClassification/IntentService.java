package com.attijari.vocalbanking.IntentClassification;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class IntentService {
    private static final String FLASK_APP_URL = "http://localhost:5000"; // replace with your Flask app's URL

//    public String getIntent(String prompt) {
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_APP_URL, prompt, String.class);
//        return response.getBody();
//    }

    private RestTemplate restTemplate = new RestTemplate();

    public String sendRequestToFlask(String prompt) {
        String flaskUrl = "http://localhost:5000/predict"; // replace with your Flask server URL

        // Create request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);

        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create request entity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send POST request
        ResponseEntity<String> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, requestEntity, String.class);

        // Return response body
        return response.getBody();
    }
}

