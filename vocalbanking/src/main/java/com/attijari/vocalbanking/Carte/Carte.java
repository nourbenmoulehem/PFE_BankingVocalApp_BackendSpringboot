package com.attijari.vocalbanking.Carte;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Random;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="id_produit")
public class Carte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_produit;
    private String code_offre;
    private String numero_carte;
    private String status;
    private Date date_expiration;
    private int plafond;
    @OneToOne(mappedBy = "carte")
    private CompteBancaire compteBancaire;

    @PrePersist
    public void prePersist() {
        this.numero_carte = generateCarte();
    }

    public static String generateCarte() {
        // Generate 17 random digits
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            sb.append(random.nextInt(10)); // Generates a random digit (0-9)
        }
        // Concatenate bank identifier with the random digits
        return sb.toString();
    }

}
