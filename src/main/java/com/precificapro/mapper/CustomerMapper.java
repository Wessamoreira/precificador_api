package com.precificapro.mapper;

import com.precificapro.controller.dto.CustomerCreateDTO;
import com.precificapro.controller.dto.CustomerResponseDTO;
import com.precificapro.controller.dto.CustomerUpdateDTO;
import com.precificapro.domain.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerCreateDTO dto);
    CustomerResponseDTO toResponseDTO(Customer customer);
    void updateEntityFromDto(CustomerUpdateDTO dto, @MappingTarget Customer customer);
}