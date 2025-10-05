package com.precificapro.service;

import com.precificapro.controller.dto.FreightBatchCreateDTO;
import com.precificapro.controller.dto.FreightBatchResponseDTO;
import com.precificapro.domain.model.FreightBatch;
import com.precificapro.domain.model.Product;
import com.precificapro.domain.model.User;
import com.precificapro.domain.repository.FreightBatchRepository;
import com.precificapro.domain.repository.ProductRepository;
import com.precificapro.mapper.FreightBatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FreightBatchService {

    @Autowired
    private FreightBatchRepository freightBatchRepository;
    @Autowired
    private ProductRepository productRepository; // Precisamos para verificar a posse do produto
    @Autowired
    private FreightBatchMapper freightBatchMapper;

    @Transactional
    public FreightBatchResponseDTO createFreightBatch(FreightBatchCreateDTO dto, User owner) {
        // DUPLA VERIFICAÇÃO DE SEGURANÇA:
        // 1. Busca o produto pelo ID fornecido.
        // 2. Garante que o produto encontrado pertence ao 'owner' logado.
        Product product = productRepository.findByIdAndOwner(dto.productId(), owner)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado ou não pertence a este usuário."));

        FreightBatch freightBatch = freightBatchMapper.toEntity(dto);
        freightBatch.setOwner(owner); // Define o dono do registro de frete
        freightBatch.setProduct(product); // Associa ao produto já verificado

        FreightBatch savedFreightBatch = freightBatchRepository.save(freightBatch);
        return freightBatchMapper.toResponseDTO(savedFreightBatch);
    }
    
    // Implementar findById, findAll e delete seguindo o mesmo padrão dos outros serviços
}