package com.precificapro.controller.dto;

import java.math.BigDecimal;

public record SimulationResponseDTO(
        BigDecimal suggestedPrice,
        Integer breakEvenUnits,
        CostBreakdownDTO costBreakdown,
        ProfitDetailsDTO profitDetails,
        MonthlyProjectionDTO monthlyProjection
) {

    public record CostBreakdownDTO(
            BigDecimal purchaseCost, BigDecimal packagingCost, BigDecimal otherVariableCost,
            BigDecimal freightCostUnit, BigDecimal directCostUnit, BigDecimal indirectCostUnit,
            BigDecimal totalCostUnit, BigDecimal feesValue, BigDecimal costPlusFees
    ) {}

    public record ProfitDetailsDTO(
            BigDecimal netProfitPerUnit,
            BigDecimal netProfitPercentage,     // margem líquida = (Lucro Líquido / Preço)
            BigDecimal markupOnTotalCost,       // % sobre custo total
            BigDecimal margemDeLucro            // NOVO: margem bruta = (Preço - Custo Direto) / Preço
    ) {}

    public record MonthlyProjectionDTO(
            BigDecimal revenue, BigDecimal totalDirectCost, BigDecimal totalIndirectCost,
            BigDecimal totalFees, BigDecimal netProfit
    ) {}
}
