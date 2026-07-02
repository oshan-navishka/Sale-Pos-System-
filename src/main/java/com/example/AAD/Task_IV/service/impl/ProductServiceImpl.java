package com.example.AAD.Task_IV.service.impl;

import com.example.AAD.Task_IV.dto.ProductDTO;
import com.example.AAD.Task_IV.entity.Product;
import com.example.AAD.Task_IV.repository.ProductRepository;
import com.example.AAD.Task_IV.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private ProductDTO toDTO(Product product) {
        if (product == null) return null;
        return new ProductDTO(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getStatus()
        );
    }

    @Override
    public void saveProduct(ProductDTO productDTO) {
        log.info("Saving product: {}", productDTO.getProductName());
        try {
            if (productDTO.getProductName() == null || productDTO.getProductName().isEmpty()) {
                throw new RuntimeException("Product name cannot be empty");
            }
            if (productDTO.getPrice() < 0) {
                throw new RuntimeException("Product price cannot be negative");
            }
            Product product = new Product();
            product.setProductName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setQuantity(productDTO.getQuantity());
            product.setStatus(productDTO.getStatus() != null ? productDTO.getStatus() : "ACTIVE");

            productRepository.save(product);
            log.info("Product saved successfully");
        } catch (Exception e) {
            log.error("Error saving product: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateProduct(ProductDTO productDTO) {
        log.info("Updating product with ID: {}", productDTO.getProductId());
        try {
            if (productDTO.getProductName() == null || productDTO.getProductName().isEmpty()) {
                throw new RuntimeException("Product name cannot be empty");
            }
            Optional<Product> productOpt = productRepository.findById(productDTO.getProductId());
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found with ID: " + productDTO.getProductId());
            }
            Product product = productOpt.get();
            product.setProductName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setQuantity(productDTO.getQuantity());
            if (productDTO.getStatus() != null) {
                product.setStatus(productDTO.getStatus());
            }
            productRepository.save(product);
            log.info("Product updated successfully");
        } catch (Exception e) {
            log.error("Error updating product: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateProductPrice(Long productId, double price) {
        log.info("Updating price for product ID: {} to {}", productId, price);
        try {
            if (price < 0) {
                throw new RuntimeException("Product price cannot be negative");
            }
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found with ID: " + productId);
            }
            Product product = productOpt.get();
            product.setPrice(price);
            productRepository.save(product);
            log.info("Product price updated successfully");
        } catch (Exception e) {
            log.error("Error updating product price: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        log.info("Deleting product with ID: {}", productId);
        try {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found with ID: " + productId);
            }
            // Discontinue rather than hard delete if referenced, or hard delete
            Product product = productOpt.get();
            product.setStatus("DISCONTINUED");
            productRepository.save(product);
            log.info("Product marked as DISCONTINUED successfully");
        } catch (Exception e) {
            log.error("Error deleting product: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<ProductDTO> getProductsByStatus(String status) {
        log.info("Retrieving products by status: {}", status);
        try {
            List<Product> products = productRepository.findByStatus(status);
            List<ProductDTO> dtoList = new ArrayList<>();
            for (Product product : products) {
                dtoList.add(toDTO(product));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error retrieving products by status: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        log.info("Retrieving all products");
        try {
            List<Product> products = productRepository.findAll();
            List<ProductDTO> dtoList = new ArrayList<>();
            for (Product product : products) {
                dtoList.add(toDTO(product));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error retrieving all products: {}", e.getMessage());
            throw e;
        }
    }
}
