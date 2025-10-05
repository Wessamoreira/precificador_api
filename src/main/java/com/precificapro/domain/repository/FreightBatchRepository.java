package com.precificapro.domain.repository;

import com.precificapro.domain.model.FreightBatch;
import com.precificapro.domain.model.Product;
import com.precificapro.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FreightBatchRepository extends JpaRepository<FreightBatch, UUID> {
    Optional<FreightBatch> findByIdAndOwner(UUID id, User owner);
    List<FreightBatch> findByOwner(User owner);
    
    Optional<FreightBatch> findFirstByProductOrderByCreatedAtDesc(Product product);
}