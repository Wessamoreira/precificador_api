package com.precificapro.controller;

import com.precificapro.controller.dto.DashboardMetricsDTO;
import com.precificapro.domain.model.User;
import com.precificapro.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<DashboardMetricsDTO> getMetrics(@AuthenticationPrincipal User owner) {
        return ResponseEntity.ok(dashboardService.getMetrics(owner));
    }
}