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
@Table(name = "freight_batches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreightBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Relação de segurança: quem é o dono deste registro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Relação de negócio: a qual produto este frete se aplica
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "batch_size", nullable = false)
    private int batchSize;

    @Column(name = "freight_total", nullable = false)
    private BigDecimal freightTotal;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    
    @PrePersist
    public void onPrePersist() {
        createdAt = OffsetDateTime.now();
    }
}