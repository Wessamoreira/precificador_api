package com.precificapro.controller.dto;

import com.precificapro.domain.enums.PricingMethod;
import com.precificapro.domain.enums.RoundingRule;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PricingProfileResponseDTO(
        UUID id,
        String name,
        PricingMethod method,
        BigDecimal markup,
        BigDecimal marginOnPrice,
        BigDecimal machineFeePct,
        BigDecimal marketplaceFeePct,
        BigDecimal otherFeesPct,
        int monthlySalesTarget,
        RoundingRule roundingRule,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}