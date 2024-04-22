package com.attijari.vocalbanking.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

            private final NotificationRepository notificationRepository;
    public List<Notification> getNotificationsByClientId(Long clientId) {
        return notificationRepository.findByClientClientId(clientId);
    }
}
