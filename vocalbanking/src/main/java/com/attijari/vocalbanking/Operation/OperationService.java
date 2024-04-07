package com.attijari.vocalbanking.Operation;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaireRepository;
import com.attijari.vocalbanking.exceptions.InvalidDatesException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;
    private final CompteBancaireRepository compteBancaireRepository;

    public CompteBancaire insertTransactionsToCompteBancaire(Long idCompteBancaire, List<Operation> operations) {
        Optional<CompteBancaire> compteBancaireOptional = compteBancaireRepository.findById(idCompteBancaire);

        if(compteBancaireOptional.isPresent()) {
            CompteBancaire compteBancaire = compteBancaireOptional.get();
            for (Operation operation : operations) {
                operation.setCompteBancaire(compteBancaire);

                operationRepository.save(operation);
            }
            // insert ALL* transaction to compteBancaire
            compteBancaire.setOperations(operations);
            compteBancaireRepository.save(compteBancaire);
            return compteBancaire;
        } else {
            throw new RuntimeException("Compte bancaire not found");
        }

    }

    public List<Operation> getLastNRows(int n) {
        return operationRepository.findLastNRows(n);
    }

    public List<Operation> getOperationByDates(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new InvalidDatesException("Start date must be before end date");
        }
        return operationRepository.findOperationsBetweenDates(startDate, endDate);

    }
}
