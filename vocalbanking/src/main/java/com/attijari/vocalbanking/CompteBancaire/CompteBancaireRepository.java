package com.attijari.vocalbanking.CompteBancaire;

import com.attijari.vocalbanking.Client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteBancaireRepository extends JpaRepository<CompteBancaire, Long> {

    boolean existsByRIB(String rib);

}
