package com.attijari.vocalbanking.Operation;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
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
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property = "tran_id", scope= Operation.class)
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int op_id;

    @Temporal(TemporalType.DATE)
    private Date date_valeur;

    @Temporal(TemporalType.DATE)
    private Date date_operation;

    @Enumerated(EnumType.STRING)
    private OperationType op_type;

    @Enumerated(EnumType.STRING)
    private CanalType op_canal;
    private String op_marchant;
    private String op_emplacement;
    private float montant;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_compteBancaire", referencedColumnName = "id_compteBancaire")
    private CompteBancaire compteBancaire;

    public String getDateValeurInFrench() {
        return formatDateInFrench(date_valeur);
    }

    public String getDateOperationInFrench() {
        return formatDateInFrench(date_operation);
    }

    private String formatDateInFrench(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM", new Locale("fr"));
        return sdf.format(date);
    }


}
