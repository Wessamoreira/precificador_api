package com.precificapro.controller;

import com.precificapro.controller.dto.SaleCreateDTO;
import com.precificapro.controller.dto.SaleResponseDTO; // Importe o DTO de resposta
import com.precificapro.domain.model.User;
import com.precificapro.mapper.SaleMapper; // Importe o Mapper
import com.precificapro.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired private SaleService saleService;
    @Autowired private SaleMapper saleMapper; // Injete o Mapper

    @PostMapping
    public ResponseEntity<?> recordSale(@RequestBody SaleCreateDTO dto, @AuthenticationPrincipal User owner) {
        saleService.recordSale(dto, owner);
        return new ResponseEntity<>("Venda registrada com sucesso.", HttpStatus.CREATED);
    }

    // NOVO ENDPOINT
    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> getAllSales(@AuthenticationPrincipal User owner) {
        List<SaleResponseDTO> sales = saleService.findAllByOwner(owner).stream()
                .map(saleMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sales);
    }
}