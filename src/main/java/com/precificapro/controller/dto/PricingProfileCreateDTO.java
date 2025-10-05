package com.precificapro.controller.dto;

import com.precificapro.domain.enums.PricingMethod;
import com.precificapro.domain.enums.RoundingRule;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PricingProfileCreateDTO(
        @NotBlank String name,
        @NotNull PricingMethod method,

        // Markup pode ser nulo se o método for MARGIN
        @DecimalMin("0.0") BigDecimal markup,

        // Margin pode ser nulo se o método for MARKUP
        @DecimalMin("0.0") BigDecimal marginOnPrice,

        @NotNull @DecimalMin("0.0") BigDecimal machineFeePct,
        @NotNull @DecimalMin("0.0") BigDecimal marketplaceFeePct,
        @NotNull @DecimalMin("0.0") BigDecimal otherFeesPct,
        @NotNull @Min(1) Integer monthlySalesTarget,
        @NotNull RoundingRule roundingRule
) {}