package com.ventacamaras.camaras.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.ventacamaras.camaras.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    @Value("${app.cors.allowed-origin-patterns:http://localhost:4200,http://127.0.0.1:4200}")
    private String allowedOriginPatterns;

    /**
     * Configurar el FilterChain de seguridad
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilitar CSRF (no es necesario con JWT)
                .csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())
                        .contentTypeOptions(org.springframework.security.config.Customizer.withDefaults())
                )
                // Usar autenticación stateless (sin sesiones)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configurar autorización de endpoints
                .authorizeHttpRequests(authz -> authz
                        // Permitir registro y login sin autenticación
                        .requestMatchers("/error", "/auth/register", "/auth/login", "/uploads/**").permitAll()
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/categorias/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/api/camaras/**", "/api/resenas/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/ordenes").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/ordenes/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/camaras/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/camaras/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/camaras/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/camaras/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/resenas/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/clientes").hasRole("ADMIN")
                        .requestMatchers("/clientes/me").authenticated()
                        .requestMatchers("/clientes/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/pagos").hasRole("ADMIN")
                        // Otros endpoints requieren autenticación
                        .anyRequest().authenticated()
                )
                // Agregar proveedor de autenticación
                .authenticationProvider(authenticationProvider())
                // Agregar el filtro JWT antes del filtro de autenticación por defecto
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Proveedor de autenticación usando contraseña
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

    /**
     * Manager de autenticación
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(
                java.util.Arrays.stream(allowedOriginPatterns.split(","))
                        .map(String::trim)
                        .filter(value -> !value.isBlank())
                        .toList()
        );

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        // Exponer el header Authorization para que el cliente Angular pueda leerlo si es necesario
        configuration.setExposedHeaders(List.of("Authorization"));

        // Cachear la preflight request por 1 hora
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;

    }

    /**
     * Encoder de contraseñas usando BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

