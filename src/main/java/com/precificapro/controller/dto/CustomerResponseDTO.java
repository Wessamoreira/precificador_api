package com.precificapro.controller.dto;

import java.util.UUID;

public record CustomerResponseDTO(
    UUID id,
    String name,
    String phoneNumber,
    String email
) {}