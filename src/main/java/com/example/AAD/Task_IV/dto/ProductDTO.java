package com.example.AAD.Task_IV.dto;

import com.example.AAD.Task_IV.enumaration.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String description;
    private double price;
    private int quantity;
    private ProductStatus status;
}
