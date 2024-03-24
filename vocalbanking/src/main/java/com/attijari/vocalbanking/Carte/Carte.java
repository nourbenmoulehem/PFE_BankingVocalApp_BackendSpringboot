package com.attijari.vocalbanking.Carte;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Carte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_produit;
    private String code_offre;
    private String numero_carte;
    private String status;
    private Date date_expiration;
    private int plafond;
    // @OneToOne // one-to-one relationship with CompteBancaire

}
