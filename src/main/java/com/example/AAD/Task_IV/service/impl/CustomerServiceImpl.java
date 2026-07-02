package com.example.AAD.Task_IV.service.impl;

import com.example.AAD.Task_IV.dto.CustomerDTO;
import com.example.AAD.Task_IV.entity.Customer;
import com.example.AAD.Task_IV.repository.CustomerRepository;
import com.example.AAD.Task_IV.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;
        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getContactNumber(),
                customer.getEmail()
        );
    }

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving customer: {}", customerDTO.getCustomerName());
        try {
            if (customerDTO.getCustomerName() == null || customerDTO.getCustomerName().isEmpty()) {
                throw new RuntimeException("Customer name cannot be empty");
            }
            Customer customer = new Customer();
            customer.setCustomerName(customerDTO.getCustomerName());
            customer.setContactNumber(customerDTO.getContactNumber());
            customer.setEmail(customerDTO.getEmail());
            customerRepository.save(customer);
            log.info("Customer saved successfully");
        } catch (Exception e) {
            log.error("Error saving customer: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating customer with ID: {}", customerDTO.getCustomerId());
        try {
            if (customerDTO.getCustomerName() == null || customerDTO.getCustomerName().isEmpty()) {
                throw new RuntimeException("Customer name cannot be empty");
            }
            Optional<Customer> customerOpt = customerRepository.findById(customerDTO.getCustomerId());
            if (customerOpt.isEmpty()) {
                throw new RuntimeException("Customer not found with ID: " + customerDTO.getCustomerId());
            }
            Customer customer = customerOpt.get();
            customer.setCustomerName(customerDTO.getCustomerName());
            customer.setContactNumber(customerDTO.getContactNumber());
            customer.setEmail(customerDTO.getEmail());
            customerRepository.save(customer);
            log.info("Customer updated successfully");
        } catch (Exception e) {
            log.error("Error updating customer: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.info("Retrieving all customers");
        try {
            List<Customer> customers = customerRepository.findAll();
            List<CustomerDTO> dtoList = new ArrayList<>();
            for (Customer customer : customers) {
                dtoList.add(toDTO(customer));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error retrieving all customers: {}", e.getMessage());
            throw e;
        }
    }
}
