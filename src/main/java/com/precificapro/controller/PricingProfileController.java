package com.precificapro.controller;

import com.precificapro.controller.dto.PricingProfileCreateDTO;
import com.precificapro.controller.dto.PricingProfileResponseDTO;
import com.precificapro.domain.model.User;
import com.precificapro.service.PricingProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pricing-profiles")
public class PricingProfileController {

    @Autowired
    private PricingProfileService profileService;

    @PostMapping
    public ResponseEntity<PricingProfileResponseDTO> createProfile(
            @Valid @RequestBody PricingProfileCreateDTO dto,
            @AuthenticationPrincipal User owner // Pega o usuário da sessão autenticada
    ) {
        PricingProfileResponseDTO createdProfile = profileService.createProfile(dto, owner);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PricingProfileResponseDTO> getProfileById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User owner
    ) {
        return ResponseEntity.ok(profileService.findProfileById(id, owner));
    }

    @GetMapping
    public ResponseEntity<List<PricingProfileResponseDTO>> getAllProfilesByUser(
            @AuthenticationPrincipal User owner
    ) {
        return ResponseEntity.ok(profileService.findAllByOwner(owner));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable UUID id,
            @AuthenticationPrincipal User owner
    ) {
        profileService.deleteProfile(id, owner);
        return ResponseEntity.noContent().build();
    }
}