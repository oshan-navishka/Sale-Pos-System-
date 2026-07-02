package com.example.AAD.Task_IV.service.impl;

import com.example.AAD.Task_IV.dto.UserDTO;
import com.example.AAD.Task_IV.entity.User;
import com.example.AAD.Task_IV.repository.UserRepository;
import com.example.AAD.Task_IV.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user.getUserID(), user.getUserName(), user.getContact(), user.getRole());
    }

    @Override
    public void saveUser(UserDTO userDTO) {
        log.info("Saving new user: {}", userDTO.getUserName());
        try {
            if (userDTO.getUserName() == null || userDTO.getUserName().isEmpty()) {
                throw new RuntimeException("User name cannot be empty");
            }
            if (userDTO.getRole() == null) {
                throw new RuntimeException("User role must be specified");
            }
            User user = new User();
            user.setUserName(userDTO.getUserName());
            user.setContact(userDTO.getContact());
            user.setRole(userDTO.getRole());
            userRepository.save(user);
            log.info("User saved successfully");
        } catch (Exception e) {
            log.error("Error saving user: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        log.info("Updating user with ID: {}", userDTO.getUserID());
        try {
            if (userDTO.getUserName() == null || userDTO.getUserName().isEmpty()) {
                throw new RuntimeException("User name cannot be empty");
            }
            Optional<User> userOptional = userRepository.findById(userDTO.getUserID());
            if (userOptional.isEmpty()) {
                throw new RuntimeException("User not found with ID: " + userDTO.getUserID());
            }
            User user = userOptional.get();
            user.setUserName(userDTO.getUserName());
            user.setContact(userDTO.getContact());
            if (userDTO.getRole() != null) {
                user.setRole(userDTO.getRole());
            }
            userRepository.save(user);
            log.info("User updated successfully");
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                throw new RuntimeException("User not found with ID: " + userId);
            }
            userRepository.deleteById(userId);
            log.info("User deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Retrieving all users");
        try {
            List<User> users = userRepository.findAll();
            List<UserDTO> dtoList = new ArrayList<>();
            for (User user : users) {
                dtoList.add(toDTO(user));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error retrieving all users: {}", e.getMessage());
            throw e;
        }
    }
}
