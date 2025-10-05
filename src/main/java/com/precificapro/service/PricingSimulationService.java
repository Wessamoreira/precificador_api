package com.precificapro.service;

import com.precificapro.controller.dto.SimulationRequestDTO;
import com.precificapro.controller.dto.SimulationResponseDTO;
import com.precificapro.domain.enums.PricingMethod;
import com.precificapro.domain.enums.RoundingRule;
import com.precificapro.domain.model.CostItem;
import com.precificapro.domain.model.PricingProfile;
import com.precificapro.domain.model.Product;
import com.precificapro.domain.model.User;
import com.precificapro.domain.repository.CostItemRepository;
import com.precificapro.domain.repository.FreightBatchRepository;
import com.precificapro.domain.repository.PricingProfileRepository;
import com.precificapro.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class PricingSimulationService {

    @Autowired private ProductRepository productRepository;
    @Autowired private PricingProfileRepository profileRepository;
    @Autowired private CostItemRepository costItemRepository;
    @Autowired private FreightBatchRepository freightBatchRepository;

    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_UP);

    @Transactional(readOnly = true)
    public SimulationResponseDTO simulate(SimulationRequestDTO request, User owner) {

        // 1) CARREGAR ENTIDADES
        Product product = productRepository.findByIdAndOwner(request.productId(), owner)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));
        PricingProfile profile = profileRepository.findByIdAndOwner(request.profileId(), owner)
                .orElseThrow(() -> new RuntimeException("Perfil de precificação não encontrado."));

        // 2) APLICAR OVERRIDES
        SimulationRequestDTO.OverrideDTO override = request.override();
        BigDecimal purchaseCost = (override != null && override.purchaseCost() != null)
                ? override.purchaseCost() : nvl(product.getDefaultPurchaseCost());
        BigDecimal packagingCost = (override != null && override.packagingCost() != null)
                ? override.packagingCost() : nvl(product.getDefaultPackagingCost());
        BigDecimal otherVariableCost = (override != null && override.otherVariableCost() != null)
                ? override.otherVariableCost() : nvl(product.getDefaultOtherVariableCost());
        Integer optSalesTarget = (override != null) ? override.monthlySalesTarget() : null;
        int salesTarget = (optSalesTarget != null) ? optSalesTarget : nz(profile.getMonthlySalesTarget());

        BigDecimal machineFeePct = (override != null && override.machineFeePct() != null)
                ? override.machineFeePct() : nvl(profile.getMachineFeePct());
        BigDecimal marketplaceFeePct = (override != null && override.marketplaceFeePct() != null)
                ? override.marketplaceFeePct() : nvl(profile.getMarketplaceFeePct());
        BigDecimal otherFeesPct = (override != null && override.otherFeesPct() != null)
                ? override.otherFeesPct() : nvl(profile.getOtherFeesPct());

        // 3) CUSTO DIRETO UNITÁRIO (inclui último frete rateado se houver)
        BigDecimal freightCostUnit = freightBatchRepository.findFirstByProductOrderByCreatedAtDesc(product)
                .map(batch -> safeDivide(batch.getFreightTotal(), BigDecimal.valueOf(nz(batch.getBatchSize())), MC))
                .orElse(BigDecimal.ZERO);

        BigDecimal directCostUnit = purchaseCost
                .add(packagingCost)
                .add(otherVariableCost)
                .add(freightCostUnit);

        // 4) CUSTO INDIRETO UNITÁRIO (rateio dos fixos)
        BigDecimal totalFixedCosts = costItemRepository.findByOwnerAndActiveTrue(owner).stream()
                .map(ci -> nvl(ci.getAmountMonthly()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal indirectCostUnit = (salesTarget > 0)
                ? safeDivide(totalFixedCosts, BigDecimal.valueOf(salesTarget), MC)
                : BigDecimal.ZERO;

        // 5) CUSTO TOTAL UNITÁRIO
        BigDecimal totalCostUnit = directCostUnit.add(indirectCostUnit);

        // 6) PREÇO DE VENDA (considerando método: MARKUP vs MARGIN)
        BigDecimal totalFeesPct = nvl(machineFeePct).add(nvl(marketplaceFeePct)).add(nvl(otherFeesPct));

        BigDecimal price;
        if (profile.getMethod() == PricingMethod.MARKUP) {
            BigDecimal markupValue = nvl(profile.getMarkup()); // Ex.: 0.50 = 50%
            // preço = custo_total * (1 + markup) / (1 - taxas)
            price = safeDivide(
                    totalCostUnit.multiply(BigDecimal.ONE.add(markupValue), MC),
                    BigDecimal.ONE.subtract(totalFeesPct, MC),
                    MC
            );
        } else { // MARGIN (marginOnPrice = % do preço que deve ser lucro líquido)
            BigDecimal marginValue = nvl(profile.getMarginOnPrice()); // Ex.: 0.20 = 20%
            // preço = custo_total / (1 - taxas - margem)
            price = safeDivide(
                    totalCostUnit,
                    BigDecimal.ONE.subtract(totalFeesPct, MC).subtract(marginValue, MC),
                    MC
            );
        }

        BigDecimal suggestedPrice = applyRounding(price, profile.getRoundingRule());

        // 7) MÉTRICAS FINAIS
        BigDecimal feesValue = suggestedPrice.multiply(totalFeesPct, MC);
        BigDecimal costPlusFees = totalCostUnit.add(feesValue, MC);
        BigDecimal netProfitPerUnit = suggestedPrice.subtract(costPlusFees, MC);

        // % lucro líquido sobre o preço (margem líquida)
        BigDecimal netProfitPercentage = (suggestedPrice.compareTo(BigDecimal.ZERO) > 0)
                ? safeDivide(netProfitPerUnit, suggestedPrice, MC).multiply(BigDecimal.valueOf(100), MC)
                : BigDecimal.ZERO;

        // % markup sobre custo total
        BigDecimal markupOnTotalCost = (totalCostUnit.compareTo(BigDecimal.ZERO) > 0)
                ? (safeDivide(suggestedPrice, totalCostUnit, MC).subtract(BigDecimal.ONE, MC)).multiply(BigDecimal.valueOf(100), MC)
                : BigDecimal.ZERO;

        // === NOVO CAMPO: MARGEM DE LUCRO (BRUTA) = (Preço - Custo Direto) / Preço ===
        BigDecimal lucroBruto = suggestedPrice.subtract(directCostUnit, MC);
        BigDecimal margemDeLucro = (suggestedPrice.compareTo(BigDecimal.ZERO) > 0)
                ? safeDivide(lucroBruto, suggestedPrice, MC).multiply(BigDecimal.valueOf(100), MC)
                : BigDecimal.ZERO;

        // Contribuição e Ponto de Equilíbrio
        BigDecimal contributionMarginUnit = suggestedPrice.multiply(BigDecimal.ONE.subtract(totalFeesPct, MC), MC)
                .subtract(directCostUnit, MC);
        Integer breakEvenUnits = (contributionMarginUnit.compareTo(BigDecimal.ZERO) > 0)
                ? safeDivide(totalFixedCosts, contributionMarginUnit, MC).setScale(0, RoundingMode.CEILING).intValue()
                : null;

        // 8) SUB-DTOs
        SimulationResponseDTO.CostBreakdownDTO costBreakdown = new SimulationResponseDTO.CostBreakdownDTO(
                purchaseCost.setScale(2, RoundingMode.HALF_UP),
                packagingCost.setScale(2, RoundingMode.HALF_UP),
                otherVariableCost.setScale(2, RoundingMode.HALF_UP),
                freightCostUnit.setScale(2, RoundingMode.HALF_UP),
                directCostUnit.setScale(2, RoundingMode.HALF_UP),
                indirectCostUnit.setScale(2, RoundingMode.HALF_UP),
                totalCostUnit.setScale(2, RoundingMode.HALF_UP),
                feesValue.setScale(2, RoundingMode.HALF_UP),
                costPlusFees.setScale(2, RoundingMode.HALF_UP)
        );

        SimulationResponseDTO.ProfitDetailsDTO profitDetails = new SimulationResponseDTO.ProfitDetailsDTO(
                netProfitPerUnit.setScale(2, RoundingMode.HALF_UP),
                netProfitPercentage.setScale(2, RoundingMode.HALF_UP),   // margem líquida (% sobre preço)
                markupOnTotalCost.setScale(2, RoundingMode.HALF_UP),
                margemDeLucro.setScale(2, RoundingMode.HALF_UP)          // NOVO: margem bruta (% sobre preço)
        );

        BigDecimal revenue = suggestedPrice.multiply(BigDecimal.valueOf(salesTarget), MC);
        BigDecimal totalDirectCost = directCostUnit.multiply(BigDecimal.valueOf(salesTarget), MC);
        BigDecimal totalFees = feesValue.multiply(BigDecimal.valueOf(salesTarget), MC);
        BigDecimal totalNetProfit = netProfitPerUnit.multiply(BigDecimal.valueOf(salesTarget), MC);

        SimulationResponseDTO.MonthlyProjectionDTO monthlyProjection = new SimulationResponseDTO.MonthlyProjectionDTO(
                revenue.setScale(2, RoundingMode.HALF_UP),
                totalDirectCost.setScale(2, RoundingMode.HALF_UP),
                totalFixedCosts.setScale(2, RoundingMode.HALF_UP),
                totalFees.setScale(2, RoundingMode.HALF_UP),
                totalNetProfit.setScale(2, RoundingMode.HALF_UP)
        );

        // 9) RESPOSTA FINAL
        return new SimulationResponseDTO(
                suggestedPrice.setScale(2, RoundingMode.HALF_UP),
                breakEvenUnits,
                costBreakdown,
                profitDetails,
                monthlyProjection
        );
    }

    // === ARREDONDAMENTO ESPECÍFICO ===
    private BigDecimal applyRounding(BigDecimal price, RoundingRule rule) {
        if (rule == null || rule == RoundingRule.NONE) {
            return price.setScale(2, RoundingMode.HALF_UP);
        }

        long integerPart = price.longValue();
        BigDecimal decimalPart = price.subtract(BigDecimal.valueOf(integerPart));

        return switch (rule) {
            case UP_TO_0_90 -> (decimalPart.compareTo(BigDecimal.valueOf(0.90)) <= 0)
                    ? BigDecimal.valueOf(integerPart).add(BigDecimal.valueOf(0.90))
                    : BigDecimal.valueOf(integerPart + 1).add(BigDecimal.valueOf(0.90));
            case UP_TO_0_99 -> (decimalPart.compareTo(BigDecimal.valueOf(0.99)) <= 0)
                    ? BigDecimal.valueOf(integerPart).add(BigDecimal.valueOf(0.99))
                    : BigDecimal.valueOf(integerPart + 1).add(BigDecimal.valueOf(0.99));
            case UP_TO_0_50 -> (decimalPart.compareTo(BigDecimal.valueOf(0.50)) <= 0)
                    ? BigDecimal.valueOf(integerPart).add(BigDecimal.valueOf(0.50))
                    : BigDecimal.valueOf(integerPart + 1).add(BigDecimal.valueOf(0.50));
            default -> price.setScale(2, RoundingMode.HALF_UP);
        };
    }

    // === HELPERS DE SEGURANÇA NUMÉRICA ===
    private static BigDecimal nvl(BigDecimal v) {
        return (v == null) ? BigDecimal.ZERO : v;
    }

    private static int nz(Integer i) {
        return (i == null) ? 0 : i;
    }

    private static BigDecimal safeDivide(BigDecimal a, BigDecimal b, MathContext mc) {
        if (b == null || b.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return a.divide(b, mc);
    }
}
