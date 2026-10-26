package com.ventacamaras.camaras.config;

import com.ventacamaras.camaras.model.Role;
import com.ventacamaras.camaras.model.User;
import com.ventacamaras.camaras.repository.RoleRepository;
import com.ventacamaras.camaras.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:admin@camshop.com}")
    private String adminEmail;
    @Value("${app.admin.password:Admin123!}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) {
        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ADMIN");
            return roleRepository.save(role);
        });

        User admin = userRepository.findByUsername(adminEmail).orElseGet(() -> User.builder()
                .username(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .enabled(true)
                .build());
        admin.setEnabled(true);
        admin.getRoles().add(adminRole);
        userRepository.save(admin);
    }
}
