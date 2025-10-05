package com.precificapro.mapper;

import com.precificapro.controller.dto.SaleResponseDTO;
import com.precificapro.domain.model.Sale;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, SaleItemMapper.class})
public interface SaleMapper {
    SaleResponseDTO toResponseDTO(Sale sale);
}