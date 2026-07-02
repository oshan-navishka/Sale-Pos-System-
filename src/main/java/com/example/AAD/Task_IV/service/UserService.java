package com.example.AAD.Task_IV.service;

import com.example.AAD.Task_IV.dto.UserDTO;

import java.util.List;

public interface UserService {
    void saveUser(UserDTO userDTO);
    void updateUser(UserDTO userDTO);
    void deleteUser(Long userId);
    List<UserDTO> getAllUsers();
}
