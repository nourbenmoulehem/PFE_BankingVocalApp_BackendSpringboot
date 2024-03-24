package com.attijari.vocalbanking.Carte;

import com.attijari.vocalbanking.Client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteRepository extends JpaRepository<Carte, Long> {
}
