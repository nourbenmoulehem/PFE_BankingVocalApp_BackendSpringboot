package com.attijari.vocalbanking.IntentClassification;

import com.attijari.vocalbanking.Client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/intent")
@RequiredArgsConstructor
public class IntentController {

    private final IntentService intentService;
    private final ClientService clientService;

    @PostMapping("/getIntent")
    public String getIntent(@RequestBody String prompt) {
        System.out.println("Prompt: " + prompt);
        return "hello";

    }

    @GetMapping("/intent2")
    public String getAllClients(@PathVariable String prompt) {
        System.out.println("Prompt: " + prompt);
        return "hello" + prompt;


    }

}

