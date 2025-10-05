package com.precificapro.controller.dto;

import java.math.BigDecimal;

public record DashboardMetricsDTO(
    BigDecimal totalRevenue,
    BigDecimal totalNetProfit,
    long productCount,
    long customerCount
) {}