package com.precificapro.service;

import com.precificapro.controller.dto.RegisterRequestDTO;
import com.precificapro.domain.model.User;
import com.precificapro.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequestDTO registerRequest) {
        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new RuntimeException("Erro: Email já está em uso!"); // Criar uma exceção customizada depois
        }

        User newUser = User.builder()
                .name(registerRequest.name())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .build();
        
        return userRepository.save(newUser);
    }
}