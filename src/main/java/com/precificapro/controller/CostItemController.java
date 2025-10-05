package com.precificapro.controller;

import com.precificapro.controller.dto.CostItemCreateDTO;
import com.precificapro.controller.dto.CostItemResponseDTO;
import com.precificapro.domain.model.User;
import com.precificapro.service.CostItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cost-items")
public class CostItemController {

    @Autowired
    private CostItemService costItemService;
    
    @PostMapping
    public ResponseEntity<CostItemResponseDTO> createCostItem(
            @Valid @RequestBody CostItemCreateDTO dto,
            @AuthenticationPrincipal User owner
    ) {
        CostItemResponseDTO createdCostItem = costItemService.createCostItem(dto, owner);
        return new ResponseEntity<>(createdCostItem, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CostItemResponseDTO> getCostItemById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User owner
    ) {
        return ResponseEntity.ok(costItemService.findCostItemById(id, owner));
    }
    
    @GetMapping
    public ResponseEntity<List<CostItemResponseDTO>> getAllCostItemsByUser(
            @AuthenticationPrincipal User owner
    ) {
        return ResponseEntity.ok(costItemService.findAllByOwner(owner));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostItem(
            @PathVariable UUID id,
            @AuthenticationPrincipal User owner
    ) {
        costItemService.deleteCostItem(id, owner);
        return ResponseEntity.noContent().build();
    }
}