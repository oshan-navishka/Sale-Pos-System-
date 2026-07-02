package com.example.AAD.Task_IV.service.impl;

import com.example.AAD.Task_IV.dto.request.ReportRowDTO;
import com.example.AAD.Task_IV.dto.SaleDTO;
import com.example.AAD.Task_IV.dto.SalesItemDTO;
import com.example.AAD.Task_IV.dto.SalesReportDTO;
import com.example.AAD.Task_IV.entity.Customer;
import com.example.AAD.Task_IV.entity.Product;
import com.example.AAD.Task_IV.entity.Sale;
import com.example.AAD.Task_IV.entity.SalesItem;
import com.example.AAD.Task_IV.entity.User;
import com.example.AAD.Task_IV.repository.CustomerRepository;
import com.example.AAD.Task_IV.repository.ProductRepository;
import com.example.AAD.Task_IV.repository.SaleRepository;
import com.example.AAD.Task_IV.repository.UserRepository;
import com.example.AAD.Task_IV.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public SaleServiceImpl(SaleRepository saleRepository,
                           ProductRepository productRepository,
                           CustomerRepository customerRepository,
                           UserRepository userRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    private SaleDTO toDTO(Sale sale) {
        if (sale == null) return null;
        Long customerId = (sale.getCustomer() != null) ? sale.getCustomer().getCustomerId() : null;
        String customerName = (sale.getCustomer() != null) ? sale.getCustomer().getCustomerName() : null;
        Long cashierId = (sale.getCashier() != null) ? sale.getCashier().getUserID() : null;
        String cashierName = (sale.getCashier() != null) ? sale.getCashier().getUserName() : null;

        List<SalesItemDTO> itemDTOs = new ArrayList<>();
        if (sale.getSalesItems() != null) {
            for (SalesItem item : sale.getSalesItems()) {
                Long productId = (item.getProduct() != null) ? item.getProduct().getProductId() : null;
                String productName = (item.getProduct() != null) ? item.getProduct().getProductName() : null;
                itemDTOs.add(new SalesItemDTO(
                        item.getSalesItemId(),
                        item.getQuantity(),
                        item.getPrice(),
                        productId,
                        productName
                ));
            }
        }

        return new SaleDTO(
                sale.getSaleId(),
                sale.getSaleDate(),
                sale.getDiscountPercentage(),
                sale.getTotalAmount(),
                sale.getStatus(),
                customerId,
                customerName,
                cashierId,
                cashierName,
                itemDTOs
        );
    }

    @Override
    @Transactional
    public void saveSale(SaleDTO saleDTO) {
        log.info("Initiating new sales transaction for customer ID: {}", saleDTO.getCustomerId());
        try {
            if (saleDTO.getCashierId() == null) {
                throw new RuntimeException("Cashier ID must be specified");
            }
            if (saleDTO.getSalesItems() == null || saleDTO.getSalesItems().isEmpty()) {
                throw new RuntimeException("Sale must contain at least one item");
            }

            Optional<User> cashierOpt = userRepository.findById(saleDTO.getCashierId());
            if (cashierOpt.isEmpty()) {
                throw new RuntimeException("Cashier not found with ID: " + saleDTO.getCashierId());
            }
            User cashier = cashierOpt.get();

            Customer customer = null;
            if (saleDTO.getCustomerId() != null) {
                Optional<Customer> customerOpt = customerRepository.findById(saleDTO.getCustomerId());
                if (customerOpt.isEmpty()) {
                    throw new RuntimeException("Customer not found with ID: " + saleDTO.getCustomerId());
                }
                customer = customerOpt.get();
            }

            Sale sale = new Sale();
            sale.setCashier(cashier);
            sale.setCustomer(customer);
            sale.setSaleDate(saleDTO.getSaleDate() != null ? saleDTO.getSaleDate() : LocalDate.now());
            sale.setStatus(saleDTO.getStatus() != null ? saleDTO.getStatus() : "completed");
            sale.setDiscountPercentage(saleDTO.getDiscountPercentage());

            double subTotal = 0;
            List<SalesItem> items = new ArrayList<>();
            for (SalesItemDTO itemDTO : saleDTO.getSalesItems()) {
                if (itemDTO.getProductId() == null) {
                    throw new RuntimeException("Product ID must be specified for each item");
                }
                if (itemDTO.getQuantity() <= 0) {
                    throw new RuntimeException("Item quantity must be greater than zero");
                }

                Optional<Product> productOpt = productRepository.findById(itemDTO.getProductId());
                if (productOpt.isEmpty()) {
                    throw new RuntimeException("Product not found with ID: " + itemDTO.getProductId());
                }
                Product product = productOpt.get();

                if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
                    throw new RuntimeException("Product is discontinued or inactive: " + product.getProductName());
                }

                // Check real-time stock
                if (product.getQuantity() < itemDTO.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getProductName() +
                            ". Available: " + product.getQuantity() + ", Required: " + itemDTO.getQuantity());
                }

                // Deduct stock immediately
                product.setQuantity(product.getQuantity() - itemDTO.getQuantity());
                productRepository.save(product);

                SalesItem item = new SalesItem();
                item.setSale(sale);
                item.setProduct(product);
                item.setQuantity(itemDTO.getQuantity());
                // Use product price if not provided
                double price = itemDTO.getPrice() > 0 ? itemDTO.getPrice() : product.getPrice();
                item.setPrice(price);

                subTotal += price * itemDTO.getQuantity();
                items.add(item);
            }

            sale.setSalesItems(items);

            // Apply discounts
            double total = subTotal;
            if (sale.getDiscountPercentage() > 0) {
                total = subTotal * (1.0 - sale.getDiscountPercentage() / 100.0);
            }
            sale.setTotalAmount(total);

            saleRepository.save(sale);
            log.info("Sales transaction completed and saved successfully with total amount: {}", total);
        } catch (Exception e) {
            log.error("Error saving sales order: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void applyDiscount(Long saleId, double discountPercentage) {
        log.info("Applying discount of {}% to sale ID: {}", discountPercentage, saleId);
        try {
            if (discountPercentage < 0 || discountPercentage > 100) {
                throw new RuntimeException("Discount percentage must be between 0 and 100");
            }
            Optional<Sale> saleOpt = saleRepository.findById(saleId);
            if (saleOpt.isEmpty()) {
                throw new RuntimeException("Sale not found with ID: " + saleId);
            }
            Sale sale = saleOpt.get();
            if ("cancelled".equalsIgnoreCase(sale.getStatus())) {
                throw new RuntimeException("Cannot apply discount to a cancelled sale");
            }

            sale.setDiscountPercentage(discountPercentage);

            // Recalculate total amount from items
            double subTotal = 0;
            for (SalesItem item : sale.getSalesItems()) {
                subTotal += item.getPrice() * item.getQuantity();
            }
            double total = subTotal * (1.0 - discountPercentage / 100.0);
            sale.setTotalAmount(total);

            saleRepository.save(sale);
            log.info("Discount applied successfully. New total: {}", total);
        } catch (Exception e) {
            log.error("Error applying discount: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void cancelSale(Long saleId) {
        log.info("Cancelling/Voiding sales transaction ID: {}", saleId);
        try {
            Optional<Sale> saleOpt = saleRepository.findById(saleId);
            if (saleOpt.isEmpty()) {
                throw new RuntimeException("Sale not found with ID: " + saleId);
            }
            Sale sale = saleOpt.get();
            if ("cancelled".equalsIgnoreCase(sale.getStatus())) {
                throw new RuntimeException("Sale is already cancelled");
            }

            // Restore product quantities to stock
            for (SalesItem item : sale.getSalesItems()) {
                Product product = item.getProduct();
                if (product != null) {
                    product.setQuantity(product.getQuantity() + item.getQuantity());
                    productRepository.save(product);
                }
            }

            sale.setStatus("cancelled");
            saleRepository.save(sale);
            log.info("Sales transaction ID: {} has been voided/cancelled and stock levels restored", saleId);
        } catch (Exception e) {
            log.error("Error cancelling sale ID: {}: {}", saleId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<SaleDTO> getRecentSales() {
        log.info("Retrieving recent sales");
        try {
            List<Sale> sales = saleRepository.findAllByOrderBySaleIdDesc();
            List<SaleDTO> dtoList = new ArrayList<>();
            for (Sale sale : sales) {
                dtoList.add(toDTO(sale));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error retrieving recent sales: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<SaleDTO> getAllSales() {
        log.info("Retrieving all sales");
        try {
            List<Sale> sales = saleRepository.findAll();
            List<SaleDTO> dtoList = new ArrayList<>();
            for (Sale sale : sales) {
                dtoList.add(toDTO(sale));
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error retrieving all sales: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public SalesReportDTO getSalesReport(String period) {
        log.info("Generating sales report for period: {}", period);
        try {
            LocalDate startDate;
            LocalDate endDate = LocalDate.now();

            if ("daily".equalsIgnoreCase(period)) {
                startDate = LocalDate.now();
            } else if ("weekly".equalsIgnoreCase(period)) {
                startDate = LocalDate.now().minusDays(7);
            } else if ("monthly".equalsIgnoreCase(period)) {
                startDate = LocalDate.now().withDayOfMonth(1);
            } else {
                // Default to all history/monthly
                startDate = LocalDate.now().withDayOfMonth(1);
            }

            List<Sale> sales = saleRepository.findBySaleDateBetween(startDate, endDate);
            List<ReportRowDTO> rows = new ArrayList<>();
            double totalRevenue = 0;

            for (Sale sale : sales) {
                String customerName = (sale.getCustomer() != null) ? sale.getCustomer().getCustomerName() : "Walk-in";
                rows.add(new ReportRowDTO(
                        sale.getSaleDate(),
                        sale.getSaleId(),
                        customerName,
                        sale.getStatus(),
                        sale.getTotalAmount()
                ));

                // Add to total revenue only if NOT cancelled
                if (!"cancelled".equalsIgnoreCase(sale.getStatus())) {
                    totalRevenue += sale.getTotalAmount();
                }
            }

            return new SalesReportDTO(rows, totalRevenue);
        } catch (Exception e) {
            log.error("Error generating sales report: {}", e.getMessage());
            throw e;
        }
    }
}
