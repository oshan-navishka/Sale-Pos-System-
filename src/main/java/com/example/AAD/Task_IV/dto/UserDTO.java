package com.example.AAD.Task_IV.dto;

import com.example.AAD.Task_IV.enumaration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userID;
    private String userName;
    private int contact;
    private UserRole role;
}
