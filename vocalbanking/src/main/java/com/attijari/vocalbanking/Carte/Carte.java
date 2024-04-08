package com.attijari.vocalbanking.Carte;

import com.attijari.vocalbanking.Client.Client;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property = "id_produit", scope= Carte.class)
public class Carte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_produit;
    @Enumerated(EnumType.STRING)
    private CodeOffre code_offre;
    private String numero_carte;
    private String status;
    @Temporal(TemporalType.DATE)
    private Date date_expiration;
    private int plafond;

    @OneToOne(mappedBy = "carte")
    private CompteBancaire compteBancaire;

    @PrePersist
    public void prePersist() {
        this.numero_carte = generateCarte();
        this.status = "Activee";
        this.date_expiration = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 5); // Add 5 years
        this.date_expiration = cal.getTime();
    }

    public static String generateCarte() {
        // Generate 16 random digits
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10)); // Generates a random digit (0-9)
        }
        // Concatenate bank identifier with the random digits
        return sb.toString();
    }

}
