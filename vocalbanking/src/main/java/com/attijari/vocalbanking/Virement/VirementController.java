package com.attijari.vocalbanking.Virement;

import com.attijari.vocalbanking.Operation.RequestGetByClientId;
import com.attijari.vocalbanking.exceptions.InvalidDatesException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
}
