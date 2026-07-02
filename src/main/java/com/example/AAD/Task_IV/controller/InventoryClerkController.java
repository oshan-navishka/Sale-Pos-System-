package com.example.AAD.Task_IV.controller;

import com.example.AAD.Task_IV.constant.CommonResponse;
import com.example.AAD.Task_IV.dto.ProductDTO;
import com.example.AAD.Task_IV.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.AAD.Task_IV.constant.ResponseConde.OPERATION_SUCCESS;
import static com.example.AAD.Task_IV.constant.ResponseMassage.SUCCESS_MASSAGE;

@RestController
@RequestMapping("/api/clerk")
@CrossOrigin
public class InventoryClerkController {
    private final ProductService productService;

    public InventoryClerkController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/products/stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getRealTimeStock() {
        List<ProductDTO> products = productService.getProductsByStatus("ACTIVE");
        return new CommonResponse(OPERATION_SUCCESS, products, SUCCESS_MASSAGE);
    }

    @GetMapping(value = "/products/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return new CommonResponse(OPERATION_SUCCESS, products, SUCCESS_MASSAGE);
    }

    @PutMapping(value = "/products/{productId}/price", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateProductPrice(@PathVariable Long productId, @RequestParam double price) {
        productService.updateProductPrice(productId, price);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @PostMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addProduct(@RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @PutMapping(value = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        productDTO.setProductId(productId);
        productService.updateProduct(productDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @DeleteMapping(value = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }
}
