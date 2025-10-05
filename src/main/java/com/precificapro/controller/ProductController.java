package com.precificapro.controller;

import com.precificapro.controller.dto.ProductCreateDTO;
import com.precificapro.controller.dto.ProductResponseDTO;
import com.precificapro.controller.dto.ProductUpdateDTO;
import com.precificapro.domain.model.User;
import com.precificapro.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductCreateDTO dto,
            @AuthenticationPrincipal User owner
    ) {
        ProductResponseDTO createdProduct = productService.createProduct(dto, owner);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User owner
    ) {
        return ResponseEntity.ok(productService.findProductById(id, owner));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(
            @AuthenticationPrincipal User owner
    ) {
        return ResponseEntity.ok(productService.findAllProductsByOwner(owner));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductUpdateDTO dto,
            @AuthenticationPrincipal User owner
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, dto, owner));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable UUID id,
            @AuthenticationPrincipal User owner
    ) {
        productService.deleteProduct(id, owner);
        return ResponseEntity.noContent().build();
    }
}