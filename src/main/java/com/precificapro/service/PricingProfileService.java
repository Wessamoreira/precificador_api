package com.precificapro.service;

import com.precificapro.controller.dto.PricingProfileCreateDTO;
import com.precificapro.controller.dto.PricingProfileResponseDTO;
import com.precificapro.domain.enums.PricingMethod;
import com.precificapro.domain.model.PricingProfile;
import com.precificapro.domain.model.User;
import com.precificapro.domain.repository.PricingProfileRepository;
import com.precificapro.mapper.PricingProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PricingProfileService {

    @Autowired
    private PricingProfileRepository profileRepository;

    @Autowired
    private PricingProfileMapper profileMapper;

    @Transactional
    public PricingProfileResponseDTO createProfile(PricingProfileCreateDTO dto, User owner) {
        // Regra de negócio: Valida se os campos de método são consistentes
        if (dto.method() == PricingMethod.MARKUP && dto.markup() == null) {
            throw new IllegalArgumentException("Markup não pode ser nulo quando o método é MARKUP.");
        }
        if (dto.method() == PricingMethod.MARGIN && dto.marginOnPrice() == null) {
            throw new IllegalArgumentException("MarginOnPrice não pode ser nulo quando o método é MARGIN.");
        }

        PricingProfile profile = profileMapper.toEntity(dto);
        profile.setOwner(owner); // Associação de segurança com o usuário logado

        PricingProfile savedProfile = profileRepository.save(profile);
        return profileMapper.toResponseDTO(savedProfile);
    }

    @Transactional(readOnly = true)
    public PricingProfileResponseDTO findProfileById(UUID id, User owner) {
        PricingProfile profile = profileRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new RuntimeException("Perfil de precificação não encontrado."));
        return profileMapper.toResponseDTO(profile);
    }

    @Transactional(readOnly = true)
    public List<PricingProfileResponseDTO> findAllByOwner(User owner) {
        return profileRepository.findByOwner(owner).stream()
                .map(profileMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProfile(UUID id, User owner) {
        // A busca por ID e Owner garante que o usuário só pode deletar o que é seu
        if (!profileRepository.existsByIdAndOwner(id, owner)) {
             throw new RuntimeException("Perfil de precificação não encontrado.");
        }
        profileRepository.deleteById(id);
    }
}