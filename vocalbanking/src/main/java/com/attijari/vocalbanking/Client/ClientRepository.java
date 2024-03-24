package com.attijari.vocalbanking.Client;

import com.attijari.vocalbanking.Client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByCin(String cin);

    Optional<Client> findByCinAndPhoneNumber(String cin, String phoneNumber);

}
