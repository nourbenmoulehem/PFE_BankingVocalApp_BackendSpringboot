package com.attijari.vocalbanking.Beneficiare;

import com.attijari.vocalbanking.Transaction.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
