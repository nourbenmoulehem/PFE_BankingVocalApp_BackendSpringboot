package com.attijari.vocalbanking.Virement;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property = "vir_id", scope=Virement.class)
public class Virement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long vir_id;

    @Temporal(TemporalType.DATE)
    @JsonProperty("date_valeur")
    private Date dateValeur;

    @Temporal(TemporalType.DATE)
    @JsonProperty("date_operation")
    private Date dateOperation;

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



    public String getDateValeurInFrench() {
        return formatDateInFrench(dateValeur);
    }

    public String getDateOperationInFrench() {
        return formatDateInFrench(dateOperation);
    }

    private String formatDateInFrench(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM", new Locale("fr"));
        return sdf.format(date);
    }

    /* how dateValeurInFrench and dateOperationInFrench appear automatically in the json repsonse explanation:
    When you serialize a Virement object into JSON (for example, when returning it as a response from a Spring Boot controller), Jackson (the JSON library used by Spring Boot) automatically invokes these methods to include additional fields in the JSON output.

     */
}
