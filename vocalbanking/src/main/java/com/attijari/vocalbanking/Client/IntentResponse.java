package com.attijari.vocalbanking.Client;

import lombok.Getter;

import java.util.List;

@Getter
public class IntentResponse {
    private List<List<String>> entities;
    private String intent;

    // Getters and setters for 'entities' and 'intent' fields
    // Omitted for brevity
}

