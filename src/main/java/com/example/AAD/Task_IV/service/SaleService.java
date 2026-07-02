package com.example.AAD.Task_IV.service;

import com.example.AAD.Task_IV.dto.SaleDTO;
import com.example.AAD.Task_IV.dto.SalesReportDTO;

import java.util.List;

public interface SaleService {
    void saveSale(SaleDTO saleDTO);
    void applyDiscount(Long saleId, double discountPercentage);
    void cancelSale(Long saleId);
    List<SaleDTO> getRecentSales();
    List<SaleDTO> getAllSales();
    SalesReportDTO getSalesReport(String period);
}
