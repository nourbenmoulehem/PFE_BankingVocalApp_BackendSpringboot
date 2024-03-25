package com.attijari.vocalbanking.Virement;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VirementService {
    private final VirementRepository virementRepository;
    private final CompteBancaireRepository compteBancaireRepository;

    public CompteBancaire insertVirementsToCompteBancaire(Long idCompteBancaire, List<Virement> virements) {
        System.out.println("Virements = " + virements);
        Optional<CompteBancaire> compteBancaireOptional = compteBancaireRepository.findById(idCompteBancaire);

        if(compteBancaireOptional.isPresent()) {
            CompteBancaire compteBancaire = compteBancaireOptional.get();
            for (Virement virement : virements) {
                virement.setCompteBancaire(compteBancaire);

                virementRepository.save(virement);
            }
            compteBancaire.setVirements(virements);
            compteBancaireRepository.save(compteBancaire);
            return compteBancaire;
        } else {
            throw new RuntimeException("Compte bancaire not found");
        }


    }

    public List<Virement> getAllVirements() {
        return virementRepository.findAll();
    }

    public Virement getVirementById(Long id) {
        return virementRepository.findById(id).orElseThrow(() -> new RuntimeException("Virement not found"));
    }
}
