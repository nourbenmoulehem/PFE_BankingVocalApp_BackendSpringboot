package com.attijari.vocalbanking.Beneficiare;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiareRepository extends JpaRepository<Beneficiaire, Long> {

    List<Beneficiaire> findByClientClientId(Long clientId);
}
