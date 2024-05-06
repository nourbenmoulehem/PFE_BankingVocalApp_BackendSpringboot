package com.attijari.vocalbanking.Carte;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/carte")
@RequiredArgsConstructor
public class CarteController {
    private final CarteService carteService;
    private static final Logger logger = LoggerFactory.getLogger(CarteController.class);

    @PutMapping("/status/{id_produit}")
    public ResponseEntity<Carte> updateCarteStatus(@PathVariable int id_produit,  @RequestBody Map<String, String> body) {
        String newStatus = body.get("status");
        logger.info("Received request to update carte status. id_produit: {}, newStatus: {}", id_produit, newStatus);
        Carte updatedCarte = carteService.updateCarteStatus(id_produit, newStatus);
        if (updatedCarte == null) {
            logger.warn("No Carte found with id_produit: {}", id_produit);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Carte status updated successfully. id_produit: {}, newStatus: {}", id_produit, newStatus);
        return ResponseEntity.ok(updatedCarte);
    }
}
