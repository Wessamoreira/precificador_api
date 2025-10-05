package com.precificapro.service;

import com.precificapro.controller.dto.CostItemCreateDTO;
import com.precificapro.controller.dto.CostItemResponseDTO;
import com.precificapro.domain.model.CostItem;
import com.precificapro.domain.model.User;
import com.precificapro.domain.repository.CostItemRepository;
import com.precificapro.mapper.CostItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CostItemService {

    @Autowired
    private CostItemRepository costItemRepository;
    @Autowired
    private CostItemMapper costItemMapper;

    @Transactional
    public CostItemResponseDTO createCostItem(CostItemCreateDTO dto, User owner) {
        CostItem costItem = costItemMapper.toEntity(dto);
        costItem.setOwner(owner); // GARANTIA DE SEGURANÇA
        CostItem savedCostItem = costItemRepository.save(costItem);
        return costItemMapper.toResponseDTO(savedCostItem);
    }
    
    @Transactional(readOnly = true)
    public CostItemResponseDTO findCostItemById(UUID id, User owner) {
        CostItem costItem = costItemRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new RuntimeException("Custo não encontrado."));
        return costItemMapper.toResponseDTO(costItem);
    }
    
    @Transactional(readOnly = true)
    public List<CostItemResponseDTO> findAllByOwner(User owner) {
        return costItemRepository.findByOwner(owner).stream()
                .map(costItemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteCostItem(UUID id, User owner) {
        CostItem costItem = costItemRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new RuntimeException("Custo não encontrado."));
        costItemRepository.delete(costItem);
    }
}