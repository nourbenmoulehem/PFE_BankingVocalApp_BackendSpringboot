package com.attijari.vocalbanking.Reclamations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client") // for administrators
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class ReclamationController {

    private final ReclamationService reclamationService;

    @PostMapping("/reclamation/add")
    public ResponseEntity<?> addReclamation(@RequestBody Reclamation reclamation) {
        return ResponseEntity.ok(reclamationService.addReclamation(reclamation));
    }

    @GetMapping("/reclamation/getReclamations")
    public ResponseEntity<?> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());

    }

    @PostMapping("/reclamation/reclamationResponse")
    public ResponseEntity<?> insertAssistantResponse(@RequestBody Reclamation reclamation) {
        return ResponseEntity.ok(reclamationService.insertAssistantResponse(reclamation));

    }

    @DeleteMapping("/reclamation/delete/{id}")
    public ResponseEntity<?> deleteReclamation(@PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.deleteReclamation(id));
    }


}
