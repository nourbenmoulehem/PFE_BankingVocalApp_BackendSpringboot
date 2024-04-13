package com.attijari.vocalbanking.Beneficiare;

import com.attijari.vocalbanking.Client.Client;
import com.attijari.vocalbanking.Virement.Virement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property = "id", scope= Beneficiaire.class) // This is used to avoid infinite recursion
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

