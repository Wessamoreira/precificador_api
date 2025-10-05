package com.precificapro.domain.model;

import com.precificapro.domain.enums.CostItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "cost_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CostItemType type;

    @Column(name = "amount_monthly", nullable = false)
    private BigDecimal amountMonthly;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    
    @PrePersist
    public void onPrePersist() {
        createdAt = OffsetDateTime.now();
    }
}