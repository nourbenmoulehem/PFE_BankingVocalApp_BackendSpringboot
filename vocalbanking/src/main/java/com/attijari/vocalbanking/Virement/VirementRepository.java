package com.attijari.vocalbanking.Virement;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import com.attijari.vocalbanking.Operation.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VirementRepository extends JpaRepository<Virement, Long>  {

    @Query(value = "SELECT * FROM virement ORDER BY vir_id DESC LIMIT :n", nativeQuery = true)
    List<Virement> findLastNRows(int n);

    @Query("SELECT v FROM Virement v WHERE v.dateOperation BETWEEN :startDate AND :endDate AND v.compteBancaire.id_compteBancaire = :idCompteBancaire")
    List<Virement> findVirementsBetweenDatesAndByCompteBancaireId(Date startDate, Date endDate, int idCompteBancaire);

    @Query("SELECT v FROM Virement v WHERE v.compteBancaire = ?1")
    List<Virement> findByCompteBancaire(CompteBancaire compteBancaire);
    @Query("SELECT v FROM Virement v WHERE v.vir_id = ?1")
    Virement findByVirId(Long vir_id);
}
