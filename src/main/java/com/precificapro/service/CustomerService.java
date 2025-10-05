package com.precificapro.service;

import com.precificapro.controller.dto.CustomerCreateDTO;
import com.precificapro.controller.dto.CustomerResponseDTO;
import com.precificapro.controller.dto.CustomerUpdateDTO;
import com.precificapro.domain.model.Customer;
import com.precificapro.domain.model.User;
import com.precificapro.domain.repository.CustomerRepository;
import com.precificapro.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private CustomerMapper customerMapper;

    @Transactional
    public CustomerResponseDTO createCustomer(CustomerCreateDTO dto, User owner) {
        if (customerRepository.existsByOwnerAndPhoneNumber(owner, dto.phoneNumber())) {
            throw new RuntimeException("Já existe um cliente com este número de telefone.");
        }
        Customer customer = customerMapper.toEntity(dto);
        customer.setOwner(owner);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDTO(savedCustomer);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> findAllByOwner(User owner) {
        return customerRepository.findByOwner(owner).stream()
                .map(customerMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO findByIdAndOwner(UUID id, User owner) {
        Customer customer = customerRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
        return customerMapper.toResponseDTO(customer);
    }

    @Transactional
    public CustomerResponseDTO updateCustomer(UUID id, CustomerUpdateDTO dto, User owner) {
        Customer customer = customerRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        // Verifica se o novo telefone já pertence a outro cliente
        customerRepository.findByOwnerAndPhoneNumber(owner, dto.phoneNumber()).ifPresent(existingCustomer -> {
            if (!existingCustomer.getId().equals(id)) {
                throw new RuntimeException("Este número de telefone já está em uso por outro cliente.");
            }
        });

        customerMapper.updateEntityFromDto(dto, customer);
        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDTO(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(UUID id, User owner) {
        Customer customer = customerRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
        customerRepository.delete(customer);
    }
}