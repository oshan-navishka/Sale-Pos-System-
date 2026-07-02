package com.example.AAD.Task_IV.entity;

import com.example.AAD.Task_IV.enumaration.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    private String userName;
    private int contact;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
