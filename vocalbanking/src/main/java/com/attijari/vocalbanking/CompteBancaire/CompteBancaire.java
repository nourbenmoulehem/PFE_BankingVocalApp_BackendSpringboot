package com.attijari.vocalbanking.CompteBancaire;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompteBancaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_Compte;
    @Column(unique = true)
    private String RIB;
    private float solde;
    private Date date_ouverture;
    // @OneToOne // one-to-one relationship with Client



    // gets executed just before the entity is going to be inserted into the database for the first time
    @PrePersist
    public void prePersist() {
        this.date_ouverture = new Date();
        this.RIB = generateUniqueNumber();
    }

    private String generateUniqueNumber() {
        long min = (long) Math.pow(10, 19); // minimum value for 20-digit number
        long max = (long) Math.pow(10, 20) - 1; // maximum value for 20-digit number
        long randomNum = ThreadLocalRandom.current().nextLong(min, max);
        return String.format("%020d", randomNum); // format as 20-digit string
    }

}
