package com.attijari.vocalbanking.Reclamations;

import com.attijari.vocalbanking.Client.Client;
import com.attijari.vocalbanking.Client.ClientRepository;
import com.attijari.vocalbanking.notification.Notification;
import com.attijari.vocalbanking.notification.NotificationRepository;
import com.attijari.vocalbanking.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final NotificationRepository notificationRepository;
    private final ClientRepository clientRepository;

    LocalDate today = LocalDate.now();
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    public Object addReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }




    public Object insertAssistantResponse(InsertAssistantResponseRequest reclamation) {
        Optional<Reclamation> recdb = reclamationRepository.findById(reclamation.getReclamationId());
        Optional<Client> clientDb = clientRepository.findById(reclamation.getClientId());
        if (!recdb.isPresent() && !clientDb.isPresent()) {
            throw new RuntimeException("Reclamation introuvable ou client introuvable");
        } else {
            recdb.get().setDescriptionAssistant(reclamation.getDescriptionAssistant());
            Client client = clientDb.get();
            Notification notification = Notification.builder()
                    .notif(reclamation.getDescriptionAssistant())
                    .notifDate(java.sql.Date.valueOf(today))
                    .type(NotificationType.assistant)
                    .client(client)
                    .build();
            notificationRepository.save(notification);

            return reclamationRepository.save(recdb.get());

        }
    }

    public String deleteReclamation(Long id) {
        reclamationRepository.deleteById(id);
        return "Reclamation suprimée avec succès";
    }
}
