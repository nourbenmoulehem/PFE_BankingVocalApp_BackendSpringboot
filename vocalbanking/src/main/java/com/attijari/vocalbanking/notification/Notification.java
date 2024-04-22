package com.attijari.vocalbanking.notification;

import com.attijari.vocalbanking.Client.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long notifId;

    private String notif;
    @Temporal(TemporalType.DATE)
    private Date notifDate;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Override
    public String toString() {
        return "Notification{" +
                "notifId=" + notifId +
                ", notif='" + notif + '\'' +
                ", notifDate=" + notifDate +
                ", type=" + type +
                '}';
    }
}
