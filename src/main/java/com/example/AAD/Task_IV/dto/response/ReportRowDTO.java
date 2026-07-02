package com.example.AAD.Task_IV.dto.response;

import com.example.AAD.Task_IV.enumaration.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRowDTO {
    private LocalDate date;
    private Long orderId;
    private String customer;
    private SalesStatus status;
    private double amount;
}
