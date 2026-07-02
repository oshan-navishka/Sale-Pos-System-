package com.example.AAD.Task_IV.service;

import com.example.AAD.Task_IV.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    void saveCustomer(CustomerDTO customerDTO);
    void updateCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> getAllCustomers();
}
