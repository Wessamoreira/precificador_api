package com.precificapro.controller.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String name,
        String sku,
        BigDecimal defaultPurchaseCost,
        BigDecimal defaultPackagingCost,
        BigDecimal defaultOtherVariableCost,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}