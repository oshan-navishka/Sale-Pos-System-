package com.example.AAD.Task_IV.dto;

import com.example.AAD.Task_IV.dto.request.ReportRowDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportDTO {
    private List<ReportRowDTO> transactions;
    private double totalRevenue;
}
