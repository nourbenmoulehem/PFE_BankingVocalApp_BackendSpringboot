package com.attijari.vocalbanking.notification;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class NotificationController {

        private final NotificationService notificationService;
        @GetMapping("/{clientId}/notifications")
        public List<Notification> getNotificationsByClientId(@PathVariable Long clientId) {
                System.out.println("clientId = " + clientId);
                return notificationService.getNotificationsByClientId(clientId);
        }
}
