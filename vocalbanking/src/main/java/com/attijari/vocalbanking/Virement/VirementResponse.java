package com.attijari.vocalbanking.Virement;

import com.attijari.vocalbanking.Beneficiare.Beneficiaire;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirementResponse {
    private Long virId;
    @Enumerated(EnumType.STRING)
    private LibelleVir libelle;
    @Temporal(TemporalType.DATE)
    @JsonProperty("date_operation")
    private Date dateOperation;
    @Temporal(TemporalType.DATE)
    @JsonProperty("date_valeur")
    private Date dateValeur;
    private String bank;
    private float montant;
    private String motif;
    @Enumerated(EnumType.STRING)
    private EtatVirement etat;
    private String beneficiaireNom;
}
