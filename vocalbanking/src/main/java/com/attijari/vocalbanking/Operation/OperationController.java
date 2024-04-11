package com.attijari.vocalbanking.Operation;



import com.attijari.vocalbanking.exceptions.InvalidDatesException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/operation/mouvement")
@RequiredArgsConstructor
public class OperationController {
    private final OperationService operationService;

    @PostMapping("/compteBancaire/{id_compteBancaire}")
    public void insertOperationsToCompteBancaire(@PathVariable Long id_compteBancaire, @RequestBody List<Operation> operations) {
        System.out.println("id_compteBancaire = " + id_compteBancaire);
        operationService.insertTransactionsToCompteBancaire(id_compteBancaire, operations);
    }

    @PostMapping("/all")
    public ResponseEntity<?> getAllOperations(@RequestBody RequestGetByClientId request) {
        return operationService.getAllOperations(request.getClientId());
    }

    @GetMapping("/lastNRows")
    public List<Operation> getLastNRows(@RequestParam int n) {
        return operationService.getLastNRows(n);
    }


    @GetMapping("/byDate")
    public ResponseEntity<?> getVirementByDate(
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam("clientId") Long clientId) {
        try {
            List<Operation> operations = operationService.getOperationByDates(startDate, endDate, clientId);
            return ResponseEntity.ok(operations);
        } catch (InvalidDatesException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
