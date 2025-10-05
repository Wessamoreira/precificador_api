package com.precificapro.controller.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SaleItemResponseDTO(
        UUID id,
        UUID productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal unitCostAtSale,
        BigDecimal netProfit
) {}