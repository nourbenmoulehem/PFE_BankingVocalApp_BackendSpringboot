package com.attijari.vocalbanking.CompteBancaire;

import com.attijari.vocalbanking.Carte.Carte;
import com.attijari.vocalbanking.Client.Client;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class CompteBancaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compteBancaire")
    private int id_compteBancaire;
    @Column(unique = true)
    private String RIB;
    private float solde;
    private Date date_ouverture;
//    @JsonManagedReference // This is used to avoid infinite recursion
    @OneToOne(mappedBy = "compteBancaire")
    private Client client;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_produit", referencedColumnName = "id_produit")
    private Carte carte;



    // gets executed just before the entity is going to be inserted into the database for the first time
    @PrePersist
    public void prePersist() {
        this.date_ouverture = new Date();
        this.RIB = generateRib();
    }

    public static String generateRib() {
        String BANK_IDENTIFIER = "0409";
        // Generate 17 random digits
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 18; i++) {
            sb.append(random.nextInt(10)); // Generates a random digit (0-9)
        }
        // Concatenate bank identifier with the random digits
        return BANK_IDENTIFIER + sb.toString();
    }

}