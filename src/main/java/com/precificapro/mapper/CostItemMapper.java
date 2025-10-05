package com.precificapro.mapper;

import com.precificapro.controller.dto.CostItemCreateDTO;
import com.precificapro.controller.dto.CostItemResponseDTO;
import com.precificapro.domain.model.CostItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CostItemMapper {
    CostItem toEntity(CostItemCreateDTO dto);
    CostItemResponseDTO toResponseDTO(CostItem costItem);
}