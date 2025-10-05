package com.precificapro.controller;

import com.precificapro.controller.dto.CustomerCreateDTO;
import com.precificapro.controller.dto.CustomerResponseDTO;
import com.precificapro.controller.dto.CustomerUpdateDTO;
import com.precificapro.domain.model.User;
import com.precificapro.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(
            @Valid @RequestBody CustomerCreateDTO dto, @AuthenticationPrincipal User owner) {
        CustomerResponseDTO createdCustomer = customerService.createCustomer(dto, owner);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers(@AuthenticationPrincipal User owner) {
        return ResponseEntity.ok(customerService.findAllByOwner(owner));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(
            @PathVariable UUID id, @AuthenticationPrincipal User owner) {
        return ResponseEntity.ok(customerService.findByIdAndOwner(id, owner));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable UUID id, @Valid @RequestBody CustomerUpdateDTO dto, @AuthenticationPrincipal User owner) {
        return ResponseEntity.ok(customerService.updateCustomer(id, dto, owner));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id, @AuthenticationPrincipal User owner) {
        customerService.deleteCustomer(id, owner);
        return ResponseEntity.noContent().build();
    }
}