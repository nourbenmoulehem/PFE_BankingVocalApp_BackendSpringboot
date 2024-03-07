package com.attijari.vocalbanking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import java.util.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;
//    @NaturalId
//    @Digits(integer = 8, fraction = 0)
    private String cin;
    private String offer;
    private Date dateDelivrationCin;
    private String cinRecto; // File path or URL to the front picture
    private String cinVerso;  // File path or URL to the back picture
    private String selfie;  // File path or URL to the back picture
    private String firstName;
    private String lastName;
//    @Digits(integer = 8, fraction = 0)
    private String phoneNumber;
    private String gender;
    private Date birthday;
    private String nationality;
    private String statusCivil;
    private Integer nombre_enfant;
    private String socio_professional;
    private String secteurActivite;
    private String natureActivite;
    private String revenu;
    private String codePostal;
    private String gouvernorat;
    private boolean hasOtherBank;
    private String agence;

}
