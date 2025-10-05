package com.precificapro.controller;

import com.precificapro.controller.dto.FreightBatchCreateDTO;
import com.precificapro.controller.dto.FreightBatchResponseDTO;
import com.precificapro.domain.model.User;
import com.precificapro.service.FreightBatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/freight-batches")
public class FreightBatchController {

    @Autowired
    private FreightBatchService freightBatchService;
    
    @PostMapping
    public ResponseEntity<FreightBatchResponseDTO> createFreightBatch(
            @Valid @RequestBody FreightBatchCreateDTO dto,
            @AuthenticationPrincipal User owner
    ) {
        FreightBatchResponseDTO createdBatch = freightBatchService.createFreightBatch(dto, owner);
        return new ResponseEntity<>(createdBatch, HttpStatus.CREATED);
    }
    
    // Implementar os outros endpoints (GET, DELETE)
}