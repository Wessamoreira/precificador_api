package com.precificapro.controller;

import com.precificapro.controller.dto.SimulationRequestDTO;
import com.precificapro.controller.dto.SimulationResponseDTO;
import com.precificapro.domain.model.User;
import com.precificapro.service.PricingSimulationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulations")
public class SimulationController {

    @Autowired
    private PricingSimulationService simulationService;

    @PostMapping("/calculate")
    public ResponseEntity<SimulationResponseDTO> calculate(
            @Valid @RequestBody SimulationRequestDTO request,
            @AuthenticationPrincipal User owner) {
        
        SimulationResponseDTO response = simulationService.simulate(request, owner);
        return ResponseEntity.ok(response);
    }
}