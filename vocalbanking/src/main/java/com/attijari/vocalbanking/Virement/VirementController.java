package com.attijari.vocalbanking.Virement;

import com.attijari.vocalbanking.Operation.RequestGetByClientId;
import com.attijari.vocalbanking.exceptions.InsufficientBalanceException;
import com.attijari.vocalbanking.exceptions.InvalidBeneficiaryException;
import com.attijari.vocalbanking.exceptions.InvalidDatesException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/operation/virement")
@RequiredArgsConstructor
public class VirementController {
    private final VirementService virementService;
    private final Logger logger = LoggerFactory.getLogger(VirementController.class);
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

    // get n last virements
    @GetMapping("/lastNRows")
    public List<Virement> getLastNRows(@RequestParam int n) {
        return virementService.getLastNRows(n);
    }

    @PostMapping("/all")
    public ResponseEntity<?> getAllVirementsByClientId(@RequestBody RequestGetByClientId request) {
        return virementService.getAllVirementsByClientId(request.getClientId());
    }

    @GetMapping("/byDate")
    public ResponseEntity<?> getVirementByDate(
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam("clientId") Long clientId)
    {
        try {
            List<Virement> virements = virementService.getVirementByDates(startDate, endDate, clientId);
            return ResponseEntity.ok(virements);
        } catch (InvalidDatesException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/initiation-virement")
    public ResponseEntity<?> initiateTransfer(@RequestBody VirementRequest request) {
        logger.info("Received request to initiate transfer");
        try {
            String response = virementService.initiateTransfer(request);
            logger.info("Transfer initiated successfully, sending response...");
            return ResponseEntity.ok(response);
        } catch (InsufficientBalanceException | InvalidBeneficiaryException e) {
            logger.error("Error while initiating transfer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/verifier-virement/{virementId}")
    public ResponseEntity<?> verifyTransfer(@PathVariable Long virementId) {
        try {
            Virement virement = virementService.verifyTransfer(virementId);
            VirementResponse response = VirementResponse.builder()
                    .libelle(virement.getLibelle())
                    .dateOperation(virement.getDateOperation())
                    .bank(virement.getBank())
                    .montant(virement.getMontant())
                    .motif(virement.getMotif())
                    .etat(virement.getEtat())
                    .beneficiaire(virement.getBeneficiaire())
                    .build();
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("your_mobile_app_url_here")).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
