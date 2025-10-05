package com.precificapro.mapper;

import com.precificapro.controller.dto.SaleItemResponseDTO;
import com.precificapro.domain.model.SaleItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleItemMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    SaleItemResponseDTO toResponseDTO(SaleItem saleItem);
}