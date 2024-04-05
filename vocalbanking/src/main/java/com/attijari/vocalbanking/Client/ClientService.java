package com.attijari.vocalbanking.Client;

import com.attijari.vocalbanking.Beneficiare.BeneficiaireService;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaireService;
import com.attijari.vocalbanking.Transaction.Transaction;
import com.attijari.vocalbanking.Transaction.TransactionService;
import com.attijari.vocalbanking.Virement.Virement;
import com.attijari.vocalbanking.Virement.VirementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final CompteBancaireService compteBancaireService;

    // for vocal
    private  final BeneficiaireService beneficiaireService;
    private final VirementService virementService;
    private final TransactionService transactionService;
    NumberFormat frenchNumberFormat = NumberFormat.getInstance(Locale.FRENCH);
    ObjectMapper objectMapper = new ObjectMapper();

    public Client getClientByCin(String cin) {
        return clientRepository.findByCin(cin);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }


    private RestTemplate restTemplate = new RestTemplate();

    public String sendRequestToFlask(String[] prompts, Long clientId) {

        String flaskUrl = "http://localhost:5000/predict"; // Flask server URL

        // Create request body
        Map<String, String[]> requestBody = new HashMap<>();

        requestBody.put("prompts",  prompts);

        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create request entity
        HttpEntity<Map<String, String[]>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send POST request
        ResponseEntity<List<IntentResponse>> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<IntentResponse>>() {});

        List<IntentResponse> intentResponses = response.getBody();

        System.out.println("Intent Responses: " + intentResponses);

        // Check if all intents are the same
        String intent = null;
        for (IntentResponse intentResponse : intentResponses) {
            if (intent == null) {
                intent = intentResponse.getIntent();
            } else if (!intentResponse.getIntent().equals(intent)) {
                return "Désolé, je n'ai pas compris votre commande. Pouvez-vous la répéter s'il vous plaît ?";
            }
        }
        System.out.println("Common Intent: " + intent);

        String feedback = "";
        float solde = 0;
        String name = "";
        if(clientId == null) {
            return "client est null";
        } else {


        }

        int soldeInt = (int) solde;

        if(intent.equals("consulter_solde")) {
            System.out.println("Client ID: " + clientId);
            Client client = clientRepository.findById(clientId).orElse(null);
            System.out.println("Client: " + client.getCompteBancaire().getSolde());
            solde = client.getCompteBancaire().getSolde();

            name = client.getFirstName();
            feedback = "Vous avez " + (int)soldeInt + " dinars dans votre compte, " + name + ".";
            return feedback;
        } else if(intent.equals("virement")) {
            System.out.println("Hello I'm here ");
            feedback = handleVirementIntent(intentResponses, clientId);
            System.out.println("Feedback: " + feedback);
            return feedback;
        } else if(intent.equals("consulter_mouvements")) {
            feedback = handleConsulterVirementIntent(intentResponses, clientId, intent);
            System.out.println("HELLOOOOOOO");
            return feedback;
        } else if(intent.equals("consulter_historique_virements")) {
            feedback = handleConsulterVirementIntent(intentResponses, clientId, intent);
            System.out.println("HELLOOOOOOO");
            return feedback;
        }
        else {
            return "Désolé, je n'ai pas compris votre commande. Pouvez-vous la répéter s'il vous plaît ?";
        }




    }

    private String handleVirementIntent(List<IntentResponse> intentResponses, Long clientId) {
        List<String> namesInDatabase = beneficiaireService.getNomsByClientId(clientId);
        namesInDatabase = namesInDatabase.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        String feedback = "";

        for (IntentResponse intentResponse : intentResponses) {
            List<List<String>> entities = intentResponse.getEntities();
            String amountOfMoney = null;
            String person = null;
            for (List<String> entity : entities) {
                String entityType = entity.get(0);
                System.out.println("Entity Type: " + entityType);
                String entityValue = entity.get(1);
                System.out.println("Entity Value: " + entityValue);
                if (entityType.equals("amount_of_money")) {
                    amountOfMoney = entityValue;
                } else if (entityType.equals("person")) {
                    person = entityValue;
                }
            }
            if (amountOfMoney != null && person != null) {
                System.out.println("Amount of Money: " + amountOfMoney);
                System.out.println("Person: " + person);
                if (namesInDatabase.contains(person)) {
                    // TODO: Check if amount of money is valid and send money to that person
                    return "Virement de " + amountOfMoney + " à " + person + " a été effectué.";

                }
            } else {
                if (amountOfMoney == null) {
                    feedback = "montant d'argent non reconnu..";
                } else {
                    feedback = "Personne avec ce nom n'a été trouvée.";
                }
            }
        }
        return feedback;
    }

    private String handleConsulterVirementIntent(List<IntentResponse> intentResponses, Long clientId, String intent) {
        System.out.println("Handling consulter historique virements intent");
        String feedback = "";
        Date startDate = null;
        Date endDate = null;
        StringBuilder feedbackBuilder = new StringBuilder();
        IntentResponse intentResponse = intentResponses.get(0);
        System.out.println("Intent Response: " + intentResponse.getIntent());
        List<List<String>> entities = intentResponse.getEntities();

        if (entities.size() == 0) {
            return "Désolé, pas de entités trouvées. Pouvez-vous répéter votre commande s'il vous plaît ?";
        }
        for (List<String> entity : entities) {
            String entityType = entity.get(0);
            System.out.println("Entity Type: " + entityType);
            String entityValue = entity.get(1);
            System.out.println("Entity Value: " + entityValue);

            if(entityType.equals("number")) {
                System.out.println("Entity Value is numeric");
                int num = Integer.parseInt(entityValue);
                if(num < 0) {
                    return "Désolé, le nombre doit être positif.";
                }
                if(num > 10) {
                    return "Désolé, le nombre doit être inférieur à 10.";
                }
                if(intent.equals("consulter_historique_virements")) {
                    List<Virement> virList = virementService.getLastNRows(num);
                    for (Virement virement : virList) {
                        feedbackBuilder.append("Virement avec ID: ").append(virement.getVir_id()).append("\n");
//                    feedbackBuilder.append("Beneficiare: ").append(virement.getLibelle()).append("\n");
//                    feedbackBuilder.append("Banque: ").append(virement.getBank()).append("\n");
                        feedbackBuilder.append("Montant: ").append(virement.getMontant()).append("\n");
                        feedbackBuilder.append("Motif: ").append(virement.getMotif()).append("\n");
                        feedbackBuilder.append("État: ").append(virement.getEtat()).append("\n");
                        feedbackBuilder.append("Date operation: ").append(virement.getDateOperation()).append("\n");
                        // Add more information as needed
                        feedbackBuilder.append("\n");
                    }
                    return feedbackBuilder.toString();
                }
                else if(intent.equals("consulter_mouvements")) {
                    // TODO handle retrieving mouvement by last n number
                    List<Transaction> transactions = transactionService.getLastNRows(num);

                    for (Transaction transaction : transactions) {
                        feedbackBuilder.append("mouvement avec ID: ").append(transaction.getTran_id()).append("\n");
                        feedbackBuilder.append("Libellé: ").append(transaction.getTran_canal()).append("\n");
                        feedbackBuilder.append("Date opération: ").append(transaction.getDateOperationInFrench()).append("\n");
                        feedbackBuilder.append("Date valeur: ").append(transaction.getDateValeurInFrench()).append("\n");
                        feedbackBuilder.append("mouvement  ").append(transaction.getTran_type()).append("\n");
                        feedbackBuilder.append("montant  ").append(transaction.getMontant()).append("\n");
                        // Add more information as needed
                        feedbackBuilder.append("\n");
                    }
                    return feedbackBuilder.toString();
                } else {
                    return "Essayer une autre fois";
                }

            } if (entityType.equals("date")) {

                Date date = convertToDate(entityValue);
                if (startDate == null) {
                    startDate = date;
                } else {
                    endDate = date;
                }
                System.out.println("date");

                if (startDate != null && endDate != null) {
                    // Now you have startDate and endDate
                    // Use these dates to query your VirementService
                    if(intent.equals("consulter_historique_virements")) {
                        List<Virement> virements = virementService.getVirementByDates(startDate, endDate);
                        for (Virement virement : virements) {
                            // Construct feedback based on virement details
                            feedbackBuilder.append(virement.getDateOperationInFrench()).append("\n");
                            feedbackBuilder.append("Virement avec ID: ").append(virement.getVir_id()).append("\n");
                            feedbackBuilder.append("Montant: ").append(virement.getMontant()).append("\n");
                            feedbackBuilder.append("Motif: ").append(virement.getMotif()).append("\n");
                            feedbackBuilder.append("État: ").append(virement.getEtat()).append("\n");
                            feedbackBuilder.append("Date operation: ").append(virement.getDateOperation()).append("\n");
                            feedbackBuilder.append("\n");
                        }
                        return feedbackBuilder.toString();
                    } else if (intent.equals("consulter_mouvements")) {
                        // TODO handle consulter mouvement between dates
                        List<Transaction> transactions = transactionService.getOperationByDates(startDate, endDate);
                        for (Transaction transaction : transactions) {
                            // Construct feedback based on virement details
                            feedbackBuilder.append(transaction.getDateOperationInFrench()).append("\n");
                            feedbackBuilder.append("mouvement avec ID: ").append(transaction.getTran_id()).append("\n");
                            feedbackBuilder.append("Libellé: ").append(transaction.getTran_canal()).append("\n");
                            feedbackBuilder.append("Date opération: ").append(transaction.getDateOperationInFrench()).append("\n");
                            feedbackBuilder.append("Date valeur: ").append(transaction.getDateValeurInFrench()).append("\n");
                            feedbackBuilder.append("mouvement  ").append(transaction.getTran_type()).append("\n");
                            feedbackBuilder.append("montant  ").append(transaction.getMontant()).append("\n");
                            feedbackBuilder.append("\n");
                        }
                        return feedbackBuilder.toString();


                    } else {
                        return "Essayer une autre fois";
                    }
                }
            }
            else {
                feedback = "Désolé, je n'ai pas compris votre commande. Pouvez-vous la répéter s'il vous plaît ?";
            }
        }


        return feedback;
    }

    public static boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    public static boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }

    private static Date convertToDate(String dateString) {
        // Split the input string into day and month
        String[] parts = dateString.split(" ");
        int day = Integer.parseInt(parts[0]);

        // Map the French month name to its corresponding English name
        Map<String, String> monthMap = new HashMap<>();
        monthMap.put("janvier", "JANUARY");
        monthMap.put("février", "FEBRUARY");
        monthMap.put("mars", "MARCH");
        monthMap.put("avril", "APRIL");
        monthMap.put("mai", "MAY");
        monthMap.put("juin", "JUNE");
        monthMap.put("juillet", "JULY");
        monthMap.put("août", "AUGUST");
        monthMap.put("septembre", "SEPTEMBER");
        monthMap.put("octobre", "OCTOBER");
        monthMap.put("novembre", "NOVEMBER");
        monthMap.put("décembre", "DECEMBER");

        String englishMonth = monthMap.get(parts[1].toLowerCase());

        // Map the English month name to its corresponding Month enum value
        Month month = Month.valueOf(englishMonth.toUpperCase(Locale.ENGLISH));

        // Construct a LocalDate object using the current year
        LocalDate localDate = LocalDate.of(LocalDate.now().getYear(), month, day);

        // Construct a Date object
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
