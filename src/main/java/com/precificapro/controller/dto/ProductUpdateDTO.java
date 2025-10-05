package com.precificapro.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductUpdateDTO(
        @NotBlank String name,
        @NotBlank String sku,
        @NotNull @DecimalMin("0.0") BigDecimal defaultPurchaseCost,
        @NotNull @DecimalMin("0.0") BigDecimal defaultPackagingCost,
        @NotNull @DecimalMin("0.0") BigDecimal defaultOtherVariableCost
) {}