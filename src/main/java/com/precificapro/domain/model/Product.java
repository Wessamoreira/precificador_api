package com.precificapro.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
// CORREÇÃO: Adicionamos a restrição para garantir que o par (owner_id, sku) seja único no banco
@Table(name = "products", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"owner_id", "sku"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY) // Relacionamento com o usuário dono do produto
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sku;

    @Column(name = "default_purchase_cost", nullable = false)
    private BigDecimal defaultPurchaseCost;

    @Column(name = "default_packaging_cost", nullable = false)
    private BigDecimal defaultPackagingCost;

    @Column(name = "default_other_variable_cost", nullable = false)
    private BigDecimal defaultOtherVariableCost;
    
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    public void onPrePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}