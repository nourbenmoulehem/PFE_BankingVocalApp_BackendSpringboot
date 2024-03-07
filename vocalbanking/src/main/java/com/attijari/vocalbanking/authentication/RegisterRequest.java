package com.attijari.vocalbanking.authentication;

import com.attijari.vocalbanking.model.Role;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String email;
    private String password;
    private Role role;
    private String cin;
    private String offer;
    private Date dateDelivrationCin;
    private String cinRecto; // File path or URL to the front picture
    private String cinVerso;  // File path or URL to the back picture
    private String selfie;  // File path or URL to the back picture
    private String firstName;
    private String lastName;
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
    //private boolean hasOtherBank;
    private String agence;

}
