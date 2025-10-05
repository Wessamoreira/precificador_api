package com.precificapro.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerUpdateDTO(
    @NotBlank String name,
    @NotBlank String phoneNumber,
    String email
) {}