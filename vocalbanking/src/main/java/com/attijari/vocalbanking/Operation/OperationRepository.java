package com.attijari.vocalbanking.Operation;

import com.attijari.vocalbanking.CompteBancaire.CompteBancaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {

    @Query(value = "SELECT * FROM Operation ORDER BY op_id DESC LIMIT :n", nativeQuery = true)
    List<Operation> findLastNRows(int n);

    @Query("SELECT v FROM Operation v WHERE v.date_operation BETWEEN :startDate AND :endDate AND v.compteBancaire.id_compteBancaire = :idCompteBancaire")
    List<Operation> findOperationsBetweenDatesAndByCompteBancaireId(Date startDate, Date endDate, int idCompteBancaire);

    @Query("SELECT v FROM Operation v WHERE v.compteBancaire = ?1")
    List<Operation> findByCompteBancaire(CompteBancaire compteBancaire);
}
