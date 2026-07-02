package com.example.AAD.Task_IV.repository;

import com.example.AAD.Task_IV.entity.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {
}
