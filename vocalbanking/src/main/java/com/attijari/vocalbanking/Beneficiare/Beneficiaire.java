package com.attijari.vocalbanking.Beneficiare;

import com.attijari.vocalbanking.Client.Client;
import com.attijari.vocalbanking.Virement.Virement;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

//    @OneToMany(mappedBy = "beneficiaire")
//    private List<Virement> virements;
}

