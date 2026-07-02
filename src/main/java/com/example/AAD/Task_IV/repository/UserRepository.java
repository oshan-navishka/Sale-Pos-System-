package com.example.AAD.Task_IV.repository;

import com.example.AAD.Task_IV.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
