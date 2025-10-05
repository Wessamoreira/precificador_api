package com.precificapro.controller.dto;

import com.precificapro.domain.enums.CostItemType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CostItemResponseDTO(
        UUID id,
        String description,
        CostItemType type,
        BigDecimal amountMonthly,
        boolean active,
        OffsetDateTime createdAt
) {}