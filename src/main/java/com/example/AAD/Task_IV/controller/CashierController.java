package com.example.AAD.Task_IV.controller;

import com.example.AAD.Task_IV.constant.CommonResponse;
import com.example.AAD.Task_IV.dto.CustomerDTO;
import com.example.AAD.Task_IV.dto.SaleDTO;
import com.example.AAD.Task_IV.service.CustomerService;
import com.example.AAD.Task_IV.service.SaleService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.AAD.Task_IV.constant.ResponseConde.OPERATION_SUCCESS;
import static com.example.AAD.Task_IV.constant.ResponseMassage.SUCCESS_MASSAGE;

@RestController
@RequestMapping("/api/cashier")
@CrossOrigin
public class CashierController {
    private final SaleService saleService;
    private final CustomerService customerService;

    public CashierController(SaleService saleService, CustomerService customerService) {
        this.saleService = saleService;
        this.customerService = customerService;
    }

    @PostMapping(value = "/sales", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse createSalesOrder(@RequestBody SaleDTO saleDTO) {
        saleService.saveSale(saleDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    //  DISCOUNTS
    @PutMapping(value = "/sales/{saleId}/discount", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse applyDiscount(@PathVariable Long saleId, @RequestParam double discountPercentage) {
        saleService.applyDiscount(saleId, discountPercentage);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @PutMapping(value = "/sales/{saleId}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse cancelTransaction(@PathVariable Long saleId) {
        saleService.cancelSale(saleId);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @GetMapping(value = "/sales/recent", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getRecentSales() {
        List<SaleDTO> sales = saleService.getRecentSales();
        return new CommonResponse(OPERATION_SUCCESS, sales, SUCCESS_MASSAGE);
    }

    @PostMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.saveCustomer(customerDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @GetMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return new CommonResponse(OPERATION_SUCCESS, customers, SUCCESS_MASSAGE);
    }
}
