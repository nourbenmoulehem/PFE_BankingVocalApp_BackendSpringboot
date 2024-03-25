package com.attijari.vocalbanking.Virement;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/operation/virement")
@RequiredArgsConstructor
public class VirementController {
    private final VirementService virementService;

    @GetMapping
    public List<Virement> getAllVirements() {
        return virementService.getAllVirements();
    }

    // create an end point to insert virement to compteBancaire by id_compteBancaire
    @PostMapping("/compteBancaire/{id_compteBancaire}")
    public void insertVirementsToCompteBancaire(@PathVariable Long id_compteBancaire, @RequestBody List<Virement> virements) {
        System.out.println("id_compteBancaire = " + id_compteBancaire);
        virementService.insertVirementsToCompteBancaire(id_compteBancaire, virements);
    }

    // get virement by id
    @GetMapping("/{id}")
    public Virement getVirementById(@PathVariable Long id) {
        return virementService.getVirementById(id);
    }

}
