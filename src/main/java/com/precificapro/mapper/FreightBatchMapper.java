package com.precificapro.mapper;

import com.precificapro.controller.dto.FreightBatchCreateDTO;
import com.precificapro.controller.dto.FreightBatchResponseDTO;
import com.precificapro.domain.model.FreightBatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FreightBatchMapper {
    FreightBatch toEntity(FreightBatchCreateDTO dto);
    
    @Mapping(source = "product.id", target = "productId")
    FreightBatchResponseDTO toResponseDTO(FreightBatch freightBatch);
}