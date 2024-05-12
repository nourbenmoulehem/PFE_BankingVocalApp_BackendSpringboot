package com.attijari.vocalbanking.Reclamations;

import com.attijari.vocalbanking.Client.Client;
import com.attijari.vocalbanking.Client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client") // for administrators
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class ReclamationController {

    private final ReclamationService reclamationService;
    private final ClientService clientService;

    @PostMapping("/reclamation/add")
    public ResponseEntity<?> addReclamation(@RequestBody Reclamation reclamation) {
        return ResponseEntity.ok(reclamationService.addReclamation(reclamation));
    }
    @PostMapping("/reclamation/add/{clientId}")
    public ResponseEntity<?> addReclamationByID(@PathVariable Long clientId, @RequestBody Reclamation reclamation) {
        Client client = clientService.getClientById(clientId);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client introuvable");
        }
        reclamation.setClient(client);
        return ResponseEntity.ok(reclamationService.addReclamation(reclamation));
    }

    @GetMapping("/reclamation/getReclamations")
    public ResponseEntity<?> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());

    }

    @PostMapping("/reclamation/reclamationResponse")
    public ResponseEntity<?> insertAssistantResponse(@RequestBody InsertAssistantResponseRequest reclamation) {
        System.out.println("reclamation clid = " + reclamation.getClientId());
        return ResponseEntity.ok(reclamationService.insertAssistantResponse(reclamation));

    }

    @DeleteMapping("/reclamation/delete/{id}")
    public ResponseEntity<?> deleteReclamation(@PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.deleteReclamation(id));
    }


}
