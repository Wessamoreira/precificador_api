package com.precificapro.controller.dto;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken
) {}