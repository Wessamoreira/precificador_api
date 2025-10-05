package com.precificapro.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record SaleCreateDTO(
    @NotNull String customerPhoneNumber,
    @NotEmpty List<SaleItemCreateDTO> items
) {
    // Note que este record está aninhado, o que é permitido.
    public record SaleItemCreateDTO(
        @NotNull UUID productId,
        @NotNull Integer quantity,
        @NotNull BigDecimal unitPrice
    ) {}
}