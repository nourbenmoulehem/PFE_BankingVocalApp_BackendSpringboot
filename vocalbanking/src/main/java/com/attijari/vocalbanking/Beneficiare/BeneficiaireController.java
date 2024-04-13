package com.attijari.vocalbanking.Beneficiare;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client/beneficiaire")
@RequiredArgsConstructor
public class BeneficiaireController {
    private final BeneficiaireService beneficiaireService;

    @PostMapping("/beneficiaires/{id_client}")
    public void insertVirementsToCompteBancaire(@PathVariable Long id_client, @RequestBody List<Beneficiaire> beneficiaires) {
        System.out.println("id_compteBancaire = " + id_client);
        beneficiaireService.saveBeneficiaires(beneficiaires, id_client);
    }

    @GetMapping("/beneficiaires/{id_client}")
    public List<Beneficiaire> getBeneficiairesByClient(@PathVariable Long id_client) {
        return beneficiaireService.getBeneficiairesByClient(id_client);
    }

    @GetMapping("/noms/{id_client}")
    public List<String> getNomsByClientId(@PathVariable Long id_client) {
        return beneficiaireService.getNomsByClientId(id_client);
    }


    // Insert a (one) new beneficiaire
    @PostMapping("/insert-beneficiaire/{id_client}")
    public ResponseEntity<?> insertBeneficiaire(@PathVariable Long id_client, @RequestBody InsertBeneficiaireRequest beneficiaireRequest) {
        try {
            System.out.println("id_client = " + id_client);
            System.out.println("beneficiaire = " + beneficiaireRequest.getNom());
            Beneficiaire beneficiaire = Beneficiaire.builder()
                    .nom(beneficiaireRequest.getNom())
                    .rib(beneficiaireRequest.getRib())
                    .build();
            beneficiaireService.saveBeneficiaire(beneficiaire, id_client);
            return ResponseEntity.ok().body(Map.of("message", "Beneficiaire inserted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/delete-beneficiaire/{id_client}") // delete a (one) new beneficiaire
    public void deleteBeneficiaire(@PathVariable Long id_client, @RequestBody Beneficiaire beneficiaire) {
        beneficiaireService.deleteBeneficiaire(beneficiaire, id_client);
    }

    @PostMapping("/update-beneficiaire/{id_client}") // update a (one) new beneficiaire
    public void updateBeneficiaire(@PathVariable Long id_client, @RequestBody Beneficiaire beneficiaire) {
        beneficiaireService.updateBeneficiaire(beneficiaire, id_client);
    }
}
