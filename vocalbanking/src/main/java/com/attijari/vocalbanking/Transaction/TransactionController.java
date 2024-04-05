package com.attijari.vocalbanking.Transaction;



import com.attijari.vocalbanking.Virement.Virement;
import com.attijari.vocalbanking.exceptions.InvalidDatesException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/operation/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/compteBancaire/{id_compteBancaire}")
    public void insertVirementsToCompteBancaire(@PathVariable Long id_compteBancaire, @RequestBody List<Transaction> transactions) {
        System.out.println("id_compteBancaire = " + id_compteBancaire);
        transactionService.insertTransactionsToCompteBancaire(id_compteBancaire, transactions);
    }

    @GetMapping("/lastNRows")
    public List<Transaction> getLastNRows(@RequestParam int n) {
        return transactionService.getLastNRows(n);
    }


    @GetMapping("/byDate")
    public ResponseEntity<?> getvVirementByDate(
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        try {
            List<Transaction> transactions = transactionService.getOperationByDates(startDate, endDate);
            return ResponseEntity.ok(transactions);
        } catch (InvalidDatesException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
