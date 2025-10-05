package com.precificapro.domain.repository;

import com.precificapro.domain.model.Customer;
import com.precificapro.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByOwnerAndPhoneNumber(User owner, String phoneNumber);
    Optional<Customer> findByIdAndOwner(UUID id, User owner);
    List<Customer> findByOwner(User owner);
    boolean existsByOwnerAndPhoneNumber(User owner, String phoneNumber);
    
 // MÉTODO NOVO PARA O DASHBOARD
    long countByOwner(User owner);
}