package com.ventacamaras.camaras.config;

import com.ventacamaras.camaras.model.Camara;
import com.ventacamaras.camaras.model.Categoria;
import com.ventacamaras.camaras.model.Cliente;
import com.ventacamaras.camaras.model.Role;
import com.ventacamaras.camaras.model.User;
import com.ventacamaras.camaras.repository.CamaraRepository;
import com.ventacamaras.camaras.repository.CategoriaRepository;
import com.ventacamaras.camaras.repository.ClienteRepository;
import com.ventacamaras.camaras.repository.RoleRepository;
import com.ventacamaras.camaras.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;
    private final CategoriaRepository categoriaRepository;
    private final CamaraRepository camaraRepository;

    @Value("${app.admin.email:admin@camshop.com}")
    private String adminEmail;
    @Value("${app.admin.password:Admin123!}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Roles del sistema
        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ADMIN");
            return roleRepository.save(role);
        });

        Role clienteRole = roleRepository.findByName("CLIENTE").orElseGet(() -> {
            Role role = new Role();
            role.setName("CLIENTE");
            return roleRepository.save(role);
        });

        // 2. Usuario Administrador
        User admin = userRepository.findByUsername(adminEmail).orElseGet(() -> User.builder()
                .username(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .enabled(true)
                .build());
        admin.setEnabled(true);
        if (!admin.getRoles().contains(adminRole)) {
            admin.getRoles().add(adminRole);
        }
        userRepository.save(admin);

        // 3. Usuario Cliente para Pruebas QA (Happy Path)
        String testUserEmail = "juan.perez@email.com";
        User testUser = userRepository.findByUsername(testUserEmail).orElseGet(() -> User.builder()
                .username(testUserEmail)
                .password(passwordEncoder.encode("SecurityPass123!"))
                .enabled(true)
                .build());
        testUser.setEnabled(true);
        if (!testUser.getRoles().contains(clienteRole)) {
            testUser.getRoles().add(clienteRole);
        }
        userRepository.save(testUser);

        // 4. Perfil de Cliente Asociado (Requerido para Checkout)
        if (clienteRepository.findByUserUsername(testUserEmail).isEmpty()) {
            Cliente cliente = Cliente.builder()
                    .nombre("Juan Pérez")
                    .email(testUserEmail)
                    .telefono("998877665")
                    .direccion("Av. Javier Prado Este 1234, San Isidro")
                    .user(testUser)
                    .build();
            clienteRepository.save(cliente);
        }

        // 5. Categoría y Cámara de Prueba en Catálogo
        if (camaraRepository.count() == 0) {
            Categoria categoriaDomo = categoriaRepository.findByNombreIgnoreCase("Cámaras Domo").orElseGet(() -> {
                Categoria cat = new Categoria();
                cat.setNombre("Cámaras Domo");
                cat.setDescripcion("Cámaras domo antivandálicas para interiores y exteriores");
                cat.setActivo(true);
                return categoriaRepository.save(cat);
            });

            Camara camara = new Camara();
            camara.setNombre("Hikvision Domo IP 4K UltraHD");
            camara.setMarca("Hikvision");
            camara.setModelo("DS-2CD2183G0-I");
            camara.setResolucion("4K (8MP)");
            camara.setPrecio(389.90);
            camara.setStock(25);
            camara.setDescripcion("Cámara Domo IP antivandálica IK10 con infrarrojos EXIR hasta 30m.");
            camara.setActivo(true);
            camara.setCategoria(categoriaDomo);
            camara.setImagenes(new ArrayList<>());
            camaraRepository.save(camara);
        }
    }
}
