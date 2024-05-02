package com.attijari.vocalbanking.Operation;



import com.attijari.vocalbanking.exceptions.InvalidDatesException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(OperationController.class);

    @GetMapping("/getLastMonthExpenses/{id}")
    public ResponseEntity<?> getLastMonthExpenses(@PathVariable Long id) {
        try {
            logger.info("Received request to get last month expenses. id: {}", id);
            ResponseEntity<?> response = ResponseEntity.ok(operationService.getSumMontantlastMonth(id));
            logger.info("Last month expenses retrieved successfully. id: {}", id);
            return response;
        } catch (Exception e) {
            logger.error("Error occurred while retrieving last month expenses. id: {}, error: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getCurrentMonthExpenses/{id}")
    public ResponseEntity<?> getCurrentMonthExpenses(@PathVariable Long id) {
        try {
            logger.info("Received request to get current month expenses. id: {}", id);
            ResponseEntity<?> response = ResponseEntity.ok(operationService.getSumMontantCurrentMonth(id));
            logger.info("Current month expenses retrieved successfully. id: {}", id);
            return response;
        } catch (Exception e) {
            logger.error("Error occurred while retrieving current month expenses. id: {}, error: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
