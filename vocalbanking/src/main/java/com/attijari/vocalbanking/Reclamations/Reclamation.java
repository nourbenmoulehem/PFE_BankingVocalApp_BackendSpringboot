package com.attijari.vocalbanking.Reclamations;

import com.attijari.vocalbanking.Client.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reclamationId;

    private String objet;

    private String descriptionClient;

    private String descriptionAssistant;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
