package com.attijari.vocalbanking.Carte;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carte")
@RequiredArgsConstructor
public class CarteController {
    private final CarteService carteService;
    @PutMapping("/status/{id_produit}")
    public ResponseEntity<Carte> updateCarteStatus(@PathVariable int id_produit, @RequestBody String newStatus) {
        Carte updatedCarte = carteService.updateCarteStatus(id_produit, newStatus);
        if (updatedCarte == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedCarte);
    }
}
