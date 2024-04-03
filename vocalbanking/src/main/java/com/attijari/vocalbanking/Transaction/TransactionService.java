package com.attijari.vocalbanking.Transaction;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaireRepository;
import com.attijari.vocalbanking.Virement.Virement;
import com.attijari.vocalbanking.exceptions.InvalidDatesException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CompteBancaireRepository compteBancaireRepository;

    public CompteBancaire insertTransactionsToCompteBancaire(Long idCompteBancaire, List<Transaction> transactions) {
        Optional<CompteBancaire> compteBancaireOptional = compteBancaireRepository.findById(idCompteBancaire);

        if(compteBancaireOptional.isPresent()) {
            CompteBancaire compteBancaire = compteBancaireOptional.get();
            for (Transaction transaction : transactions) {
                transaction.setCompteBancaire(compteBancaire);

                transactionRepository.save(transaction);
            }
            // insert ALL* transaction to compteBancaire
            compteBancaire.setTransactions(transactions);
            compteBancaireRepository.save(compteBancaire);
            return compteBancaire;
        } else {
            throw new RuntimeException("Compte bancaire not found");
        }

    }

    public List<Transaction> getLastNRows(int n) {
        return transactionRepository.findLastNRows(n);
    }

    public List<Transaction> getOperationByDates(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new InvalidDatesException("Start date must be before end date");
        }
        return transactionRepository.findOperationsBetweenDates(startDate, endDate);

    }
}
