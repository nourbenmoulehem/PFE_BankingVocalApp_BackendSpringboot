package com.attijari.vocalbanking.Transaction;

import com.attijari.vocalbanking.Virement.Virement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT * FROM Transaction ORDER BY tran_id DESC LIMIT :n", nativeQuery = true)
    List<Transaction> findLastNRows(int n);

    @Query("SELECT v FROM Transaction v WHERE v.date_operation BETWEEN :startDate AND :endDate")
    List<Transaction> findOperationsBetweenDates(Date startDate, Date endDate);
}
