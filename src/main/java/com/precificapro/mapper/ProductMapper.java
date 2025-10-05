package com.precificapro.mapper;

import com.precificapro.controller.dto.ProductCreateDTO;
import com.precificapro.controller.dto.ProductResponseDTO;
import com.precificapro.controller.dto.ProductUpdateDTO;
import com.precificapro.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductCreateDTO dto);
    ProductResponseDTO toResponseDTO(Product product);
    void updateEntityFromDto(ProductUpdateDTO dto, @MappingTarget Product product);
}