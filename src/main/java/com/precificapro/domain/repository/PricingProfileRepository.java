package com.precificapro.domain.repository;

import com.precificapro.domain.model.PricingProfile;
import com.precificapro.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PricingProfileRepository extends JpaRepository<PricingProfile, UUID> {

    // Busca um perfil pelo seu ID e pelo seu dono (para segurança)
    Optional<PricingProfile> findByIdAndOwner(UUID id, User owner);

    // Busca todos os perfis de um usuário específico
    List<PricingProfile> findByOwner(User owner);
    
 // Em PricingProfileRepository.java
    boolean existsByIdAndOwner(UUID id, User owner);
}