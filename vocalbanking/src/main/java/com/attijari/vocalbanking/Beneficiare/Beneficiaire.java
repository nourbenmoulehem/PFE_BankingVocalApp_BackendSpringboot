package com.attijari.vocalbanking.Beneficiare;

import com.attijari.vocalbanking.Client.Client;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;

    private String rib;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private Client client;
}

