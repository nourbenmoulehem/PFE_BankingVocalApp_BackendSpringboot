package com.attijari.vocalbanking.Transaction;

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
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tran_id;

    @Temporal(TemporalType.DATE)
    private Date date_valeur;

    @Temporal(TemporalType.DATE)
    private Date date_operation;

    @Enumerated(EnumType.STRING)
    private TransactionType tran_type;

    @Enumerated(EnumType.STRING)
    private CanalType tran_canal;
    private String tran_marchant;
    private String tran_emplacement;
    private float montant;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_compteBancaire", referencedColumnName = "id_compteBancaire")
    private CompteBancaire compteBancaire;


}
