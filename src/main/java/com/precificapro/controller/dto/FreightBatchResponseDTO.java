package com.precificapro.controller.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record FreightBatchResponseDTO(
        UUID id,
        UUID productId,
        int batchSize,
        BigDecimal freightTotal,
        OffsetDateTime createdAt
) {}