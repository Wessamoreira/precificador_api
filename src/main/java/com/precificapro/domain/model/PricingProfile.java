package com.precificapro.domain.model;

import com.precificapro.domain.enums.PricingMethod;
import com.precificapro.domain.enums.RoundingRule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "pricing_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PricingMethod method;

    private BigDecimal markup; // Ex: 0.80 para 80%

    @Column(name = "margin_on_price")
    private BigDecimal marginOnPrice; // Ex: 0.40 para 40%

    @Column(name = "machine_fee_pct")
    private BigDecimal machineFeePct;

    @Column(name = "marketplace_fee_pct")
    private BigDecimal marketplaceFeePct;

    @Column(name = "other_fees_pct")
    private BigDecimal otherFeesPct;

    @Column(name = "monthly_sales_target")
    private int monthlySalesTarget;

    @Enumerated(EnumType.STRING)
    @Column(name = "rounding_rule", nullable = false)
    private RoundingRule roundingRule;
    
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