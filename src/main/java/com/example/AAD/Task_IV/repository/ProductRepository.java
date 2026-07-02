package com.example.AAD.Task_IV.repository;

import com.example.AAD.Task_IV.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.status = ?1")
    List<Product> findByStatus(String status);
}
