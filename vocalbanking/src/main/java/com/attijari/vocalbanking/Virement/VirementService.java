package com.attijari.vocalbanking.Virement;

import com.attijari.vocalbanking.Beneficiare.Beneficiaire;

import com.attijari.vocalbanking.Beneficiare.BeneficiaireService;
import com.attijari.vocalbanking.Beneficiare.BeneficiareRepository;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaireRepository;
import com.attijari.vocalbanking.Operation.CanalType;
import com.attijari.vocalbanking.Operation.Operation;
import com.attijari.vocalbanking.Operation.OperationRepository;
import com.attijari.vocalbanking.Operation.OperationType;
import com.attijari.vocalbanking.authentication.EmailVerificationService;
import com.attijari.vocalbanking.exceptions.InsufficientBalanceException;

import com.attijari.vocalbanking.exceptions.InvalidDatesException;
import com.attijari.vocalbanking.notification.Notification;
import com.attijari.vocalbanking.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.attijari.vocalbanking.notification.NotificationType;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VirementService {
    private final VirementRepository virementRepository;
    private final CompteBancaireRepository compteBancaireRepository;
    private final BeneficiaireService beneficiareService;
    private final EmailVerificationService emailVerificationService;
    private final BeneficiareRepository beneficiaireRepository;
    private final OperationRepository operationRepository;
    private final NotificationRepository notificationRepository;
    private final Logger logger = LoggerFactory.getLogger(VirementService.class);

    public CompteBancaire insertVirementsToCompteBancaire(Long idCompteBancaire, List<Virement> virements) {
        System.out.println("Virements = " + virements);
        Optional<CompteBancaire> compteBancaireOptional = compteBancaireRepository.findById(idCompteBancaire);
        Long idClient = compteBancaireOptional.get().getClient().getClientId();
        List<Beneficiaire> beneficiares = beneficiareService.getBeneficiairesByClient(idClient);
        if (compteBancaireOptional.isPresent()) {
            CompteBancaire compteBancaire = compteBancaireOptional.get();
            int counter = 1;
            for (Virement virement : virements) {
                virement.setCompteBancaire(compteBancaire);
                virement.setBeneficiaire(beneficiares.get(counter));
                virementRepository.save(virement);
                counter++;
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

        List<Virement> virements = virementRepository.findByCompteBancaire(compteBancaire);
        if (virements.isEmpty()) {
            return ResponseEntity.ok("Aucun virement trouvé");
        }
        return ResponseEntity.ok(virements);
    }

    public String initiateTransfer(VirementRequest request) throws InsufficientBalanceException {

        // Retrieve the CompteBancaire by the client ID
        CompteBancaire compteBancaire = compteBancaireRepository.findByClientID(request.getClientId()); // changed from findById to findByClientID
//                .orElseThrow(() -> new NoSuchElementException("Client not found"));

        if (compteBancaire == null) {
            return "Compte bancaire inexistant";
        }
        // Check if the beneficiary exists and is related to the client
        Beneficiaire beneficiaire = beneficiaireRepository.findByRIBandClientId(request.getRIB(), request.getClientId());
        if (beneficiaire == null) {
            return "Beneficiaire inexistant";
        }
        // Retrieve virements by the CompteBancaire
        List<Virement> virements = virementRepository.findByCompteBancaire(compteBancaire);

        // Check if the client has any virement with status 'initié'
        for (Virement virement : virements) {
            if (virement.getEtat() == EtatVirement.initié) {
                return "Vous avez déjà un virement en cours d'initiation, veuillez le vérifier avant d'en initier un autre";
            }

        }

        // Check if the client has enough balance
        if (compteBancaire.getSolde() < request.getMontant()) {
//            throw new InsufficientBalanceException("solde insuffisant");
            return "Solde insuffisant";
        }


        logger.info("Initiating transfer...");
        // Create a new Virement
        Virement virement = new Virement();
        virement.setCompteBancaire(compteBancaire);
        virement.setBeneficiaire(beneficiaire);
        virement.setMontant(request.getMontant());
        virement.setMotif(request.getMotif());
        virement.setEtat(EtatVirement.initié);
        virement.setBank("webank");

        //setting the dateOperation
        // Get the current date
        Date dateOperation = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOperation);
        // Get the day of the month
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Calculate dateValeur
        if (dayOfMonth >= 1 && dayOfMonth <= 15) {
            // If the day of the month is between 1 and 15, subtract 3 days
            calendar.add(Calendar.DAY_OF_MONTH, -3);
        } else {
            // Otherwise, add 3 days
            calendar.add(Calendar.DAY_OF_MONTH, 3);
        }

        // setting date valeur
        Date dateValeur = calendar.getTime();
        virement.setDateOperation(dateOperation);
        virement.setDateValeur(dateValeur);
        virement.setLibelle(LibelleVir.Virement_emis);

        // Save the Virement
        virement = virementRepository.save(virement);
        logger.info("Transfer initiated successfully, sending verification email...");
        // Send the verification email
        emailVerificationService.sendVerifyTransferEmail(virement, compteBancaire.getClient());
        logger.info("Verification email sent successfully");
        return "Transfer initiated successfully";
    }

    public Virement verifyTransfer(Long virementId) {
        // Retrieve the virement
        Virement virement = virementRepository.findById(virementId).orElseThrow(() -> new NoSuchElementException("Virement not found"));
        Operation operation =  Operation.builder()
                .date_operation(virement.getDateOperation())
                .date_valeur(virement.getDateValeur())
                .montant(virement.getMontant())
                .op_canal(CanalType.Virement_emis)
                .op_type(OperationType.Debit)
                .op_emplacement(virement.getBank())
                .op_marchant("application")
                .compteBancaire(virement.getCompteBancaire())
                .build();
        operationRepository.save(operation);
        // Update the solde in the CompteBancaire
        CompteBancaire compteBancaire = virement.getCompteBancaire();
        compteBancaire.setSolde(compteBancaire.getSolde() - virement.getMontant());
        compteBancaireRepository.save(compteBancaire);

        // TODO : update solde beneficiarre if it exists
        String beneficiaireRIB = virement.getBeneficiaire().getRib();
        // find compte bancaire by rub
        CompteBancaire beneficiaireCompte = compteBancaireRepository.findByRIB(beneficiaireRIB);
        if(beneficiaireCompte != null){
            beneficiaireCompte.setSolde(beneficiaireCompte.getSolde() + virement.getMontant());
            compteBancaireRepository.save(beneficiaireCompte);
            //new : saved operation for benificiaire
            Operation operationReciever =  Operation.builder()
                    .date_operation(virement.getDateOperation())
                    .date_valeur(virement.getDateValeur())
                    .montant(virement.getMontant())
                    .op_canal(CanalType.Virement_reçu)
                    .op_type(OperationType.Credit)
                    .op_emplacement(virement.getBank())
                    .op_marchant("application")
                    .compteBancaire(beneficiaireCompte)
                    .build();
            operationRepository.save(operationReciever);
            //new : notification builiding for beneficiaire
            Notification notificationBenef = Notification.builder()
                    .notif("Vous avez reçu un virement de " + virement.getMontant() + " de la part de " + virement.getCompteBancaire().getClient().getFirstName())
                    .notifDate(new Date())
                    .type(NotificationType.virement)
                    .client(beneficiaireCompte.getClient())
                    .build();
            notificationRepository.save(notificationBenef);
        }
        // Create a new notification for client
        Notification notification = Notification.builder()
                .notif("Vous avez envoyer un virement de " + virement.getMontant() + " a " + virement.getBeneficiaire().getNom())
                .notifDate(new Date())
                .type(NotificationType.virement)
                .client(virement.getCompteBancaire().getClient())
                .build();
        notificationRepository.save(notification);
        // Update the virement status
        virement.setEtat(EtatVirement.exécuté);
        virementRepository.save(virement);

        return virement;
    }
}
