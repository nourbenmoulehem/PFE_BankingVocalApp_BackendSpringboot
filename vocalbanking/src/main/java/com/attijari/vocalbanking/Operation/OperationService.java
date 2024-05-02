package com.attijari.vocalbanking.Operation;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaireRepository;
import com.attijari.vocalbanking.exceptions.InvalidDatesException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
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

    public ResponseEntity<?> getAllOperations(Long clientId) {
        CompteBancaire compteBancaire = compteBancaireRepository.findByClientID(clientId);
//        System.out.println("compteBancaire id = " + compteBancaire.getOperations().toString());
//        List <Operation> operations = compteBancaire.getOperations();

        List <Operation> operations = operationRepository.findByCompteBancaire(compteBancaire);
        if (operations.isEmpty()) {
            return ResponseEntity.ok("Aucune opération trouvée");
        }
        return ResponseEntity.ok(operations);
    }

    public List<Operation> getLastNRows(int n) {
        return operationRepository.findLastNRows(n);
    }

    public List<Operation> getOperationByDates(Date startDate, Date endDate, Long clientId) {
        if (startDate.after(endDate)) {
            throw new InvalidDatesException("Start date must be before end date");
        }
        CompteBancaire compteBancaire = compteBancaireRepository.findByClientID(clientId);
        int idCompteBancaire = compteBancaire.getId_compteBancaire();
        return operationRepository.findOperationsBetweenDatesAndByCompteBancaireId(startDate, endDate, idCompteBancaire);

    }

    public float getSumMontantlastMonth( Long clientId) {
        CompteBancaire compteBancaire = compteBancaireRepository.findByClientID(clientId);
        int idCompteBancaire = compteBancaire.getId_compteBancaire();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date sDate = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date eDate = cal.getTime();

        Float sum = operationRepository.sumMontantbyMonth(sDate, eDate, idCompteBancaire);
        return sum != null ? sum : 0.0f;
    }
    public float getSumMontantCurrentMonth(Long clientId) {
        CompteBancaire compteBancaire = compteBancaireRepository.findByClientID(clientId);
        int idCompteBancaire = compteBancaire.getId_compteBancaire();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date sDate = cal.getTime();

        Date eDate = Calendar.getInstance().getTime();

        Float sum = operationRepository.sumMontantbyMonth(sDate, eDate, idCompteBancaire);
        return sum != null ? sum : 0.0f;
    }
}
