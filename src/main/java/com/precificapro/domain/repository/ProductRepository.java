package com.precificapro.domain.repository;

import com.precificapro.domain.model.Product;
import com.precificapro.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    // Busca todos os produtos de um usuário específico
    List<Product> findByOwner(User owner);
    
    // Busca um produto pelo seu ID E pelo seu dono
    Optional<Product> findByIdAndOwner(UUID id, User owner);

    // Verifica se já existe um produto com um SKU para um determinado dono
    boolean existsBySkuAndOwner(String sku, User owner);
    
 // Em ProductRepository.java
    boolean existsByIdAndOwner(UUID id, User owner);
    Optional<Product> findBySkuAndOwner(String sku, User owner);
    
 // MÉTODO NOVO PARA O DASHBOARD
    long countByOwner(User owner);
}