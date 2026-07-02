package com.example.AAD.Task_IV.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesItemDTO {
    private Long salesItemId;
    private int quantity;
    private double price;
    private Long productId;
    private String productName;
}
