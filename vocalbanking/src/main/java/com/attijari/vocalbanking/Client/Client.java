package com.attijari.vocalbanking.Client;

import com.attijari.vocalbanking.Beneficiare.Beneficiaire;
import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.attijari.vocalbanking.Virement.Virement;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import java.util.*;

@Data
@ToString(exclude = "compteBancaire") // including compteBancaire will cause infinite recursion loop, so either exclude it or idk
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property = "clientId", scope= Client.class) // This is used to avoid infinite recursion
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
    private String adresse;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JsonBackReference // This is used to avoid infinite recursion
    @JoinColumn(name = "id_compteBancaire", referencedColumnName = "id_compteBancaire")  // This means Foreign key will be created only in the Client table i.e. extra column 'id_CompteBancaire' will be created in the Client table
    private CompteBancaire compteBancaire;

    @OneToMany(mappedBy = "client")
    private List<Beneficiaire> beneficiairesList;

}
