package com.attijari.vocalbanking.Client;

public class IntentRequest {
    private String prompt;
    private Long id;

    // Getter and Setter for 'prompt' field
    public String getPrompt() {
        return prompt;
    }

    public Long getId() {
        return id;
    }


    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}