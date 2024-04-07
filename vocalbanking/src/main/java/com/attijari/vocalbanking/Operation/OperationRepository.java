package com.attijari.vocalbanking.Operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {

    @Query(value = "SELECT * FROM Operation ORDER BY op_id DESC LIMIT :n", nativeQuery = true)
    List<Operation> findLastNRows(int n);

    @Query("SELECT v FROM Operation v WHERE v.date_operation BETWEEN :startDate AND :endDate")
    List<Operation> findOperationsBetweenDates(Date startDate, Date endDate);
}
