package com.attijari.vocalbanking.Client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;


    @GetMapping("/clients")
    public ResponseEntity<?> getAllClients() {
        // Get all clients from the service
        List<Client> clients = clientService.getAllClients();

        // Check if clients list is not empty
        if (!clients.isEmpty()) {
            return ResponseEntity.ok(clients);
        } else {
            // If clients list is empty, return 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    // only for testing, need to be modified asap
    @GetMapping("/get-by-cin")
    @ResponseBody
    public Client getClientByCin(@RequestParam("cin") String cin) {
        return clientService.getClientByCin(cin);
    }

    @PostMapping("/getById/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        System.out.println("helloooo");
        System.out.println("id = " + id);

        // Get client by id from the service
        Client client = clientService.getClientById(id);

        // Check if client is not null
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            // If client is null, return 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/getIntent")
    public ResponseEntity<Map<String, String>> getIntent(@RequestBody IntentRequest request) {
        String[] prompts = request.getPrompts();
        System.out.println("Prompts: " + prompts);
        Long clientId = request.getClientId();
        System.out.println("id: " + clientId);
        System.out.println("Prompts: " + prompts);
        String feedback = clientService.sendRequestToFlask(prompts, clientId);

        Map<String, String> response = new HashMap<>();
        response.put("assistantResponse", feedback);

        return ResponseEntity.ok(response);
    }
}
