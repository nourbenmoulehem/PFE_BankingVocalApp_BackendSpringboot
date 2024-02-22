package com.attijari.vocalbanking.repository;

import com.attijari.vocalbanking.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByCIN(Integer CIN);
}
