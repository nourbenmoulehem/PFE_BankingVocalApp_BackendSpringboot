package com.attijari.vocalbanking.Client;

import lombok.Getter;

import java.util.List;

@Getter
public class IntentResponse {
    private String intent;
    private List<List<String>> entities;
}


