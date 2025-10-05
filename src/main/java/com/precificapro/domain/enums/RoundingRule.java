package com.precificapro.domain.enums;

public enum RoundingRule {
    NONE, // Nenhum arredondamento
    UP_TO_0_50, // Arredonda pra cima para terminar em _.50
    UP_TO_0_90, // Arredonda pra cima para terminar em _.90
    UP_TO_0_99  // Arredonda pra cima para terminar em _.99
}