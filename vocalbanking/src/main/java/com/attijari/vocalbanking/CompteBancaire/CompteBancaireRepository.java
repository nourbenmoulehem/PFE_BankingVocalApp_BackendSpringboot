package com.attijari.vocalbanking.CompteBancaire;

import com.attijari.vocalbanking.Client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteBancaireRepository extends JpaRepository<CompteBancaire, Long> {

    boolean existsByRIB(String rib);

    @Query("SELECT c FROM CompteBancaire c WHERE c.client.id = ?1")
    CompteBancaire findByClientID(Long clientId);
}
