package com.attijari.vocalbanking.Transaction;


import com.attijari.vocalbanking.Virement.Virement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
