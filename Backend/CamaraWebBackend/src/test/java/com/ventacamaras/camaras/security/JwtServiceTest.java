package com.ventacamaras.camaras.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.ventacamaras.camaras.model.Role;
import com.ventacamaras.camaras.model.User;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void getToken_shouldIncludeRolesClaim() {
        User user = User.builder()
                .username("admin")
                .password("secret")
                .enabled(true)
                .roles(Set.of(new Role(null, "ADMIN")))
                .build();

        String token = jwtService.getToken(user);

        assertNotNull(token);
        assertEquals("admin", jwtService.getUsernameFromToken(token));

        List<?> roles = jwtService.getClaim(token, claims -> claims.get("roles", List.class));
        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals("ADMIN", roles.getFirst());
    }
}
