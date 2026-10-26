package com.ventacamaras.camaras.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ventacamaras.camaras.dto.AuthResponse;
import com.ventacamaras.camaras.dto.LoginRequest;
import com.ventacamaras.camaras.dto.RegisterRequest;
import com.ventacamaras.camaras.model.Role;
import com.ventacamaras.camaras.model.User;
import com.ventacamaras.camaras.repository.RoleRepository;
import com.ventacamaras.camaras.repository.UserRepository;
import com.ventacamaras.camaras.repository.ClienteRepository;
import com.ventacamaras.camaras.model.Cliente;
import com.ventacamaras.camaras.security.JwtService;
import lombok.RequiredArgsConstructor;
import com.ventacamaras.camaras.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ClienteRepository clienteRepository;

    /**
     * Registra un nuevo usuario
     */
    public AuthResponse register(RegisterRequest request) {
        // Validar que el usuario no exista
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        // Obtener o crear rol por defecto
        Role roleCliente = roleRepository.findByName("CLIENTE")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("CLIENTE");
                    return roleRepository.save(newRole);
                });

        // Crear nuevo usuario
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .build();

        // Asignar rol al usuario
        user.getRoles().add(roleCliente);

        // Guardar usuario
        userRepository.save(user);

        Cliente cliente = Cliente.builder()
                .nombre(request.getUsername().split("@")[0])
                .email(request.getUsername())
                .user(user)
                .build();
        clienteRepository.save(cliente);

        // Generar token JWT
        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .token(token)
                .roles(java.util.List.of("CLIENTE"))
                .build();
    }

    /**
     * Autentica un usuario existente
     */
    public AuthResponse login(LoginRequest request) {
        // Autenticar usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Obtener usuario
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Generar token JWT
        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .token(token)
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }
    //retornar usuario por username
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
}



