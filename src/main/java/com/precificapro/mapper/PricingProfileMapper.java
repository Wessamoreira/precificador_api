package com.precificapro.mapper;

import com.precificapro.controller.dto.PricingProfileCreateDTO;
import com.precificapro.controller.dto.PricingProfileResponseDTO;
import com.precificapro.domain.model.PricingProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PricingProfileMapper {
    PricingProfile toEntity(PricingProfileCreateDTO dto);
    PricingProfileResponseDTO toResponseDTO(PricingProfile profile);
}