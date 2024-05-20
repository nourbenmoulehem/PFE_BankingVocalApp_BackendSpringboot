package com.attijari.vocalbanking.IntentClassification;

import com.attijari.vocalbanking.Client.ClientService;
import com.attijari.vocalbanking.Client.IntentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/intent")
@RequiredArgsConstructor
public class IntentController {

    private final IntentService intentService;
    private final ClientService clientService;


    @PostMapping("/getIntent")
    public ResponseEntity<Map<String, String>> getIntent(@RequestBody IntentRequest request) {
        String[] prompts = request.getPrompts();
        System.out.println("Prompts: " + prompts);
        Long clientId = request.getClientId();
        System.out.println("id: " + clientId);
        System.out.println("Prompts: " + prompts);
        String feedback = intentService.sendRequestToFlask(prompts, clientId);

        Map<String, String> response = new HashMap<>();
        response.put("assistantResponse", feedback);

        return ResponseEntity.ok(response);
    }

}

