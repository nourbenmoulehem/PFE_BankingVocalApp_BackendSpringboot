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
    @NaturalId
    @Digits(integer = 8, fraction = 0)
    private Integer CIN;
    private Date CIN_deliv_date;
    private String CIN_front; // File path or URL to the front picture
    private String CIN_back;  // File path or URL to the back picture
    private String name;
    private String last_name;
    @Digits(integer = 8, fraction = 0)
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
