package com.example.AAD.Task_IV.dto;

import com.example.AAD.Task_IV.enumaration.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {
    private Long saleId;
    private LocalDate saleDate;
    private double discountPercentage;
    private double totalAmount;
    private SalesStatus status;
    private Long customerId;
    private String customerName;
    private Long cashierId;
    private String cashierName;
    private List<SalesItemDTO> salesItems;
}
