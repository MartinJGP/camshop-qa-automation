package com.ventacamaras.camaras.dto;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequest {
@NotBlank @Size(max = 255)
    private String nombre;
@NotBlank @Email @Size(max = 255)
    private String email;
@Size(max = 255)
    private String direccion;
@Size(max = 255)
    private String telefono;
@Positive
    private Long userId;
    private String dni;
    private String ruc;
}

