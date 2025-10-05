package com.precificapro.controller.dto;

import com.precificapro.domain.enums.CostItemType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CostItemCreateDTO(
        @NotBlank String description,
        @NotNull CostItemType type,
        @NotNull @DecimalMin("0.0") BigDecimal amountMonthly,
        @NotNull Boolean active
) {}