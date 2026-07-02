package com.example.AAD.Task_IV.repository;

import com.example.AAD.Task_IV.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT s FROM Sale s WHERE s.saleDate BETWEEN ?1 AND ?2")
    List<Sale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT s FROM Sale s WHERE s.saleDate = ?1")
    List<Sale> findBySaleDate(LocalDate saleDate);

    @Query("SELECT s FROM Sale s ORDER BY s.saleId DESC")
    List<Sale> findAllByOrderBySaleIdDesc();
}
