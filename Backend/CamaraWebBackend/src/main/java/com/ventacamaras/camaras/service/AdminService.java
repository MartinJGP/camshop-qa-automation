package com.ventacamaras.camaras.service;

import com.ventacamaras.camaras.dto.UserResponse;
import com.ventacamaras.camaras.model.Role;
import com.ventacamaras.camaras.model.User;
import com.ventacamaras.camaras.repository.RoleRepository;
import com.ventacamaras.camaras.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.ventacamaras.camaras.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<UserResponse> listarUsuarios() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @Transactional
    public UserResponse cambiarEstado(Long id, boolean enabled) {
        User user = obtener(id);
        user.setEnabled(enabled);
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse cambiarRol(Long id, String nombreRol) {
        String nombre = nombreRol.trim().toUpperCase();
        if (!Set.of("ADMIN", "VENDEDOR", "CLIENTE").contains(nombre)) {
            throw new IllegalArgumentException("Rol no permitido");
        }
        Role role = roleRepository.findByName(nombre).orElseGet(() -> {
            Role nuevo = new Role();
            nuevo.setName(nombre);
            return roleRepository.save(nuevo);
        });
        User user = obtener(id);
        user.getRoles().clear();
        user.getRoles().add(role);
        return UserResponse.from(userRepository.save(user));
    }

    private User obtener(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
}

