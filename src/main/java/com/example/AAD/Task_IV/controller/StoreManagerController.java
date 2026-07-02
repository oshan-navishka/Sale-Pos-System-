package com.example.AAD.Task_IV.controller;

import com.example.AAD.Task_IV.constant.CommonResponse;
import com.example.AAD.Task_IV.dto.SalesReportDTO;
import com.example.AAD.Task_IV.dto.SaleDTO;
import com.example.AAD.Task_IV.dto.UserDTO;
import com.example.AAD.Task_IV.service.SaleService;
import com.example.AAD.Task_IV.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.AAD.Task_IV.constant.ResponseConde.OPERATION_SUCCESS;
import static com.example.AAD.Task_IV.constant.ResponseMassage.SUCCESS_MASSAGE;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin
public class StoreManagerController {
    private final SaleService saleService;
    private final UserService userService;

    public StoreManagerController(SaleService saleService, UserService userService) {
        this.saleService = saleService;
        this.userService = userService;
    }

    // VIEW SALES REPORTS
    @GetMapping(value = "/reports/sales", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getSalesReport(@RequestParam(defaultValue = "monthly") String period) {
        SalesReportDTO report = saleService.getSalesReport(period);
        return new CommonResponse(OPERATION_SUCCESS, report, SUCCESS_MASSAGE);
    }

    @PostMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addEmployee(@RequestBody UserDTO userDTO) {
        userService.saveUser(userDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @PutMapping(value = "/employees/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateEmployee(@PathVariable Long employeeId, @RequestBody UserDTO userDTO) {
        userDTO.setUserID(employeeId);
        userService.updateUser(userDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @DeleteMapping(value = "/employees/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteEmployee(@PathVariable Long employeeId) {
        userService.deleteUser(employeeId);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MASSAGE);
    }

    @GetMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllEmployees() {
        List<UserDTO> employees = userService.getAllUsers();
        return new CommonResponse(OPERATION_SUCCESS, employees, SUCCESS_MASSAGE);
    }

    @GetMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getTransactionHistory() {
        List<SaleDTO> history = saleService.getAllSales();
        return new CommonResponse(OPERATION_SUCCESS, history, SUCCESS_MASSAGE);
    }
}
