package com.precificapro.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record FreightBatchCreateDTO(
        @NotNull UUID productId,
        @NotNull @Min(1) Integer batchSize,
        @NotNull @DecimalMin("0.0") BigDecimal freightTotal
) {}