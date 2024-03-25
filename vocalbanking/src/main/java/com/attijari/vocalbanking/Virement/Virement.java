package com.attijari.vocalbanking.Virement;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property = "vir_id", scope=Virement.class)
public class Virement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long vir_id;

    @Temporal(TemporalType.DATE)
    private Date date_valeur;

    @Temporal(TemporalType.DATE)
    private Date date_operation;

    @Enumerated(EnumType.STRING)
    private LibelleVir libelle;

    private String bank;

    private float montant;

    private String motif;

    @Enumerated(EnumType.STRING)
    private EtatVirement etat;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_compteBancaire", referencedColumnName = "id_compteBancaire")
    private CompteBancaire compteBancaire;
}
