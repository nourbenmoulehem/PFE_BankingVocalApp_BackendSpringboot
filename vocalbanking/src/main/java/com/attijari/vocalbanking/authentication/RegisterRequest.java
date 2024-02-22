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
    private Integer CIN;
    private Date CIN_deliv_date;
    private String CIN_front;
    private String CIN_back;
    private String name;
    private String last_name;
    private Integer phone_number;
    private String gender;
    private Date birthdate;
    private String nationality;
    private String civil_status;
    private Integer nb_children;
    private String professional_category;
    private String activity_field;
    private String activity_nature;
    private Double monthly_revenue;

}
