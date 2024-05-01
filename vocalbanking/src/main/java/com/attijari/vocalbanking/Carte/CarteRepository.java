package com.attijari.vocalbanking.Carte;

import com.attijari.vocalbanking.Client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteRepository extends JpaRepository<Carte, Long> {

    @Query("SELECT c FROM Carte c WHERE c.id_produit = ?1")
    Carte findByIdProduit(int id);
}
