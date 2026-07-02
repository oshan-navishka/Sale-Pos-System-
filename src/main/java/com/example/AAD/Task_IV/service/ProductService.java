package com.example.AAD.Task_IV.service;

import com.example.AAD.Task_IV.dto.ProductDTO;
import com.example.AAD.Task_IV.enumaration.ProductStatus;

import java.util.List;

public interface ProductService {
    void saveProduct(ProductDTO productDTO);
    void updateProduct(ProductDTO productDTO);
    void updateProductPrice(Long productId, double price);
    void deleteProduct(Long productId);
    List<ProductDTO> getProductsByStatus(ProductStatus status);
    List<ProductDTO> getAllProducts();
}
