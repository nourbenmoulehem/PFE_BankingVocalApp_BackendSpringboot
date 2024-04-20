package com.attijari.vocalbanking.notification;

import com.attijari.vocalbanking.Reclamations.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
