package com.precificapro.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record SimulationRequestDTO(
    @NotNull UUID productId,
    @NotNull UUID profileId,
    OverrideDTO override
) {
    public record OverrideDTO(
        BigDecimal purchaseCost,
        BigDecimal packagingCost,
        BigDecimal otherVariableCost,
        Integer monthlySalesTarget,
        BigDecimal machineFeePct,
        BigDecimal marketplaceFeePct,
        BigDecimal otherFeesPct
    ) {}
}