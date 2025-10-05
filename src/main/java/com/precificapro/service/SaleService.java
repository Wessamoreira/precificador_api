package com.precificapro.service;

import com.precificapro.controller.dto.SaleCreateDTO;
import com.precificapro.domain.model.*;
import com.precificapro.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    @Autowired private SaleRepository saleRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CostItemRepository costItemRepository;
    @Autowired private FreightBatchRepository freightBatchRepository;
    
    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_UP);
    
    @Transactional(readOnly = true)
    public List<Sale> findAllByOwner(User owner) {
        return saleRepository.findAllByOwnerOrderBySaleDateDesc(owner);
    }

    @Transactional
    public Sale recordSale(SaleCreateDTO dto, User owner) {
        // 1. Encontra ou cria o cliente
        Customer customer = customerRepository.findByOwnerAndPhoneNumber(owner, dto.customerPhoneNumber())
                .orElseGet(() -> {
                    Customer newCustomer = Customer.builder()
                            .owner(owner)
                            .phoneNumber(dto.customerPhoneNumber())
                            .name("Cliente - " + dto.customerPhoneNumber()) // Nome padrão
                            .build();
                    return customerRepository.save(newCustomer);
                });

        Sale sale = Sale.builder()
                .owner(owner)
                .customer(customer)
                .saleDate(OffsetDateTime.now())
                .build();

        List<SaleItem> saleItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalNetProfit = BigDecimal.ZERO;

        // 2. Processa cada item da venda
        for (SaleCreateDTO.SaleItemCreateDTO itemDto : dto.items()) {
            Product product = productRepository.findByIdAndOwner(itemDto.productId(), owner)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

            // 3. CALCULA O CUSTO DO PRODUTO NO MOMENTO DA VENDA (SNAPSHOT)
            // Esta lógica é uma simplificação do PricingSimulationService
            BigDecimal freightCostUnit = freightBatchRepository.findFirstByProductOrderByCreatedAtDesc(product)
                .map(batch -> batch.getFreightTotal().divide(BigDecimal.valueOf(batch.getBatchSize()), MC))
                .orElse(BigDecimal.ZERO);
            BigDecimal directCostUnit = product.getDefaultPurchaseCost()
                .add(product.getDefaultPackagingCost())
                .add(product.getDefaultOtherVariableCost())
                .add(freightCostUnit);

            // Custo total unitário (sem rateio de fixos para o lucro da venda)
            BigDecimal unitCostAtSale = directCostUnit; // Decisão: lucro da venda não considera custo fixo
            BigDecimal itemProfit = itemDto.unitPrice().subtract(unitCostAtSale);
            BigDecimal totalItemProfit = itemProfit.multiply(BigDecimal.valueOf(itemDto.quantity()));

            SaleItem saleItem = SaleItem.builder()
                    .sale(sale)
                    .product(product)
                    .quantity(itemDto.quantity())
                    .unitPrice(itemDto.unitPrice())
                    .unitCostAtSale(unitCostAtSale)
                    .netProfit(totalItemProfit)
                    .build();
            
            saleItems.add(saleItem);
            totalAmount = totalAmount.add(itemDto.unitPrice().multiply(BigDecimal.valueOf(itemDto.quantity())));
            totalNetProfit = totalNetProfit.add(totalItemProfit);
        }

        sale.setItems(saleItems);
        sale.setTotalAmount(totalAmount);
        sale.setTotalNetProfit(totalNetProfit);

        return saleRepository.save(sale);
    }
}