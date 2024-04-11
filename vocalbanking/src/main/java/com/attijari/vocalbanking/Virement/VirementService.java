package com.attijari.vocalbanking.Virement;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaireRepository;
import com.attijari.vocalbanking.exceptions.InvalidDatesException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Date;
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

    public List<Virement> getLastNRows(int n) {
        return virementRepository.findLastNRows(n);
    }

    public List<Virement> getVirementByDates(Date startDate, Date endDate, Long clientId) {
        if (startDate.after(endDate)) {
            throw new InvalidDatesException("Start date must be before end date");
        }
        CompteBancaire compteBancaire = compteBancaireRepository.findByClientID(clientId);
        int idCompteBancaire = compteBancaire.getId_compteBancaire();
        return virementRepository.findVirementsBetweenDatesAndByCompteBancaireId(startDate, endDate, idCompteBancaire);

    }

    public ResponseEntity<?> getAllVirementsByClientId(Long clientId) {
        CompteBancaire compteBancaire = compteBancaireRepository.findByClientID(clientId);

        List <Virement> virements = virementRepository.findByCompteBancaire(compteBancaire);
        if (virements.isEmpty()) {
            return ResponseEntity.ok("Aucun virement trouvé");
        }
        return ResponseEntity.ok(virements);
    }
}
