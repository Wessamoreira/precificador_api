package com.precificapro.controller.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record SaleResponseDTO(
        UUID id,
        OffsetDateTime saleDate,
        BigDecimal totalAmount,
        BigDecimal totalNetProfit,
        CustomerResponseDTO customer,
        List<SaleItemResponseDTO> items
) {}