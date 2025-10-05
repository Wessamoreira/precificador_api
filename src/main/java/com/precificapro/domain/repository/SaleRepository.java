package com.precificapro.domain.repository;

import com.precificapro.domain.model.Sale;
import com.precificapro.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {
    List<Sale> findAllByOwnerOrderBySaleDateDesc(User owner);
}