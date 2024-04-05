package com.attijari.vocalbanking.Virement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

@Repository
public interface VirementRepository extends JpaRepository<Virement, Long>  {

    @Query(value = "SELECT * FROM virement ORDER BY vir_id DESC LIMIT :n", nativeQuery = true)
    List<Virement> findLastNRows(int n);

    @Query("SELECT v FROM Virement v WHERE v.dateOperation BETWEEN :startDate AND :endDate")
    List<Virement> findVirementsBetweenDates(Date startDate, Date endDate);
}
