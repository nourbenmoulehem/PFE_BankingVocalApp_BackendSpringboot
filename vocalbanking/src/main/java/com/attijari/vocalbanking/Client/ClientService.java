package com.attijari.vocalbanking.Client;

import com.attijari.vocalbanking.Client.Client;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaireService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.attijari.vocalbanking.Client.ClientRepository;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final CompteBancaireService compteBancaireService;

    public Client getClientByCin(String cin) {
        return clientRepository.findByCin(cin);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }


    private RestTemplate restTemplate = new RestTemplate();

    public String sendRequestToFlask(String prompt) {
        String flaskUrl = "http://localhost:5000/predict"; // Flask server URL

        // Create request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);

        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create request entity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send POST request
        ResponseEntity<IntentResponse> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, requestEntity, IntentResponse.class);

        response.getBody().getIntent();

        String intent = response.getBody().getIntent();
        List<List<String>> entities = response.getBody().getEntities();
        System.out.println("Intent: " + intent);
        System.out.println("Entities: " + entities);
        String feedback = "";
        if(intent.equals("consulter_solde")) {
            feedback = "Vous avez 1000 dinars dans votre compte.";
            return feedback;
        } else if(intent.equals("virements")) {
            System.out.println("Hello I'm here ");
            String amountOfMoney = extractAmountOfMoney(entities);
            System.out.println("Amount of money: " + amountOfMoney);
            feedback = "Virement de " + amountOfMoney + " à effectuer.";
            return feedback;
        } else if(intent.equals("transfer_money")) {
            return "transfer_money";
        } else {
            return "unknown";
        }

        // Return response body



    }

    private String extractAmountOfMoney(List<List<String>> entities) {
        for (List<String> entity : entities) {
            if (entity.size() == 2 && entity.get(0).equals("amount_of_money")) {
                return entity.get(1);
            }
        }
        return "Unknown";
    }
}
