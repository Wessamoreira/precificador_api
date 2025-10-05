package com.precificapro.domain.repository;

import com.precificapro.domain.model.CostItem;
import com.precificapro.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CostItemRepository extends JpaRepository<CostItem, UUID> {
    Optional<CostItem> findByIdAndOwner(UUID id, User owner);
    List<CostItem> findByOwner(User owner);
    
    List<CostItem> findByOwnerAndActiveTrue(User owner);
}